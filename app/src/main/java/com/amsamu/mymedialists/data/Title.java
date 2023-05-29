package com.amsamu.mymedialists.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

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

    public Title(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return id == title.id && releaseDate == title.releaseDate && name.equals(title.name) && author.equals(title.author) && publisher.equals(title.publisher) && genres.equals(title.genres) && coverImage.equals(title.coverImage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, author, publisher, genres, coverImage, releaseDate);
    }
}

