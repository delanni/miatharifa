
package com.miatharifa.javachallenge2017.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GameDescription {

    @SerializedName("mapSizeY")
    @Expose
    public Long mapSizeY;
    @SerializedName("unitCreateSpeed")
    @Expose
    public Double unitCreateSpeed;
    @SerializedName("battleSpeed")
    @Expose
    public Double battleSpeed;
    @SerializedName("mapSizeX")
    @Expose
    public Long mapSizeX;
    @SerializedName("players")
    @Expose
    public List<Player> players = null;
    @SerializedName("commandSchedule")
    @Expose
    public Long commandSchedule;
    @SerializedName("broadcastSchedule")
    @Expose
    public Long broadcastSchedule;
    @SerializedName("movementSpeed")
    @Expose
    public Double movementSpeed;
    @SerializedName("captureSpeed")
    @Expose
    public Double captureSpeed;
    @SerializedName("planetExponent")
    @Expose
    public Double planetExponent;
    @SerializedName("planets")
    @Expose
    public List<Planet> planets = null;
    @SerializedName("internalSchedule")
    @Expose
    public Long internalSchedule;
    @SerializedName("battleExponent")
    @Expose
    public Double battleExponent;
    @SerializedName("minMovableArmySize")
    @Expose
    public Long minMovableArmySize;
    @SerializedName("gameLength")
    @Expose
    public Long gameLength;

}