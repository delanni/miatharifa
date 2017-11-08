
package com.miatharifa.javachallenge2017.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Planet {
    @SerializedName("x")
    @Expose
    public Long x;
    @SerializedName("y")
    @Expose
    public Long y;
    @SerializedName("planetID")
    @Expose
    public Long planetID;
    @SerializedName("radius")
    @Expose
    public Long radius;

}