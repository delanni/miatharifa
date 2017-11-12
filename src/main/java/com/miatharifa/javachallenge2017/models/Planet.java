
package com.miatharifa.javachallenge2017.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Planet {
    @SerializedName("x")
    @Expose
    public Long x;
    @SerializedName("y")
    @Expose
    public Long y;
    @SerializedName("planetID")
    @Expose
    public Long planetID;
    @SerializedName("radius")
    @Expose
    public Long radius;


    public List<MovingArmy> movingArmies;
    public List<StationedArmy> stationedArmies;
    public Double ownershipRatio;
    public String owner;

    public void setState(PlanetState state) {
        this.movingArmies = state.movingArmies;
        this.stationedArmies = state.stationedArmies;
        for (StationedArmy army : stationedArmies) army.planet = this;
        this.ownershipRatio = state.ownershipRatio;
        this.owner = state.owner;
    }


    public double getDistance(Planet other) {
        return Math.sqrt(Math.pow(other.x - this.x, 2 ) + Math.pow(other.y - this.y, 2));
    }
}
