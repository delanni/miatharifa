package com.miatharifa.javachallenge2017.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameState {

    @SerializedName("timeElapsed")
    @Expose
    public Long timeElapsed;
    @SerializedName("planetStates")
    @Expose
    public List<PlanetState> planetStates = null;
    @SerializedName("remainingPlayers")
    @Expose
    public Long remainingPlayers;
    @SerializedName("gameStatus")
    @Expose
    public String gameStatus;
    @SerializedName("standings")
    @Expose
    public List<Standing> standings = null;

}
