package com.miatharifa.javachallenge2017.game;

import com.miatharifa.javachallenge2017.data.GameDescription;
import com.miatharifa.javachallenge2017.data.GameState;

public class GameModel {
    public void initialize(GameDescription description) {
        System.out.println("Initialized");
    }

    public void updateState(GameState gameStateUpdate) {
        System.out.println("Got update for time:" + gameStateUpdate.timeElapsed);
    }
}
