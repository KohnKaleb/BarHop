package com.cs407.barhop;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Users.class, Bars.class, UsersFriends.class, UsersFavoriteBars.class, UsersHistory.class}, version = 6, exportSchema = false)
public abstract class BarHopDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();
    public abstract BarsDao barsDao();

    public abstract UsersFriendsDao friendsDao();

    public abstract UsersFavoriteBarsDao favoritesDao();
    public abstract UsersHistoryDao historyDao();

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
