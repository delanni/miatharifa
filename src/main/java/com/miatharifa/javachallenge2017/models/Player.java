
package com.miatharifa.javachallenge2017.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Player {

    @SerializedName("raceID")
    @Expose
    public Integer raceID;
    @SerializedName("userName")
    @Expose
    public String userName;
    @SerializedName("userID")
    @Expose
    public String userID;

}