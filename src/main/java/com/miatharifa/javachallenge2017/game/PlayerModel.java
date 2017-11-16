package com.miatharifa.javachallenge2017.game;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.miatharifa.javachallenge2017.models.Command;
import com.miatharifa.javachallenge2017.models.GameDescription;
import com.miatharifa.javachallenge2017.models.GameState;

import javax.websocket.*;

import com.miatharifa.javachallenge2017.players.AbstractPlayer;
import com.miatharifa.javachallenge2017.players.DumbPlayer;
import com.miatharifa.javachallenge2017.players.PotatoPlayer;
import com.miatharifa.javachallenge2017.ui.Ui;

import java.util.Objects;
import java.util.stream.Collectors;

abstract class PlayerClient extends Endpoint implements MessageHandler.Whole<String> {
    private Session session;
    protected Gson gson;
    public ParseState state = ParseState.WAIT_FOR_GAME_DESCRIPTION;
    final boolean withUi;
    public AbstractPlayer player;

    PlayerClient(boolean withUi) {
        this.withUi = withUi;
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
                this.state = ParseState.WAIT_FOR_GAME_STATE;
                this.initialize(description);
            } else {
                GameState gameState = this.gson.fromJson(message, GameState.class);
                this.updateState(gameState);
            }
        } catch (JsonIOException e) {
            System.err.println("Exception while parsing JSON: " + message);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Exception applying state");
            e.printStackTrace();

        }
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
        this.kill();
    }

    protected void sendMessage(Command command) {
        try {
            System.out.println(System.currentTimeMillis() + " - Sending command: " + command);
            session.getAsyncRemote().sendText(this.gson.toJson(command, Command.class));
            if (withUi) {
                Ui.sendCommand(command);
            }
        } catch (Exception e) {
            System.err.println("Exception while sending command " + command);
            e.printStackTrace();
        }
    }

    protected abstract void kill();

    protected abstract void updateState(GameState gameState);

    protected abstract void initialize(GameDescription description);
}

public class PlayerModel extends PlayerClient {
    public static final String NAME = "miathari";
    protected final GameModel gameModel;
    private long gameStartTime = 0;

    public PlayerModel(boolean withUi) {
        super(withUi);
        this.gameModel = new GameModel();
//        this.player = new DumbPlayer(this::sendMessage);
        this.player = new PotatoPlayer(this::sendMessage);
    }

    public void initialize(GameDescription description) {
        System.out.println("Initialized [" + description.internalSchedule + ", " + description.broadcastSchedule + ", " + description.commandSchedule + "]");
        this.gameModel.initialize(description);
        if (withUi) {
            Ui.init(this.gameModel);
        }
        this.player.initPlayer(description, this.gameModel);
    }

    public void updateState(GameState gameStateUpdate) {
        long lastUpdateAt = System.currentTimeMillis();
        long nextUpdateDue = lastUpdateAt + this.gameModel.broadcastSchedule;
        if (gameStateUpdate.timeElapsed == 0) {
            this.gameStartTime = System.currentTimeMillis();
        }
        System.out.println(System.currentTimeMillis() + " - Got update for time: "+ gameStateUpdate.timeElapsed + " (" +getEstimatedGameTime() + ") " + (getEstimatedGameTime() - gameStateUpdate.timeElapsed));


        this.gameModel.update(gameStateUpdate);
        this.player.updateStateRoundStart(this.gameModel);

        this.gameModel.progressToNextState();
        this.player.updateStateRoundEnd(this.gameModel);

        this.gameModel.reset();

        System.out.println("Answered in " + (System.currentTimeMillis() - lastUpdateAt) + "ms");

        if (withUi) {
            Ui.refresh(this.gameModel);

//            while((nextUpdateDue - System.currentTimeMillis()) > this.gameModel.internalSchedule && this.gameModel.simulationTicksRemaining > 0) {
//                try {
//                    Thread.sleep(this.gameModel.internalSchedule-5);
//                    this.gameModel.tick();
//                    Ui.refresh(this.gameModel);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            System.out.println("Current time is : " + System.currentTimeMillis() + " time until next update: " + (nextUpdateDue - System.currentTimeMillis()));
        }

    }

    private long getEstimatedGameTime() {
        return System.currentTimeMillis() - this.gameStartTime;
    }

    public void kill() {
        this.state = ParseState.KILLED;
        System.out.println("Stopped.");
    }
}
