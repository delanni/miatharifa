
package com.miatharifa.javachallenge2017.data;
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
    public List<StationedArmy> stationedArmies = null;
    @SerializedName("movingArmies")
    @Expose
    public List<MovingArmy> movingArmies = null;
    @SerializedName("planetID")
    @Expose
    public Long planetID;


}
