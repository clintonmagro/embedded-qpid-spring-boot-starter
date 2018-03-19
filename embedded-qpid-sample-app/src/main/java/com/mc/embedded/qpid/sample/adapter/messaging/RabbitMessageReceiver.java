package com.mc.embedded.qpid.sample.adapter.messaging;

import com.mc.embedded.qpid.sample.config.AppQueueConfig;
import com.mc.embedded.qpid.sample.domain.model.MyMessage;
import com.mc.embedded.qpid.sample.domain.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RabbitMessageReceiver {

  private MessageService messageService;

  @RabbitListener(id = "some-unique-id-123", queues = AppQueueConfig.Q_MSG_IN)
  public void receiveMessage(final MyMessage messageIn){
    messageService.receiveMessage(messageIn);
  }
}
