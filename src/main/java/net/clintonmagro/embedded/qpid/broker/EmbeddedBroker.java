package net.clintonmagro.embedded.qpid.broker;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.clintonmagro.embedded.qpid.config.EmbeddedQpidProperties;
import org.apache.qpid.server.SystemLauncher;
import org.apache.qpid.server.SystemLauncherListener;
import org.springframework.context.SmartLifecycle;
import org.springframework.core.Ordered;
import org.springframework.util.SocketUtils;

@Slf4j
public class EmbeddedBroker implements SmartLifecycle {

  private final EmbeddedQpidProperties properties;
  private final SystemLauncher qpidLauncher;

  @Getter
  private Integer port;

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

    determinePort();
    return attributes;
  }

  private void determinePort() {
    this.port = this.properties.getPort() == 0 ? SocketUtils.findAvailableTcpPort() : this.properties.getPort();
    System.setProperty("spring.rabbitmq.port", this.port.toString());
    System.setProperty("qpid.amqp_port", this.port.toString());
  }
}
