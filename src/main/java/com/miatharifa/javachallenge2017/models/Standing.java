
package com.miatharifa.javachallenge2017.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Standing {

    @SerializedName("score")
    @Expose
    public Integer score;
    @SerializedName("strength")
    @Expose
    public Integer strength;
    @SerializedName("userID")
    @Expose
    public String userID;

    @Override
    public String toString() {
        return "Standing{" +
                "score=" + score +
                ", strength=" + strength +
                ", userID='" + userID + '\'' +
                '}';
    }
}
