package com.amsamu.mymedialists.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.amsamu.mymedialists.data.Entry;

import java.util.List;

@Dao
public interface EntryDao {

    @Query("SELECT * FROM Entry")
    List<Entry> getAll();

    @Query("SELECT * FROM Entry WHERE name LIKE :name LIMIT 1")
    Entry findByName(String name);

    @Insert
    void insertAll(Entry... entries);

    @Delete
    void delete(Entry entry);

}
