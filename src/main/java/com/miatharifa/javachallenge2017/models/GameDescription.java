
package com.miatharifa.javachallenge2017.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class GameDescription {

    @SerializedName("mapSizeY")
    @Expose
    public Integer mapSizeY;
    @SerializedName("unitCreateSpeed")
    @Expose
    public Double unitCreateSpeed;
    @SerializedName("battleSpeed")
    @Expose
    public Double battleSpeed;
    @SerializedName("mapSizeX")
    @Expose
    public Integer mapSizeX;
    @SerializedName("players")
    @Expose
    public List<Player> players = null;
    @SerializedName("commandSchedule")
    @Expose
    public Integer commandSchedule;
    @SerializedName("broadcastSchedule")
    @Expose
    public Integer broadcastSchedule;
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
    public Integer internalSchedule;
    @SerializedName("battleExponent")
    @Expose
    public Double battleExponent;
    @SerializedName("minMovableArmySize")
    @Expose
    public Integer minMovableArmySize;
    @SerializedName("gameLength")
    @Expose
    public Integer gameLength;

}