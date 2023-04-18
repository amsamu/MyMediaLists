package com.amsamu.mymedialists.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Title {

    @PrimaryKey
    @NonNull
    public int id;

    public String name;

    public String author;

    public String publisher;

    public String genres;

    public String coverImage;

    public long releaseDate;

}
