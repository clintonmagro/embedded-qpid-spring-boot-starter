package com.mc.embedded.qpid.config;

import com.mc.embedded.qpid.broker.EmbeddedBroker;
import org.springframework.amqp.core.AmqpAdmin;
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
@ConditionalOnClass({RabbitTemplate.class, AmqpAdmin.class, RabbitProperties.class})
public class EmbeddedBrokerAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(EmbeddedBroker.class)
  public EmbeddedBroker embeddedBroker(final EmbeddedQpidProperties properties) {
    return new EmbeddedBroker(properties);
  }
}

