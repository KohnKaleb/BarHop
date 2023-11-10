package com.cs407.barhop;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Users.class, Bars.class}, version = 1, exportSchema = false)
public abstract class BarHopDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();
    public abstract BarsDao barsDao();
}
