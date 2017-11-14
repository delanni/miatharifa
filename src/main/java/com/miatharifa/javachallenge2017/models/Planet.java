
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

    public void setState(PlanetState state) {
        this.movingArmies = state.movingArmies;
        this.stationedArmies = state.stationedArmies;
        for (StationedArmy army : this.stationedArmies) army.planet = this;
        for (MovingArmy army : this.movingArmies) army.targetPlanet = this;
        this.ownershipRatio = state.ownershipRatio;
        this.owner = state.owner;
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
        this.stationedArmies.forEach(p -> armies.put(p.owner, armies.containsKey(p.owner) ? armies.get(p.owner) + p.size.intValue() : p.size.intValue()));
        this.movingArmies.forEach(p -> armies.put(p.owner, armies.containsKey(p.owner) ? armies.get(p.owner) + p.size : p.size));

        Optional<Map.Entry<String, Integer>> pForce = armies.entrySet().stream().sorted(Comparator.comparingInt(Map.Entry::getValue)).findFirst();
        return pForce.map(Map.Entry::getKey).orElse("");
    }

    public Optional<StationedArmy> getArmiesFor(String name) {
        return this.stationedArmies.stream().filter(x->x.owner.equals(name)).findFirst();
    }
}
