package com.mc.embedded.qpid.sample.domain.service;

import com.mc.embedded.qpid.sample.domain.model.MessageType;
import com.mc.embedded.qpid.sample.domain.model.MyMessage;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  public void sendMessage(final String nickname, final MessageType messageType, final String payload){
    final MyMessage msg = new MyMessage(nickname, messageType, payload);
  }

  public void receiveMessage(final MyMessage myMessage){
    System.out.println(myMessage);
  }
}
