package com.cs407.barhop;

import androidx.room.Database;

@Database(entities = {Users.class, Bars.class}, version = 1, exportSchema = false)
public abstract class BarHopDatabase {
    public abstract UsersDao usersDao();
    public abstract BarsDao barsDao();
}
