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
        System.out.println(command.toString());
        this.commanderInterface.sendCommand(command);
        this.gameModel.applyCommand(command);
    }
    public abstract void updateStateRoundStart(GameModel gameModel);
    public abstract void updateStateRoundEnd(GameModel gameModel);
    public abstract void initPlayer(GameDescription gameDescription, GameModel gameMap);

    protected void sendTroops(Planet from, Planet to, int armySize){
        Command c = new Command(from.planetID, to.planetID, armySize);
        System.out.println("Executing: " + c.toString());
        this.sendCommand(c);
    }
}
