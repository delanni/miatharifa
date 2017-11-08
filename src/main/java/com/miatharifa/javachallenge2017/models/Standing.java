
package com.miatharifa.javachallenge2017.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Standing {

    @SerializedName("score")
    @Expose
    public Long score;
    @SerializedName("strength")
    @Expose
    public Long strength;
    @SerializedName("userID")
    @Expose
    public String userID;


}
