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
  private int port;

  private Log logs = new Log();

  public EmbeddedQpidProperties(@Value("${spring.rabbitmq.port:5672}") final int port) {
    this.port = port;
  }

  @Data
  public class Log {
    private boolean startupLoggedToSystemOut = true;
  }
}
