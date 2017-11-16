package com.miatharifa.javachallenge2017.game;

import com.miatharifa.javachallenge2017.models.MovingArmy;
import com.miatharifa.javachallenge2017.models.Planet;
import com.miatharifa.javachallenge2017.models.StationedArmy;
import com.miatharifa.javachallenge2017.players.DumbPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class GameMap {
    private static final Logger logger = Logger.getLogger(GameMap.class.toString());
    public final Integer mapSizeX;
    public final Integer mapSizeY;
    public final HashMap<Integer, Planet> planets;
    public List<StationedArmy> stationedArmies = new ArrayList<>();
    public List<MovingArmy> movingArmies = new ArrayList<>();

    public Map<Integer, Integer> planetIdIdxMap;
    public Map<Integer, Integer> planetIdxIdMap;
    public double[][] distanceMatrix;

    public GameMap(Integer mapSizeX, Integer mapSizeY, List<Planet> planets) {
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
                Integer planetId1 = planetIdxIdMap.get(i);
                Integer planetId2 = planetIdxIdMap.get(j);

                double distance = planets.get(planetId1).getDistance(planets.get(planetId2));
                distanceMatrix[i][j] = distance;
                distanceMatrix[j][i] = distance;
            }
        }
//        dumpDistanceMatrix();
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

    public List<Planet> getClosestPlanets(Planet reference) {
        return this.planets.values()
                .stream()
                .sorted((o1, o2) -> (int) (this.distanceOf(reference, o1) - this.distanceOf(reference, o2)))
                .collect(Collectors.toList());
    }

    public List<Planet> getClosestUnownedPlanets(Planet reference, String playerName, int max) {
        return this.planets.values()
                .stream()
                .filter(x -> !playerName.equals(x.owner) || x.ownershipRatio < 1.0)
                .sorted((o1, o2) -> (int) (this.distanceOf(reference, o1) - this.distanceOf(reference, o2)))
                .limit(max)
                .collect(Collectors.toList());
    }

    public List<Planet> getClosestOwnPlanets(Planet reference) {
        return this.planets.values()
                .stream()
                .filter(x -> PlayerModel.NAME.equals(x.owner) && x != reference)
                .sorted((o1, o2) -> (int) (this.distanceOf(reference, o1) - this.distanceOf(reference, o2)))
                .collect(Collectors.toList());
    }
}
