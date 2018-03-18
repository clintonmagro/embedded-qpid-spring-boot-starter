package com.mc.embedded.qpid.broker;

import com.mc.embedded.qpid.config.EmbeddedQpidProperties;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.SystemLauncherListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.Ordered;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class EmbeddedBroker implements SmartLifecycle {

  private final EmbeddedQpidProperties properties;
  private final SystemLauncher qpidLauncher;

  private boolean running;

  public EmbeddedBroker(final EmbeddedQpidProperties properties) {
    this.properties = properties;
    this.qpidLauncher = new SystemLauncher(new SystemLauncherListener.DefaultSystemLauncherListener());

    if (properties.isAutoStart()) {
      start();
    }
  }

  @Override
  public boolean isAutoStartup() {
    return this.properties.isAutoStart();
  }

  @Override
  public void stop(final Runnable runnable) {
    log.trace("Stopping Embedded Broker Asynchronously");
    CompletableFuture
        .runAsync(this::stop)
        .thenRunAsync(runnable);
  }

  @Override
  @SneakyThrows
  public void start() {
    log.info("Starting Embedded Qpid broker");
    this.qpidLauncher.startup(createSystemConfig());
    this.running = true;
    log.info("Started Embedded Qpid broker");
  }

  @Override
  public void stop() {
    log.info("Stopping Embedded Qpid broker");
    this.qpidLauncher.shutdown();
    this.running = false;
    log.info("Stopped Embedded Qpid broker");
  }

  @Override
  public boolean isRunning() {
    return this.running;
  }

  @Override
  public int getPhase() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  private Map<String, Object> createSystemConfig() {
    final Map<String, Object> attributes = new HashMap<>();
    final URL initialConfig = EmbeddedBroker.class.getClassLoader().getResource(this.properties.getConfigFilePath());
    attributes.put("type", "Memory");
    attributes.put("initialConfigurationLocation", initialConfig.toExternalForm());
    attributes.put("startupLoggedToSystemOut", this.properties.getLogs().isStartupLoggedToSystemOut());

    attributes.put("qpid.amqp_port", this.properties.getPort());
    attributes.put("qpid.username", this.properties.getUsername());
    attributes.put("qpid.password", this.properties.getPassword());
    return attributes;
  }
}
