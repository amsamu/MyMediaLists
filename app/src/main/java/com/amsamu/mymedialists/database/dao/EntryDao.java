package com.amsamu.mymedialists.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.util.EntryStatus;

import java.util.List;

@Dao
public interface EntryDao {
    @Query("SELECT * FROM entries")
    List<Entry> getAll();

    @Query("SELECT * FROM entries WHERE status LIKE :status")
    List<Entry> getAllOnStatus(EntryStatus status);


    // Ordered by NAME
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY name COLLATE NOCASE COLLATE UNICODE")
    List<Entry> getOrderedByNameAsc(int listId);

    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY name COLLATE NOCASE COLLATE UNICODE DESC")
    List<Entry> getOrderedByNameDesc(int listId);



    // Ordered by AUTHOR
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY author COLLATE NOCASE COLLATE UNICODE")
    List<Entry> getOrderedByAuthorAsc(int listId);

    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY author COLLATE NOCASE COLLATE UNICODE DESC")
    List<Entry> getOrderedByAuthorDesc(int listId);



    // Ordered by RELEASE YEAR
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY releaseYear")
    List<Entry> getOrderedByReleaseYearAsc(int listId);

    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY releaseYear DESC")
    List<Entry> getOrderedByReleaseYearDesc(int listId);


    // Ordered by START DATE
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY startDate")
    List<Entry> getOrderedByStartDateAsc(int listId);
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY startDate DESC")
    List<Entry> getOrderedByStartDateDesc(int listId);




    // Ordered by FINISH DATE
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY finishDate")
    List<Entry> getOrderedByFinishDateAsc(int listId);
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY finishDate DESC")
    List<Entry> getOrderedByFinishDateDesc(int listId);



    // Ordered by STATUS
    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY " +
            "CASE status " +
            "WHEN 'ONGOING' THEN 0 " +
            "WHEN 'COMPLETED' THEN 1 " +
            "WHEN 'ON_HOLD' THEN 2 " +
            "WHEN 'DROPPED' THEN 3 " +
            "WHEN 'PLANNED' THEN 4 " +
            "END")
    List<Entry> getOrderedByStatusAsc(int listId);

    @Query("SELECT * FROM entries WHERE listId = :listId ORDER BY " +
            "CASE status " +
            "WHEN 'ONGOING' THEN 0 " +
            "WHEN 'COMPLETED' THEN 1 " +
            "WHEN 'ON_HOLD' THEN 2 " +
            "WHEN 'DROPPED' THEN 3 " +
            "WHEN 'PLANNED' THEN 4 " +
            "END DESC")
    List<Entry> getOrderedByStatusDesc(int listId);




    // Get single entry
    @Query("SELECT * FROM entries WHERE id = :id LIMIT 1")
    Entry getEntry(int id);

    @Query("SELECT * FROM entries WHERE name LIKE :name LIMIT 1")
    Entry findByName(String name);

    //
    @Query("SELECT max(id) FROM entries")
    int getHighestId();



    //
    @Insert
    void insertAll(Entry... entries);

    @Update
    void update(Entry entry);

    @Delete
    void delete(Entry Entry);

    @Delete
    void deleteAll(Entry... entries);

    @Query("DELETE FROM entries WHERE listId = :listId")
    void deleteAllInList(int listId);

    @Query("DELETE FROM entries")
    void deleteEverything();

}
