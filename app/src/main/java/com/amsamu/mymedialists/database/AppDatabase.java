package com.amsamu.mymedialists.database;

import android.content.Context;

import androidx.room.*;

import com.amsamu.mymedialists.database.dao.EntryDao;
import com.amsamu.mymedialists.database.dao.MediaListDao;
import com.amsamu.mymedialists.database.tables.MediaList;
import com.amsamu.mymedialists.database.tables.Entry;

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
