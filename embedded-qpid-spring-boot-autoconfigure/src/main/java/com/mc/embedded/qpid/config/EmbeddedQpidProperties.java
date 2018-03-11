package com.mc.embedded.qpid.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "embedded.qpid")
public class EmbeddedQpidProperties {

  private boolean autoStart = true;
}
