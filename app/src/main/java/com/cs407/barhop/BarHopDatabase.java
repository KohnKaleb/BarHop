package com.cs407.barhop;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import android.content.Context;

import java.util.concurrent.Executors;

@Database(entities = {Users.class, Bars.class}, version = 1, exportSchema = false)
public abstract class BarHopDatabase extends RoomDatabase {
    public abstract UsersDao usersDao();
    public abstract BarsDao barsDao();
    private static volatile BarHopDatabase INSTANCE;

    public static BarHopDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BarHopDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context,
                                    BarHopDatabase.class, "bar_hop_database")
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    // Perform initial data insertion here
                    Executors.newSingleThreadScheduledExecutor().execute(() -> {
                        // Insert initial user data
                        UsersDao usersDao = INSTANCE.usersDao();
                        usersDao.insert(new Users(/*data for users insertion*/));

                        // Insert initial bar data
                        BarsDao barsDao = INSTANCE.barsDao();
                        barsDao.insert(new Bars(/*data for bars insertion*/));
                    });
                }
            };
}
