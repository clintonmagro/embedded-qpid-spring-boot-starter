package net.clintonmagro.embedded.qpid.config;

import net.clintonmagro.embedded.qpid.broker.EmbeddedBroker;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureBefore(RabbitAutoConfiguration.class)
@EnableConfigurationProperties(EmbeddedQpidProperties.class)
@ConditionalOnClass({ConnectionFactory.class, AmqpAdmin.class, RabbitTemplate.class, RabbitProperties.class})
public class EmbeddedBrokerAutoConfiguration {

  /*The bean name used by the embedded broker and also for qualifying the name from other beans*/
  static final String EMBEDDED_QPID_BROKER_BEAN_NAME = "embeddedQpidBroker";

  @Bean
  public static DependentBeanProcessor dependentBeanProcessor() {
    return new DependentBeanProcessor();
  }

  @Bean(EMBEDDED_QPID_BROKER_BEAN_NAME)
  @ConditionalOnMissingBean(EmbeddedBroker.class)
  public EmbeddedBroker embeddedBroker(final EmbeddedQpidProperties properties) {
    return new EmbeddedBroker(properties);
  }
}

