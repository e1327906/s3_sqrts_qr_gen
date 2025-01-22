package com.qre.cmel.message.sdk.service;

public interface MessageService {

    void send(String queue, Object message);
}
