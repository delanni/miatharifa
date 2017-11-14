package com.miatharifa.javachallenge2017.game;

import com.miatharifa.javachallenge2017.models.*;
import javafx.util.Pair;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameModel {
    public HashMap<String, Player> players;
    public GameMap map;
    public Integer broadcastSchedule;
    public Integer commandSchedule;
    public Integer internalSchedule;
    public Double battleSpeed;
    public Double captureSpeed;
    public Double unitCreateSpeed;

    public Integer gameLength;
    public Double battleExponent;
    public Integer minMovableArmySize;
    public Double planetExponent;

    public Integer timeElapsed;
    public String gameStatus;
    public Integer remainingPlayers;
    public List<Standing> standings;
    public Double movementSpeed;
    private GameState lastUpdate;
    public int simulationTicksRemaining = 0;

    public void initialize(GameDescription description) {
        this.gameLength = description.gameLength;

        this.map = new GameMap(description.mapSizeX, description.mapSizeY, description.planets);

        this.players = new HashMap<>();
        description.players.forEach(p -> this.players.put(p.userID, p));

        this.broadcastSchedule = description.broadcastSchedule;
        this.commandSchedule = description.commandSchedule;
        this.internalSchedule = description.internalSchedule;

        this.battleSpeed = description.battleSpeed / 1000 * this.internalSchedule;
        this.captureSpeed = description.captureSpeed / 1000 * this.internalSchedule;
        this.unitCreateSpeed = description.unitCreateSpeed / 1000 * this.internalSchedule;
        this.movementSpeed = description.movementSpeed / 1000 * this.internalSchedule;

        this.battleExponent = description.battleExponent;
        this.planetExponent = description.planetExponent;

        this.minMovableArmySize = description.minMovableArmySize;
    }

    public double updateAndDiff(GameState gameStateUpdate) {
        this.lastUpdate = gameStateUpdate;

        while (this.simulationTicksRemaining > 0) {
            this.tick();
        }

        this.simulationTicksRemaining = this.broadcastSchedule / this.internalSchedule;

        this.gameStatus = gameStateUpdate.gameStatus;
        this.timeElapsed = gameStateUpdate.timeElapsed;

        this.map.movingArmies.clear();
        this.map.stationedArmies.clear();
        Double planetStateDiffs = gameStateUpdate.planetStates.stream()
                .map(planetState -> {
                    this.map.movingArmies.addAll(planetState.movingArmies);
                    this.map.stationedArmies.addAll(planetState.stationedArmies);
                    return this.map.planets.get(planetState.planetID).setState(planetState);
                })
                .reduce(0.0, (a, b) -> a + b);
        this.remainingPlayers = gameStateUpdate.remainingPlayers;
        this.standings = gameStateUpdate.standings;
        return planetStateDiffs;
    }

    public void reset() {
        this.updateAndDiff(this.lastUpdate);
    }

    public void tick() {
        this.simulationTicksRemaining -= 1;
        // Conquest
        Map<Integer, MovingArmy> arrivals = new HashMap<>();

        this.map.planets.values().forEach(p -> p.movingArmies.forEach(army -> {
            XVector position = army.getPosition();
            XVector planetPosition = p.getPosition();
            if (planetPosition.distance(position) < this.movementSpeed) {
                army.x = p.x.doubleValue();
                army.y = p.y.doubleValue();
                arrivals.put(p.planetID, army);
            } else {
                XVector newPosition = planetPosition.subtract(position).unit().scaleInPlace(this.movementSpeed).addInPlace(position);
                army.x = newPosition.x;
                army.y = newPosition.y;
            }
        }));

        arrivals.forEach((hostPlanetId, arrivingArmy) -> {
            Planet hostPlanet = this.map.planets.get(hostPlanetId);
            String ownerId = arrivingArmy.owner;
            Optional<StationedArmy> stationedArmy = hostPlanet.stationedArmies.stream().filter(a -> a.owner.equals(ownerId)).findFirst();
            if (stationedArmy.isPresent()) {
                stationedArmy.get().size += arrivingArmy.size;
            } else {
                hostPlanet.stationedArmies.add(new StationedArmy(ownerId, arrivingArmy.size, hostPlanet));
            }

            hostPlanet.movingArmies.remove(arrivingArmy);
        });

        this.map.planets.values().forEach(p -> {
            if (p.stationedArmies.size() > 1) {
                // Conbat
                int totalArmyOnPlanet = p.getTotalArmyCount();
                List<Pair<StationedArmy, Integer>> lossPairs = p.stationedArmies.stream().map(army -> {
                    int enemyCount = p.getEnemyCountFor(army.owner);

                    int lostArmy = (int) Math.round(Math.pow(this.battleSpeed * enemyCount, this.battleExponent) / totalArmyOnPlanet);
                    return new Pair<>(army, lostArmy);
                }).collect(Collectors.toList());

                lossPairs.forEach(pair -> pair.getKey().size -= pair.getValue());
            } else if (p.stationedArmies.size() == 1) {
                // Conquer
                StationedArmy army = p.stationedArmies.get(0);
                double ownershipRatioChange = army.size * this.captureSpeed / Math.pow(p.radius, this.planetExponent);
                if (!Objects.equals(p.owner, p.stationedArmies.get(0).owner)) {
                    p.ownershipRatio = Math.max(p.ownershipRatio - ownershipRatioChange, 0);
                } else if (p.ownershipRatio < 1.0) {
                    p.ownershipRatio = Math.min(p.ownershipRatio + ownershipRatioChange, 1.0);
                } else {
                    // Construct
                    army.size += (int) Math.round(Math.pow(p.radius, this.planetExponent) * this.unitCreateSpeed);
                }
            }
        });

    }

}
