package net.clintonmagro.embedded.qpid.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "embedded.qpid")
public class EmbeddedQpidProperties {

  private String configFilePath = "qpid-config.json";
  private boolean autoStart = true;
  private int port = 0;

  private Log logs = new Log();

  @Data
  public class Log {

    private boolean startupLoggedToSystemOut = true;
  }
}
