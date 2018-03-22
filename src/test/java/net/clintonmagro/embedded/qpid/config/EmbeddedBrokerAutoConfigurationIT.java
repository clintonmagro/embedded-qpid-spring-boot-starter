package net.clintonmagro.embedded.qpid.config;

import net.clintonmagro.embedded.qpid.broker.EmbeddedBroker;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

public class EmbeddedBrokerAutoConfigurationIT {


  private ApplicationContextRunner contextRunner;

  @Before
  public void setup() {
    contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(EmbeddedBrokerAutoConfiguration.class));
  }

  @Test
  public void checkThatContextLoadsAsExpected_notRunning() {
    this.contextRunner
        .withUserConfiguration(UserConfiguration.class)
        .withPropertyValues("embedded.qpid.autoStart=false")
        .run((context) -> {
          assertThat(context).hasSingleBean(EmbeddedBroker.class);
          assertThat(context.getBean(EmbeddedBroker.class).isRunning()).isFalse();
        });
  }

  @Test
  public void checkThatContextLoadsAsExpected_isRunning() {
    this.contextRunner
        .withUserConfiguration(UserConfiguration.class)
        .withPropertyValues("embedded.qpid.autoStart=true")
        .run((context) -> {
          assertThat(context).hasSingleBean(EmbeddedBroker.class);
          assertThat(context.getBean(EmbeddedBroker.class).isRunning()).isTrue();
        });
  }

  @Configuration
  static class UserConfiguration {

    @Bean
    public RabbitProperties embeddedQpidProperties() {
      return new RabbitProperties();
    }
  }

}