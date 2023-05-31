package com.amsamu.mymedialists.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.amsamu.mymedialists.data.Title;

import java.util.List;

@Dao
public interface TitleDao {
    @Query("SELECT * FROM Title")
    List<Title> getAll();

    @Query("SELECT * FROM Title ORDER BY name COLLATE NOCASE COLLATE UNICODE")
    List<Title> getAllOrderedByName();

    @Query("SELECT * FROM Title WHERE name LIKE :name LIMIT 1")
    Title findByName(String name);

    @Insert
    void insertAll(Title... mediaLists);

    @Delete
    void delete(Title Title);

    @Query("SELECT max(id) FROM Title")
    int getHighestId();
}
