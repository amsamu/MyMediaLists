package com.amsamu.mymedialists.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Entry {

    @PrimaryKey
    @NonNull
    public String name;

    public long startDate;

    public long endDate;

    public int status;

    public int progress;

    public int maxProgress;

    public String notes;

    public int titleId;

    public int mediaListId;

}
