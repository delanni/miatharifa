package com.miatharifa.javachallenge2017.game;

import com.miatharifa.javachallenge2017.models.GameDescription;
import com.miatharifa.javachallenge2017.models.GameState;
import com.miatharifa.javachallenge2017.models.Player;
import com.miatharifa.javachallenge2017.models.Standing;

import java.util.HashMap;
import java.util.List;

public class GameModel {
    private HashMap<String, Player> players;
    private GameMap map;
    private Long broadcastSchedule;
    private Long commandSchedule;
    private Long internalSchedule;
    private Double battleSpeed;
    private Double captureSpeed;
    private Double unitCreateSpeed;
    private Long gameLength;
    private Double battleExponent;
    private Double planetExponent;

    private Long timeElapsed;
    private String gameStatus;
    private Long remainingPlayers;
    private List<Standing> standings;

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