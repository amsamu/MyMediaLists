package com.amsamu.mymedialists.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Type {

    @PrimaryKey
    @NonNull
    public String name;

    public String progressUnit;

}
