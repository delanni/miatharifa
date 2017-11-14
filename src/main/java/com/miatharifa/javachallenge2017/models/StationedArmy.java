
package com.miatharifa.javachallenge2017.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class StationedArmy {

    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("size")
    @Expose
    public Integer size;

    public Planet planet;

    public StationedArmy(String ownerId, Integer size, Planet planet) {
        this.owner = ownerId;
        this.size = size;
        this.planet = planet;
    }

//    public static double diffArrays(List<StationedArmy> stationedArmies1, List<StationedArmy> stationedArmies2) {
//        double diff = 0;
//        for (StationedArmy sa1 : stationedArmies1) {
//            for (StationedArmy sa2 : stationedArmies2) {
//                if (sa1.planet == sa2.planet && Objects.equals(sa1.owner, sa2.owner)){
//                    diff += Math.abs(sa1.size - sa2.size);
//                }
//            }
//        }
//        return diff;
//    }
}
