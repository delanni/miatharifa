
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

    public enum CommandLabel {
        ESCAPE,
        DEFEND,
        INTERCEPT,
        CAPTURE,
        CONQUER,
        OTHER,
    }

    public transient CommandLabel label;
    public transient String reason;

    public Command(StationedArmy army, Planet planet, int size) {
        this.moveFrom = army.planet.planetID;
        this.moveTo = planet.planetID;
        this.armySize = size;
        this.label = CommandLabel.OTHER;
    }

    public Command(int from, int to, int armySize) {
        this.moveFrom = from;
        this.moveTo = to;
        this.armySize = armySize;
        this.label = CommandLabel.OTHER;
    }

    public Command(StationedArmy army, Planet planet, double size) {
        this.moveFrom = army.planet.planetID;
        this.moveTo = planet.planetID;
        this.armySize = (int)size;
        this.label = CommandLabel.OTHER;
    }

    public Command of(CommandLabel label){
        return this.of(label, "");
    }

    public Command of(CommandLabel label, String reason){
        this.label = label;
        this.reason = reason;
        return this;
    }

    @Override
    public String toString() {
        return this.label.name() + " [" + this.armySize + " " + this.moveFrom + " -> " + this.moveTo + ", " + this.reason + "] ";
    }

}
