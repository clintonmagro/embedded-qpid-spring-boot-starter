package com.mc.embedded.qpid.sample.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {

  public static final String EX_MESSAGES = "ex.messages";

  public static final String Q_MSG_OUT = "q.message.out";
  public static final String RK_MSG_OUT = "rk.message.out";


  public static final String Q_MSG_IN = "q.message.in";
  public static final String RK_MSG_IN = "rk.message.in";

  @Bean
  public Exchange exchange(final AmqpAdmin amqpAdmin) {
    final Exchange directExchange = new DirectExchange(EX_MESSAGES, true, false);
    amqpAdmin.declareExchange(directExchange);

    return directExchange;
  }

  @Bean
  public Queue messageOutQueue(final AmqpAdmin amqpAdmin, final Exchange exchange) {
    return getQueue(amqpAdmin, exchange, Q_MSG_OUT, RK_MSG_OUT);
  }

  @Bean
  public Queue messageInQueue(final AmqpAdmin amqpAdmin, final Exchange exchange) {
    return getQueue(amqpAdmin, exchange, Q_MSG_IN, RK_MSG_IN);
  }

  @Bean
  public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
    return new MappingJackson2MessageConverter();
  }

  @Bean
  public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
    final DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
    factory.setMessageConverter(consumerJackson2MessageConverter());
    return factory;
  }

  @Override
  public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
    registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
  }

  private Queue getQueue(AmqpAdmin amqpAdmin, Exchange exchange, String qMsgIn, String rkMsgIn) {
    final Queue messageInQueue = new Queue(qMsgIn, true, false, false);
    amqpAdmin.declareQueue(messageInQueue);

    final Binding binding = new Binding(messageInQueue.getName(),
        Binding.DestinationType.QUEUE,
        exchange.getName(),
        rkMsgIn,
        null);

    amqpAdmin.declareBinding(binding);

    return messageInQueue;
  }
}
