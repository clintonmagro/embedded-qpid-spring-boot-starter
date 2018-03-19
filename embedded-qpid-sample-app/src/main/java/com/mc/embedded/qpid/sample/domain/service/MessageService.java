package com.mc.embedded.qpid.sample.domain.service;

import com.mc.embedded.qpid.sample.domain.model.MessageType;
import com.mc.embedded.qpid.sample.domain.model.MyMessage;
import com.mc.embedded.qpid.sample.domain.port.MessageSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MessageService {

  private MessageSender messageSender;

  public void sendMessage(final String nickname, final MessageType messageType, final String payload) {
    final MyMessage msg = new MyMessage(nickname, messageType, payload);
    messageSender.sendMessage(msg);
  }

  public void receiveMessage(final MyMessage myMessage) {
    System.out.println(myMessage);
  }
}
