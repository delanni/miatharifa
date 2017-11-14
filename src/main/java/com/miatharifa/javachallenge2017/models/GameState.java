package com.miatharifa.javachallenge2017.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameState {

    @SerializedName("timeElapsed")
    @Expose
    public Integer timeElapsed;
    @SerializedName("planetStates")
    @Expose
    public List<PlanetState> planetStates = new ArrayList<>();
    @SerializedName("remainingPlayers")
    @Expose
    public Integer remainingPlayers;
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

    public Map<Integer, PlanetState> getPlanetStatesMap() {
        Map<Integer, PlanetState> map = planetStates.stream().collect(Collectors.toMap(item -> item.planetID, item -> item));
        return map;
    }
}
