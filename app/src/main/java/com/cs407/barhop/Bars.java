package com.cs407.barhop;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bars")
public class Bars {
    @PrimaryKey(autoGenerate = true)
    private int barId;

    private String name;

    private String latitude;

    private String longitude;

    /* getters and setters */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
