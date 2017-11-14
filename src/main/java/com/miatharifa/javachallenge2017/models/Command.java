
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

    @Override
    public String toString() {
        return "Command{" +
                "moveFrom=" + moveFrom +
                ", moveTo=" + moveTo +
                ", armySize=" + armySize +
                '}';
    }

    public Command(int from, int to, int armySize) {
        this.moveFrom = from;
        this.moveTo = to;
        this.armySize = armySize;
    }
}
