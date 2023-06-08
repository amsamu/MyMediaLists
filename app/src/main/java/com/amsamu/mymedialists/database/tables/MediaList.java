package com.amsamu.mymedialists.database.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.amsamu.mymedialists.util.EntryStatus;
import com.amsamu.mymedialists.util.SortingField;

@Entity(tableName = "media_lists")
public class MediaList {

    @PrimaryKey
    @NonNull
    public int id;

    @NonNull
    public String name;

    @NonNull
    public SortingField sortField;

    @NonNull
    public boolean sortAscending;

    public MediaList(int id) {
        this.id = id;
        this.name = name;
        this.sortField = SortingField.STATUS;
        this.sortAscending = true;
    }
}
