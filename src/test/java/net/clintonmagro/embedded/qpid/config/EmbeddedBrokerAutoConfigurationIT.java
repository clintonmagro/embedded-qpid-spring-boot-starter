package net.clintonmagro.embedded.qpid.config;

import static org.assertj.core.api.Assertions.assertThat;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import net.clintonmagro.embedded.qpid.broker.EmbeddedBroker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RunWith(JUnitParamsRunner.class)
public class EmbeddedBrokerAutoConfigurationIT {

  private ApplicationContextRunner contextRunner;

  @Before
  public void setup() {
    contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(EmbeddedBrokerAutoConfiguration.class));
  }

  @Test
  @Parameters({"false", "true"})
  public void checkThatContextLoadsAsExpected(final boolean expectedRunningState) {
    this.contextRunner
        .withUserConfiguration(NoConfiguration.class)
        .withPropertyValues("embedded.qpid.autoStart=" + Boolean.toString(expectedRunningState))
        .run((context) -> {
          assertThat(context).hasSingleBean(EmbeddedBroker.class);
          assertThat(context.getBean(EmbeddedBroker.class).isRunning()).isEqualTo(expectedRunningState);
        });
  }

  @Test
  public void amqpBeansAbleToConnectToEmbeddedAmqp() {
    this.contextRunner
        .withUserConfiguration(AmqpConfiguration.class)
        .run((context) -> {

          final EmbeddedBroker embeddedBroker = context.getBean(EmbeddedBroker.class);
          final EmbeddedQpidProperties qpidProperties = context.getBean(EmbeddedQpidProperties.class);
//          assertThat(embeddedBroker.getPort()).isEqualTo(qpidProperties.getPort());
//          assertThat(System.getProperty("spring.rabbitmq.port")).isEqualTo(qpidProperties.getPort());
        });
  }

  @Configuration
  static class NoConfiguration {

  }

  @Configuration
  @EnableAutoConfiguration
  @EnableConfigurationProperties({EmbeddedQpidProperties.class, RabbitProperties.class})
  static class AmqpConfiguration {

    @Bean
    public Exchange exchange(final AmqpAdmin amqpAdmin) {
      final DirectExchange directExchange = new DirectExchange("x.test.direct");
      amqpAdmin.declareExchange(directExchange);

      return directExchange;
    }
  }
}