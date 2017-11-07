package com.miatharifa.javachallenge2017.client;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

public class ClientEndpoint extends Endpoint implements MessageHandler.Whole<String> {
	private Session session;

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		session.addMessageHandler(this);
		this.session = session;
	}

	@Override
	public void onMessage(String message) {
		System.out.println(message);
	}

	private void sendMessage(String message) {
		session.getAsyncRemote().sendText(message);
	}
}
