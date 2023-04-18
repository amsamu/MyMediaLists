package com.amsamu.mymedialists;

import android.content.Context;

import androidx.room.*;

import com.amsamu.mymedialists.dao.EntryDao;
import com.amsamu.mymedialists.dao.MediaListDao;
import com.amsamu.mymedialists.dao.TitleDao;
import com.amsamu.mymedialists.dao.TypeDao;
import com.amsamu.mymedialists.data.Entry;
import com.amsamu.mymedialists.data.MediaList;
import com.amsamu.mymedialists.data.Title;
import com.amsamu.mymedialists.data.Type;

@Database(entities = {Entry.class, MediaList.class, Title.class, Type.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract EntryDao entryDao();

    public abstract MediaListDao mediaListDao();

    public abstract TitleDao titleDao();

    public abstract TypeDao typeDao();

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
