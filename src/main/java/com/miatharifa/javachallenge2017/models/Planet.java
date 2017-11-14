
package com.miatharifa.javachallenge2017.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.miatharifa.javachallenge2017.game.XVector;

import java.util.*;

public class Planet {
    @SerializedName("x")
    @Expose
    public Integer x;
    @SerializedName("y")
    @Expose
    public Integer y;
    @SerializedName("planetID")
    @Expose
    public Integer planetID;
    @SerializedName("radius")
    @Expose
    public Integer radius;


    public List<MovingArmy> movingArmies = new ArrayList<>();
    public List<StationedArmy> stationedArmies = new ArrayList<>();
    public Double ownershipRatio = 0.0;
    public String owner;

    public double setState(PlanetState state) {
        double diff = 0.0;
//        diff += MovingArmy.diffArrays(this.movingArmies, state.movingArmies);
//        diff += StationedArmy.diffArrays(this.stationedArmies, state.stationedArmies);
//        diff += Math.abs(this.ownershipRatio - state.ownershipRatio);
//        diff += !Objects.equals(owner, state.owner) ? 1 : 0;

        this.movingArmies = state.movingArmies;
        this.stationedArmies = state.stationedArmies;
        for (StationedArmy army : stationedArmies) army.planet = this;
        this.ownershipRatio = state.ownershipRatio;
        this.owner = state.owner;
        return diff;
    }

    public int getTotalArmyCount() {
        int sum = 0;
        for (StationedArmy stationedArmy : this.stationedArmies) {
            sum += stationedArmy.size;
        }
        return sum;
    }

    public int getEnemyCountFor(String owner) {
        int sum = 0;
        for (StationedArmy stationedArmy : this.stationedArmies) {
            if (!Objects.equals(stationedArmy.owner, owner)) {
                sum += stationedArmy.size;
            }
        }
        return sum;
    }

    public double getDistance(Planet other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2) + Math.pow(other.y - this.y, 2));
    }

    public XVector getPosition() {
        return new XVector(this.x, this.y);
    }

    public String getPredominantForce() {
        HashMap<String, Integer> armies = new HashMap<>();
        this.stationedArmies.forEach(p -> armies.put(p.owner, armies.containsKey(p.owner) ? armies.get(p.owner) + p.size : p.size));
        this.movingArmies.forEach(p -> armies.put(p.owner, armies.containsKey(p.owner) ? armies.get(p.owner) + p.size : p.size));

        Optional<Map.Entry<String, Integer>> pForce = armies.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).findFirst();
        return pForce.map(Map.Entry::getKey).orElse("");
    }
}
