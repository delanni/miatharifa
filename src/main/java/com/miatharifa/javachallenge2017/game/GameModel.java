package com.miatharifa.javachallenge2017.game;

import com.miatharifa.javachallenge2017.models.GameDescription;
import com.miatharifa.javachallenge2017.models.GameState;
import com.miatharifa.javachallenge2017.models.Player;
import com.miatharifa.javachallenge2017.models.Standing;

import java.util.HashMap;
import java.util.List;

public class GameModel {
    public HashMap<String, Player> players;
    public GameMap map;
    public Long broadcastSchedule;
    public Long commandSchedule;
    public Long internalSchedule;
    public Double battleSpeed;
    public Double captureSpeed;
    public Double unitCreateSpeed;

    public Long gameLength;
    public Double battleExponent;
    public Long minMovableArmySize;
    public Double planetExponent;

    public Long timeElapsed;
    public String gameStatus;
    public Long remainingPlayers;
    public List<Standing> standings;

    public void initialize(GameDescription description) {
        this.gameLength = description.gameLength;

        this.map = new GameMap(description.mapSizeX, description.mapSizeY, description.planets);

        this.players = new HashMap<>();
        description.players.forEach(p -> this.players.put(p.userID, p));

        this.broadcastSchedule = description.broadcastSchedule;
        this.commandSchedule = description.commandSchedule;
        this.internalSchedule = description.internalSchedule;

        this.battleSpeed = description.battleSpeed;
        this.captureSpeed = description.captureSpeed;
        this.unitCreateSpeed = description.unitCreateSpeed;
        this.battleExponent = description.battleExponent;
        this.minMovableArmySize = description.minMovableArmySize;
        this.planetExponent = description.planetExponent;
    }

    public double updateAndDiff(GameState gameStateUpdate) {
        this.gameStatus = gameStateUpdate.gameStatus;
        this.timeElapsed = gameStateUpdate.timeElapsed;
        gameStateUpdate.planetStates.forEach(planetState -> this.map.planets.get(planetState.planetID).setState(planetState));
        this.remainingPlayers = gameStateUpdate.remainingPlayers;
        this.standings = gameStateUpdate.standings;
        return 0.0;
    }

}
