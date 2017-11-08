
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


    private List<MovingArmy> movingArmies;
    private List<StationedArmy> stationedArmies;
    private Double ownershipRatio;
    private String owner;

    public void setState(PlanetState state) {
        this.movingArmies = state.movingArmies;
        this.stationedArmies = state.stationedArmies;
        this.ownershipRatio = state.ownershipRatio;
        this.owner = state.owner;
    }
}