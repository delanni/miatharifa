package com.miatharifa.javachallenge2017.players;

import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class DumbPlayer extends PlayerModel {
    private static final Logger logger = Logger.getLogger(DumbPlayer.class.toString());
    @Override public void updateState(GameState gameStateUpdate) {
        super.updateState(gameStateUpdate);

        ArrayList<Planet> planetsToAttack = new ArrayList<>();
        ArrayList<StationedArmy> armiesToMove = new ArrayList<>();

        for (Map.Entry<Long, Planet> entry : this.gameModel.getMap().planets.entrySet()) {
            Planet planet = entry.getValue();
            if (!isPlanetMine(planet) && isPlanetAttractive(planet)) {
                planetsToAttack.add(planet);
                continue;
            }

            for (StationedArmy army : planet.getStationedArmies()) {
                if (isArmyUsable(army)) {
                    armiesToMove.add(army);
                }
            }
        }

        for (StationedArmy army : armiesToMove) {
            long actualSize = army.size;
            while (actualSize > gameModel.getMinMovableArmySize() && planetsToAttack.size() > 0) {
                if (planetsToAttack.size() > 0) {
                    Planet target = getOptimalPlanet(army, planetsToAttack);
                    planetsToAttack.remove(target);
                    long targetArmy = target.getStationedArmies().isEmpty() ? actualSize / 2 : target.getStationedArmies().get(0).size + 15;
                    if (actualSize > targetArmy) {
                        this.sendMessage(new Command(army.planet.planetID, target.planetID, targetArmy));
                        actualSize -= targetArmy;
                    }
                }
                else break;
            }
        }
    }

    private boolean isPlanetMine(Planet planet) {
        return PlayerModel.NAME.equals(planet.getOwner()) && planet.getOwnershipRatio() >= 0.999d;
    }

    private boolean isPlanetAttractive(Planet planet) {
        return planet.getOwner() != null && !planet.getMovingArmies().stream().anyMatch(x -> x.owner.equals(NAME));
    }

    private boolean isArmyUsable(StationedArmy army) {
        return army.size > this.gameModel.getMinMovableArmySize() && army.owner.equals((PlayerModel.NAME))
            && isPlanetMine(army.planet);
    }

    private Planet getOptimalPlanet(StationedArmy army, ArrayList<Planet> planets) {
        int maxIdx = 0;

        for (int i = 1; i < planets.size(); i++) {
            if (this.gameModel.getMap().distanceOf(army.planet, this.gameModel.getMap().getPlanetByIdx(i)) < this.gameModel.getMap().distanceOf(army.planet, this.gameModel.getMap().getPlanetByIdx(maxIdx))) {
            //if (this.gameModel.getMap().getPlanetByIdx(i).radius > this.gameModel.getMap().getPlanetByIdx(maxIdx).radius) {
                maxIdx = i;
            }
        }

        return planets.get(maxIdx);
    }

    @Override
    protected void sendMessage(Command command) {
        logger.info(String.format("Sending message %s", command));
        super.sendMessage(command);
    }
}
