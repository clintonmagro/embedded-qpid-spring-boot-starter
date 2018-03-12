package com.mc.embedded.qpid.broker;

import com.mc.embedded.qpid.config.EmbeddedQpidProperties;
import lombok.SneakyThrows;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.SystemLauncherListener;
import org.springframework.context.SmartLifecycle;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EmbeddedBroker implements SmartLifecycle {

  private final EmbeddedQpidProperties properties;
  private final SystemLauncher qpidLauncher;
  private boolean running;

  public EmbeddedBroker(final EmbeddedQpidProperties properties) {
    this.properties = properties;
    this.qpidLauncher = new SystemLauncher(new SystemLauncherListener.DefaultSystemLauncherListener());
  }

  @Override
  public boolean isAutoStartup() {
    return this.properties.isAutoStart();
  }

  @Override
  public void stop(final Runnable runnable) {
    runnable.run();
  }

  @Override
  @SneakyThrows
  public void start() {
    this.qpidLauncher.startup(createSystemConfig());
    this.running = true;
  }

  @Override
  public void stop() {
    this.qpidLauncher.shutdown();
    this.running = false;
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }

  @Override
  public int getPhase() {
    return 0;
  }

  private Map<String, Object> createSystemConfig() {
    final Map<String, Object> attributes = new HashMap<>();
    final URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(this.properties.getConfigFilePath());
    attributes.put("type", "Memory");
    attributes.put("qpid.amqp_port", this.properties.getPort());
    attributes.put("qpid.username", this.properties.getUsername());
    attributes.put("qpid.password", this.properties.getPassword());
    attributes.put("initialConfigurationLocation", initialConfig.toExternalForm());
    attributes.put("startupLoggedToSystemOut", this.properties.getLogs().isStartupLoggedToSystemOut());
    return attributes;
  }
}
