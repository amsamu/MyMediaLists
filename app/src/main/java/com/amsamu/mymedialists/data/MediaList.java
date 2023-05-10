package com.amsamu.mymedialists.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MediaList {

    @PrimaryKey
    @NonNull
    public int id;

    public String name;

    public MediaList(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
