package com.miatharifa.javachallenge2017.game;

import com.miatharifa.javachallenge2017.models.Planet;

import java.util.HashMap;
import java.util.List;

public class GameMap {
    private final Long mapSizeX;
    private final Long mapSizeY;
    public final HashMap<Long, Planet> planets;

    public GameMap(Long mapSizeX, Long mapSizeY, List<Planet> planets) {
        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;

        this.planets = new HashMap<>();
        for (Planet planet : planets) {
            this.planets.put(planet.planetID, planet);
        }
    }
}
