
package com.miatharifa.javachallenge2017.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StationedArmy {

    @SerializedName("owner")
    @Expose
    public String owner;
    @SerializedName("size")
    @Expose
    public Long size;

}
