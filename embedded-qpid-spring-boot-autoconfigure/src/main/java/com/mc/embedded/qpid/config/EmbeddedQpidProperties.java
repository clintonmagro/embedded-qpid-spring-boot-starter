package com.mc.embedded.qpid.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
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

  public EmbeddedQpidProperties(@Value("${spring.rabbitmq.port:5672}") final int port,
                                @Value("${spring.rabbitmq.username:guest}") final String username,
                                @Value("${spring.rabbitmq.password:guest}") final String password) {
    this.port = port;
    this.username = username;
    this.password = password;
  }

  @Data
  public class Log {
    private boolean startupLoggedToSystemOut = true;
  }
}
