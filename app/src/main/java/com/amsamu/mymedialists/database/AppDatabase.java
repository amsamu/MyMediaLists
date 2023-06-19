package com.amsamu.mymedialists.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.amsamu.mymedialists.database.dao.EntryDao;
import com.amsamu.mymedialists.database.dao.MediaListDao;
import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.database.tables.MediaList;

@Database(entities = {Entry.class, MediaList.class}, version = 11, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract MediaListDao mediaListDao();

    public abstract EntryDao entryDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "mymedialists_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries().build();
                }
            }
        }
        return INSTANCE;
    }

}
