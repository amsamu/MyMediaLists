package com.amsamu.mymedialists.database.tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.amsamu.mymedialists.util.EntryStatus;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = "entries")
public class Entry {

    @PrimaryKey
    @NonNull
    public int id;

    @NonNull
    public int listId;

    @NonNull
    public String name;

    @NonNull
    public EntryStatus status;

    public String author;

    public String publisher;

    public Integer releaseYear;

    public String coverImage;

    public Date startDate;

    public Date finishDate;

    public int progress;

    public int maxProgress;

    public String notes;


    // Constructor
    public Entry(int id, int listId) {
        this.id = id;
        this.listId = listId;
    }


    // Methods
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return id == entry.id && releaseYear == entry.releaseYear && listId == entry.listId && name.equals(entry.name) && Objects.equals(author, entry.author) && Objects.equals(publisher, entry.publisher) && Objects.equals(coverImage, entry.coverImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, publisher, coverImage, releaseYear, listId);
    }
}

