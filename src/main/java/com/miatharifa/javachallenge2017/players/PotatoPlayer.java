package com.miatharifa.javachallenge2017.players;

import com.miatharifa.javachallenge2017.game.GameModel;
import com.miatharifa.javachallenge2017.game.PlayerModel;
import com.miatharifa.javachallenge2017.models.*;

import java.security.cert.CollectionCertStoreParameters;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PotatoPlayer extends AbstractPlayer {
    private static final Logger logger = Logger.getLogger(PotatoPlayer.class.toString());

    public PotatoPlayer(CommanderInterface commanderInterface) {
        super(commanderInterface);
    }

    @Override
    public void updateStateRoundStart(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void updateStateRoundEnd(GameModel gameModel) {
        System.out.println("Round end");
        this.gameModel = gameModel;
        List<StationedArmy> armies = this.gameModel.map.stationedArmies.stream().filter(x -> x.owner.equals(PlayerModel.NAME) && x.size >= 5).collect(Collectors.toList());
        if (armies.size() == 0) return;
        StationedArmy army = armies.get((int) (Math.random() * armies.size()));
        List<Planet> emptyPlanets = this.gameModel.map.planets.values().stream().filter(x -> !PlayerModel.NAME.equals(x.owner)).collect(Collectors.toList());
        Planet target = emptyPlanets.get((int) (Math.random() * emptyPlanets.size()));
        if (army != null && target != null) {
            this.sendTroops(army.planet, target, army.size.intValue());
        }
    }

    @Override
    public void initPlayer(GameDescription gameDescription, GameModel gameMap) {

    }
}
