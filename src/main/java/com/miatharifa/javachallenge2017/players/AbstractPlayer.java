package com.miatharifa.javachallenge2017.players;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.models.*;

import java.util.logging.Logger;

public abstract class AbstractPlayer {
    private static final Logger logger = Logger.getLogger(DumbPlayer.class.toString());

    private final CommanderInterface commanderInterface;

    public GameModel gameModel;

    public interface CommanderInterface {
        void sendCommand(Command command);
    }

    public AbstractPlayer(CommanderInterface commanderInterface){
        this.commanderInterface = commanderInterface;
    }

    protected void sendCommand(Command command) {
        logger.info("Sending command" + command.toString());
        this.commanderInterface.sendCommand(command);
    }
    public abstract void updateState(GameModel gameStateUpdate);
    public abstract void initPlayer(GameDescription gameDescription, GameModel gameMap);
}
