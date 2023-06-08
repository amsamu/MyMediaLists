package com.amsamu.mymedialists.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.amsamu.mymedialists.database.tables.MediaList;

import java.util.List;

@Dao
public interface MediaListDao {

    // Get all queries
    @Query("SELECT * FROM media_lists")
    List<MediaList> getAll();

    @Query("SELECT * FROM media_lists ORDER BY name COLLATE NOCASE COLLATE UNICODE")
    List<MediaList> getAllOrderedByNameAsc();


    //
    @Query("SELECT * FROM media_lists WHERE id = :id LIMIT 1")
    MediaList getMediaList(int id);
    @Query("SELECT * FROM media_lists WHERE name LIKE :name LIMIT 1")
    MediaList findByName(String name);
    @Query("SELECT max(id) FROM media_lists")
    int getHighestId();



    //
    @Insert
    void insertAll(MediaList... mediaLists);

    @Update
    void update(MediaList mediaList);

    @Delete
    void delete(MediaList mediaList);


}
