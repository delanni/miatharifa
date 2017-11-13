package com.miatharifa.javachallenge2017.game;

import com.miatharifa.javachallenge2017.models.Planet;
import com.miatharifa.javachallenge2017.players.DumbPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class GameMap {
    private static final Logger logger = Logger.getLogger(GameMap.class.toString());
    public final Long mapSizeX;
    public final Long mapSizeY;
    public final HashMap<Long, Planet> planets;

    public Map<Long, Integer> planetIdIdxMap;
    public Map<Integer, Long> planetIdxIdMap;
    public double[][] distanceMatrix;

    public GameMap(Long mapSizeX, Long mapSizeY, List<Planet> planets) {
        this.mapSizeX = mapSizeX;
        this.mapSizeY = mapSizeY;

        this.planets = new HashMap<>();

        for (Planet planet : planets) {
            this.planets.put(planet.planetID, planet);
        }
        initPlanetIdIdxMap();
        initDistanceMatrix();
    }

    private void initPlanetIdIdxMap() {
        planetIdIdxMap = new HashMap<>();
        planetIdxIdMap = new HashMap<>();
        for (Planet planet : planets.values()) {
            planetIdIdxMap.put(planet.planetID, planetIdIdxMap.size());
            planetIdxIdMap.put(planetIdIdxMap.get(planet.planetID), planet.planetID);
        }
    }

    private void initDistanceMatrix() {
        this.distanceMatrix = new double[planets.size()][planets.size()];
        for (int i = 0; i < planets.size(); i++) {
            for (int j = i + 1; j < planets.size(); j++) {
                Long planetId1 = planetIdxIdMap.get(i);
                Long planetId2 = planetIdxIdMap.get(j);

                double distance = planets.get(planetId1).getDistance(planets.get(planetId2));
                distanceMatrix[i][j] = distance;
                distanceMatrix[j][i] = distance;
            }
        }
        dumpDistanceMatrix();
    }

    private void dumpDistanceMatrix() {
        for (int i = 0; i < planets.size(); i++) {
            for (int j = 0; j < planets.size(); j++) {
                logger.info(String.format("%s -> %s: %s", i, j, distanceMatrix[i][j]));
            }
        }
    }

    public double distanceOf(Planet planet1, Planet planet2) {
        int planet1Idx = planetIdIdxMap.get(planet1.planetID);
        int planet2Idx = planetIdIdxMap.get(planet2.planetID);
        return distanceMatrix[planet1Idx][planet2Idx];
    }

    public Planet getPlanetByIdx(int idx) {
        return planets.get(planetIdxIdMap.get(idx));
    }
}
