package com.mc.embedded.qpid.sample.adapter.messaging;

import com.mc.embedded.qpid.sample.config.AppQueueConfig;
import com.mc.embedded.qpid.sample.domain.model.MyMessage;
import com.mc.embedded.qpid.sample.domain.port.MessageSender;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMessageSender implements MessageSender {

  private RabbitTemplate rabbitTemplate;

  @Override
  public void sendMessage(final MyMessage myMessage) {
    rabbitTemplate.convertAndSend(AppQueueConfig.RK_MSG_OUT, myMessage);
  }
}
