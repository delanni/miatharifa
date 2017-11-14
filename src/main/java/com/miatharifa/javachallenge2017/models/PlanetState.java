
package com.miatharifa.javachallenge2017.models;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PlanetState {

    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("ownershipRatio")
    @Expose
    public Double ownershipRatio;
    @SerializedName("stationedArmies")
    @Expose
    public List<StationedArmy> stationedArmies = new ArrayList<>();
    @SerializedName("movingArmies")
    @Expose
    public List<MovingArmy> movingArmies = new ArrayList<>();
    @SerializedName("planetID")
    @Expose
    public Integer planetID;

    @Override
    public String toString() {
        return String.format("{owner=%s, ownershipratio=%s, id=%s}", owner, ownershipRatio, planetID);
    }
}
