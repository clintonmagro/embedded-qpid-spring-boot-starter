package net.clintonmagro.embedded.qpid.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.SocketUtils;

@Data
@Component
@ConfigurationProperties(prefix = "embedded.qpid")
public class EmbeddedQpidProperties {


  private boolean autoStart = true;
  private boolean randomPort = true;

  private String configFilePath = "qpid-config.json";

  private int port;

  private Log logs = new Log();

  public EmbeddedQpidProperties() {
    determinePort();
  }

  private void determinePort() {
    if (randomPort) {
      final Integer randomPort = SocketUtils.findAvailableTcpPort();
      System.setProperty("spring.rabbitmq.port", randomPort.toString());
      this.port = randomPort;
    } else {
      /* we want to get whatever rabbit properties default port is defined however
       * autowiring it at this stage would render the random port feature useless because
       * once spring initializes RabbitProperties bean, there is no other way to inject an
       * environment variable into it.
       *
       * RabbitProperties is guaranteed at this stage to depend on Qpid because DependentBeanProcessor
       * ensures this*/
      this.port = new RabbitProperties().getPort();
    }
  }

  @Data
  public class Log {

    private boolean startupLoggedToSystemOut = true;
  }
}
