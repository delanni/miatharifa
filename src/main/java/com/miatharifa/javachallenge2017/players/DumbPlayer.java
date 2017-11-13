package com.miatharifa.javachallenge2017.players;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

public class DumbPlayer extends AbstractPlayer {
    private static final Logger logger = Logger.getLogger(DumbPlayer.class.toString());

    public DumbPlayer(CommanderInterface commanderInterface) {
        super(commanderInterface);
    }

    public void updateState(GameModel gameModel) {
        this.gameModel = gameModel;

        ArrayList<Planet> planetsToAttack = new ArrayList<>();
        ArrayList<StationedArmy> armiesToMove = new ArrayList<>();

        for (Map.Entry<Long, Planet> entry : this.gameModel.map.planets.entrySet()) {
            Planet planet = entry.getValue();
            if (!isPlanetMine(planet) && isPlanetAttractive(planet)) {
                planetsToAttack.add(planet);
                continue;
            }

            for (StationedArmy army : planet.stationedArmies) {
                if (isArmyUsable(army)) {
                    armiesToMove.add(army);
                }
            }
        }

        for (StationedArmy army : armiesToMove) {
            long actualSize = army.size;
            while (actualSize > gameModel.minMovableArmySize && planetsToAttack.size() > 0) {
                if (planetsToAttack.size() > 0) {
                    Planet target = getOptimalPlanet(army, planetsToAttack);
                    planetsToAttack.remove(target);
                    long targetArmy = target.stationedArmies.isEmpty() ? actualSize / 2 : target.stationedArmies.get(0).size + 15;
                    if (actualSize > targetArmy) {
                        this.sendCommand(new Command(army.planet.planetID, target.planetID, targetArmy));
                        actualSize -= targetArmy;
                    }
                }
                else break;
            }
        }
    }

    private boolean isPlanetMine(Planet planet) {
        return PlayerModel.NAME.equals(planet.owner) && planet.ownershipRatio >= 0.999d;
    }

    private boolean isPlanetAttractive(Planet planet) {
        return planet.owner != null && planet.movingArmies.stream().noneMatch(x -> x.owner.equals(PlayerModel.NAME));
    }

    private boolean isArmyUsable(StationedArmy army) {
        return army.size > this.gameModel.minMovableArmySize && army.owner.equals((PlayerModel.NAME))
            && isPlanetMine(army.planet);
    }

    private Planet getOptimalPlanet(StationedArmy army, ArrayList<Planet> planets) {
        int maxIdx = 0;

        for (int i = 1; i < planets.size(); i++) {
            if (this.gameModel.map.distanceOf(army.planet, this.gameModel.map.getPlanetByIdx(i)) < this.gameModel.map.distanceOf(army.planet, this.gameModel.map.getPlanetByIdx(maxIdx))) {
            //if (this.gameModel.getMap().getPlanetByIdx(i).radius > this.gameModel.getMap().getPlanetByIdx(maxIdx).radius) {
                maxIdx = i;
            }
        }

        return planets.get(maxIdx);
    }

    @Override
    public void initPlayer(GameDescription gameDescription, GameModel gameMap) {

    }
}
