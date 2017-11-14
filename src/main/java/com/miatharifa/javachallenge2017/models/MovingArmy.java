
package com.miatharifa.javachallenge2017.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.miatharifa.javachallenge2017.game.XVector;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MovingArmy {

    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("size")
    @Expose
    public Integer size;
    @SerializedName("x")
    @Expose
    public Double x;
    @SerializedName("y")
    @Expose
    public Double y;

    public XVector getPosition() {
        return new XVector(this.x, this.y);
    }

    @Override
    public String toString() {
        return "MovingArmy{" +
                "owner='" + owner + '\'' +
                ", size=" + size +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

//    public static double diffArrays(List<MovingArmy> movingArmies1, List<MovingArmy> movingArmies2) {
//        double diff = 0.0;
//
//        for (int i = 0; i < movingArmies1.size() && i < movingArmies2.size(); i += 1) {
//            MovingArmy ma1 = movingArmies1.get(i);
//            MovingArmy ma2 = movingArmies2.get(i);
//            if (Objects.equals(ma1.owner, ma2.owner)) {
//                diff += Math.abs(ma1.size - ma2.size);
//                diff += Math.abs(ma1.x - ma2.x);
//                diff += Math.abs(ma1.y - ma2.y);
//            }
//        }
//        return diff;
//    }
}
