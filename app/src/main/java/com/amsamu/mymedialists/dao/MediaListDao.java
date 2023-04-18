package com.amsamu.mymedialists.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.amsamu.mymedialists.data.MediaList;

import java.util.List;

@Dao
public interface MediaListDao {

    @Query("SELECT * FROM MediaList")
    List<MediaList> getAll();

    @Query("SELECT * FROM MediaList WHERE name LIKE :name LIMIT 1")
    MediaList findByName(String name);

    @Insert
    void insertAll(MediaList... mediaLists);

    @Delete
    void delete(MediaList mediaList);

}
