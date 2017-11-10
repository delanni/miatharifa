package com.miatharifa.javachallenge2017.players;

import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class DumbPlayer extends PlayerModel {
    private static final Logger logger = Logger.getLogger(DumbPlayer.class.toString());
    @Override public void updateState(GameState gameStateUpdate) {
        super.updateState(gameStateUpdate);

        ArrayList<Planet> planetsToAttack = new ArrayList<>();
        ArrayList<StationedArmy> armiesToMove = new ArrayList<>();

        for (Map.Entry<Long, Planet> entry : this.gameModel.getMap().planets.entrySet()) {
            Planet planet = entry.getValue();
            if (!isPlanetMine(planet)) {
                if (isPlanetAttractive(planet)) planetsToAttack.add(planet);
                continue;
            }

            for (StationedArmy army : planet.getStationedArmies()) {
                if (isArmyUsable(army)) {
                    armiesToMove.add(army);
                }
            }
        }

        for (StationedArmy army : armiesToMove) {
            if (planetsToAttack.size() > 0) {
                Planet target = planetsToAttack.remove(0);
                this.sendMessage(new Command(army.planet.planetID, target.planetID, Integer.MAX_VALUE));
            }
        }
    }

    private boolean isPlanetMine(Planet planet) {
        return PlayerModel.NAME.equals(planet.getOwner());
    }

    private boolean isPlanetAttractive(Planet planet) {
        return planet.getStationedArmies().isEmpty() && planet.getMovingArmies().isEmpty();
    }

    private boolean isArmyUsable(StationedArmy army) {
        return army.size > this.gameModel.getMinMovableArmySize() && army.owner.equals((PlayerModel.NAME));
    }

    @Override
    protected void sendMessage(Command command) {
        logger.info(String.format("Sending message %s", command));
        super.sendMessage(command);
    }
}
