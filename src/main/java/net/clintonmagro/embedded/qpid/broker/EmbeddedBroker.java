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

  /**
   * The EmbeddedBroker will automatically start provided the embedded.broker.auto-start property is true (default behaviour).
   * Although this class extends SmartLifecycle to provide Springs with ways to auto handle start up and shutdown, it needed to be
   * force started because it needs to start immediately during instantiation to avoid the situation were other beans in
   * client code needs to make use of the broker but starting is delayed. Doing so ensures that the broker is started as soon as
   * possible.
   */
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
    System.setProperty("qpid.amqp_port", this.port.toString());
  }
}
