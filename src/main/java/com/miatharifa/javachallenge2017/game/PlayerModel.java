package com.miatharifa.javachallenge2017.game;

import com.google.gson.Gson;
import com.miatharifa.javachallenge2017.models.Command;
import com.miatharifa.javachallenge2017.models.GameDescription;
import com.miatharifa.javachallenge2017.models.GameState;

import javax.websocket.*;

abstract class PlayerClient  extends Endpoint implements MessageHandler.Whole<String>{
    private Session session;
    private Gson gson;
    public ParseState state = ParseState.WAIT_FOR_GAME_DESCRIPTION;

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
                this.state = ParseState.WAIT_FOR_GAME_STATE;
                this.initialize(description);
            } else {
                GameState gameState = this.gson.fromJson(message, GameState.class);
                this.updateState(gameState);
            }
        } catch (Exception e) {
            System.err.println("Exception while parsing JSON: " + message);
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        this.kill();
    }

    private void sendMessage(Command command) {
        try {
            session.getAsyncRemote().sendText(this.gson.toJson(command, Command.class));
        } catch (Exception e ){
            System.err.println("Exception while sending command " + command);
            e.printStackTrace();
        }
    }

    protected abstract void kill();
    protected abstract void updateState(GameState gameState);
    protected abstract void initialize(GameDescription description);
}

public class PlayerModel extends PlayerClient {
    private final GameModel gameModel;

    public PlayerModel(){
        this.gameModel = new GameModel();
    }

    public void initialize(GameDescription description) {
        System.out.println("Initialized");
        this.gameModel.initialize(description);
    }

    public void updateState(GameState gameStateUpdate) {
        System.out.println("Got update for time:" + gameStateUpdate.timeElapsed);

        this.gameModel.updateAndDiff(gameStateUpdate);
    }

    public void kill(){
        this.state = ParseState.KILLED;
        System.out.println("Stopped.");
    }
}
