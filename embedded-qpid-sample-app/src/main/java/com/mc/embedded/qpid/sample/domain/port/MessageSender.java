package com.mc.embedded.qpid.sample.domain.port;

import com.mc.embedded.qpid.sample.domain.model.MyMessage;

public interface MessageSender {

  void sendMessage(MyMessage myMessage);
}
