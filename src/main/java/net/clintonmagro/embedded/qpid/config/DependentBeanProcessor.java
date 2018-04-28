package net.clintonmagro.embedded.qpid.config;

import java.util.Arrays;
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

/**
 * This DependentBeanProcessor will ensure that the specified classes are dependent on the EmbeddedBroker so that Spring does not load them up before the broker
 * has had the chance to start. This is important because these classes will attempt to communicate on the specified spring.rabbitmq.port and if the broker
 * is not yet running the context will fail to load.
 *
 * Classes that depend on the Broker
 * @see org.springframework.amqp.rabbit.connection.ConnectionFactory
 * @see org.springframework.amqp.core.AmqpAdmin
 * @see org.springframework.amqp.rabbit.core.RabbitTemplate
 * @see org.springframework.boot.autoconfigure.amqp.RabbitProperties
 *
 * The embedded broker class being depended on by the above classes
 * @see net.clintonmagro.embedded.qpid.broker.EmbeddedBroker
 */
public class DependentBeanProcessor implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {
    Arrays.asList(ConnectionFactory.class, AmqpAdmin.class, RabbitTemplate.class, RabbitProperties.class)
        .forEach(dependantBeanClass -> {
          final String[] dependantBeanNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, dependantBeanClass, true, false);

          for (final String beanName : dependantBeanNames) {
            final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            beanDefinition.setDependsOn(StringUtils.addStringToArray(beanDefinition.getDependsOn(), EmbeddedBrokerAutoConfiguration.EMBEDDED_QPID_BROKER_BEAN_NAME));
          }
        });
  }
}
