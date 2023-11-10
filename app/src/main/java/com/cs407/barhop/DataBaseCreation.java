package com.cs407.barhop;

import android.app.Application;

import androidx.room.Room;

public class DataBaseCreation extends Application {
    public static BarHopDatabase barHopDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        barHopDatabase = Room.databaseBuilder(
                getApplicationContext(),
                BarHopDatabase.class, "Bar_Hop")
                .build();
    }
}
