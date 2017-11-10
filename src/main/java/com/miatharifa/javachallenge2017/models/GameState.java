package com.miatharifa.javachallenge2017.models;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (PlanetState planetState : planetStates) {
            sb.append(planetState);
            sb.append(";");
        }
        sb.append("!");
        sb.append(gameStatus);
        sb.append("!");
        for (Standing standing : standings) {
            sb.append(standing);
            sb.append(";");
        }
        return sb.toString();
    }

}
