
package com.miatharifa.javachallenge2017.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Command {
    @SerializedName("moveFrom")
    @Expose
    public Integer moveFrom;
    @SerializedName("moveTo")
    @Expose
    public Integer moveTo;
    @SerializedName("armySize")
    @Expose
    public Integer armySize;

    public Command(StationedArmy army, Planet planet, int size) {
        this.moveFrom = army.planet.planetID;
        this.moveTo = planet.planetID;
        this.armySize = size;
    }
    public Command(StationedArmy army, Planet planet, double size) {
        this.moveFrom = army.planet.planetID;
        this.moveTo = planet.planetID;
        this.armySize = (int)size;
    }

    @Override
    public String toString() {
        return this.armySize + " [" + this.moveFrom + " -> " + this.moveTo + "]";
    }

    public Command(int from, int to, int armySize) {
        this.moveFrom = from;
        this.moveTo = to;
        this.armySize = armySize;
    }
}
