package com.mc.embedded.qpid.sample.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyMessage {

  private String nickname;
  private MessageType messageType;
  private String payload;
}
