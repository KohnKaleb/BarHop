package com.cs407.barhop;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Users.class, Bars.class}, version = 2, exportSchema = false)
public abstract class BarHopDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();
    public abstract BarsDao barsDao();

    private static BarHopDatabase INSTANCE;

    public static BarHopDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            synchronized (BarHopDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BarHopDatabase.class, "bar_hop_database")
                            .allowMainThreadQueries().fallbackToDestructiveMigration().build();
                }
            }
        }
        return INSTANCE;
    }
}
