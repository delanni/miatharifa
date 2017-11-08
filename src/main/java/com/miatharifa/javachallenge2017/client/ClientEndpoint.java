package com.miatharifa.javachallenge2017.client;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.miatharifa.javachallenge2017.data.Command;
import com.miatharifa.javachallenge2017.data.GameDescription;
import com.miatharifa.javachallenge2017.data.GameState;

import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

enum ParseState {
	WAIT_FOR_GAME_DESCRIPTION,
	WAIT_FOR_GAME_STATE
}

public class ClientEndpoint extends Endpoint implements MessageHandler.Whole<String> {
	private final GameDescriptionHandler descriptionHandler;
	private final GameStateUpdateHandler updateHandler;
	private Session session;
	private Gson gson;
	private ParseState state = ParseState.WAIT_FOR_GAME_DESCRIPTION;

	public interface GameDescriptionHandler {
		void onGameDescription(GameDescription description);
	}
	public interface GameStateUpdateHandler {
		void onGameStateUpdate(GameState gameState);
	}

	public ClientEndpoint(GameDescriptionHandler descriptionHandler, GameStateUpdateHandler updateHandler){
		super();
		this.descriptionHandler = descriptionHandler;
		this.updateHandler = updateHandler;
	}

	@Override
	public void onOpen(Session session, EndpointConfig config) {
		session.addMessageHandler(this);
		this.session = session;
		this.gson = new Gson();
	}

	@Override
	public void onMessage(String message) {
		try {
			if (this.state == ParseState.WAIT_FOR_GAME_DESCRIPTION) {
				GameDescription description = this.gson.fromJson(message, GameDescription.class);
				this.descriptionHandler.onGameDescription(description);
				this.state = ParseState.WAIT_FOR_GAME_STATE;
			} else {
				GameState gameState = this.gson.fromJson(message, GameState.class);
				this.updateHandler.onGameStateUpdate(gameState);
			}
		} catch (Exception e) {
			System.err.println("Exception while parsing JSON: " + message);
			e.printStackTrace();
		}
	}

	private void sendMessage(Command command) {
		try {
			session.getAsyncRemote().sendText(this.gson.toJson(command, Command.class));
		} catch (Exception e ){
			System.err.println("Exception while sending command " + command);
			e.printStackTrace();
		}
	}
}
