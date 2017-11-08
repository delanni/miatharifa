
package com.miatharifa.javachallenge2017.data;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Command {
    @SerializedName("moveFrom")
    @Expose
    public Long moveFrom;
    @SerializedName("moveTo")
    @Expose
    public Long moveTo;
    @SerializedName("armySize")
    @Expose
    public Long armySize;

    @Override
    public String toString() {
        return "Command{" +
                "moveFrom=" + moveFrom +
                ", moveTo=" + moveTo +
                ", armySize=" + armySize +
                '}';
    }

    public Command(long from, long to, long armySize) {
        this.moveFrom = from;
        this.moveTo = to;
        this.armySize = armySize;
    }
}
