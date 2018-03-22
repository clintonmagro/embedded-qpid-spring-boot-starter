package net.clintonmagro.embedded.qpid.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "embedded.qpid")
public class EmbeddedQpidProperties {


  private boolean autoStart = true;
  private String configFilePath = "qpid-config.json";

  private String username;
  private String password;
  private int port;

  private Log logs = new Log();

  public EmbeddedQpidProperties(final RabbitProperties rabbitProperties) {
    this.port = rabbitProperties.getPort();
    this.username = rabbitProperties.getUsername();
    this.password = rabbitProperties.getPassword();
  }

  @Data
  public class Log {

    private boolean startupLoggedToSystemOut = true;
  }
}
