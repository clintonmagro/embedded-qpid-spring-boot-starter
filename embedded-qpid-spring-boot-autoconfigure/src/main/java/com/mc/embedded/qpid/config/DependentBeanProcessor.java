package com.mc.embedded.qpid.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.util.StringUtils;

import java.util.Arrays;

import static com.mc.embedded.qpid.config.EmbeddedBrokerAutoConfiguration.EMBEDDED_QPID_BROKER_BEAN_NAME;

public class DependentBeanProcessor implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
    Arrays.asList(ConnectionFactory.class, AmqpAdmin.class, RabbitTemplate.class)
        .forEach(dependantBeanClass -> {
          final String[] dependantBeanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, dependantBeanClass, true, false);

          for (final String beanName : dependantBeanNames) {
            final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            beanDefinition.setDependsOn(StringUtils.addStringToArray(beanDefinition.getDependsOn(), EMBEDDED_QPID_BROKER_BEAN_NAME));
          }
        });
  }
}
