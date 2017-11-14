package com.miatharifa.javachallenge2017.players;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.Command;
import com.miatharifa.javachallenge2017.models.GameDescription;
import com.miatharifa.javachallenge2017.models.Planet;
import com.miatharifa.javachallenge2017.models.StationedArmy;

import java.security.cert.CollectionCertStoreParameters;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PotatoPlayer extends AbstractPlayer {
    private static final Logger logger = Logger.getLogger(PotatoPlayer.class.toString());

    public PotatoPlayer(CommanderInterface commanderInterface) {
        super(commanderInterface);
    }

    public void updateState(GameModel gameModel) {
        this.gameModel = gameModel;

        List<StationedArmy> myArmies = this.gameModel.map.stationedArmies.stream().filter(x -> x.owner.equals(PlayerModel.NAME)).collect(Collectors.toList());


        for (StationedArmy a : myArmies) {
            if (!Objects.equals(a.planet.getPredominantForce(), PlayerModel.NAME)) {
                Planet optimalPlanet = getOptimalPlanet(a);
                this.sendCommand(new Command(a.planet.planetID, optimalPlanet.planetID, a.size));
            } else {
                if (isArmyUsable(a)) {
                    List<Planet> targets = this.getOptimalPlanets(a);
                    if (targets.size() > 1) {
                        this.sendCommand(new Command(a.planet.planetID, targets.get(0).planetID, this.gameModel.minMovableArmySize + 4));
                        if (a.size - this.gameModel.minMovableArmySize > this.gameModel.minMovableArmySize) {
                            this.sendCommand(new Command(a.planet.planetID, targets.get(1).planetID, a.size - this.gameModel.minMovableArmySize));
                        }
                    } else if (targets.size() == 1) {
                        this.sendCommand(new Command(a.planet.planetID, targets.get(0).planetID, a.size));
                    }
                }
            }
        }
    }

    private boolean isPlanetMine(Planet planet) {
        return PlayerModel.NAME.equals(planet.owner) || PlayerModel.NAME.equals(planet.getPredominantForce());
    }

    private boolean isPlanetCaptured(Planet planet) {
        return PlayerModel.NAME.equals(planet.owner) && planet.ownershipRatio > 0.99d;
    }

    private boolean isPlanetAttractive(Planet planet) {
        return planet.owner != null && planet.movingArmies.stream().noneMatch(x -> x.owner.equals(PlayerModel.NAME));
    }

    private boolean isArmyUsable(StationedArmy army) {
        return army.size >= this.gameModel.minMovableArmySize && army.owner.equals((PlayerModel.NAME))
                && isPlanetCaptured(army.planet);
    }

    private Planet getOptimalPlanet(StationedArmy army) {
        List<Planet> candidates = this.gameModel.map.getClosestPlanets(army.planet).subList(1, this.gameModel.map.planets.size());
        Optional<Planet> first = candidates.stream().filter(p -> !isPlanetCaptured(p) && p.movingArmies.size() < 2).findFirst();
        return first.orElseGet(() -> candidates.get(0));
    }

    private List<Planet> getOptimalPlanets(StationedArmy army) {
        List<Planet> o = this.gameModel.map.getClosestPlanets(army.planet).stream().filter(p -> !isPlanetMine(p)).collect(Collectors.toList());
        if (o.size() == 0) return new ArrayList<>();
        return o.subList(0, Math.min(2, o.size() - 1));
    }

    @Override
    public void initPlayer(GameDescription gameDescription, GameModel gameMap) {

    }
}
