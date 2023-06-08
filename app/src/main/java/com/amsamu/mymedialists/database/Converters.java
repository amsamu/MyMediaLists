package com.amsamu.mymedialists.database;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date fromEpoch(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToEpoch(Date date) {
        return date == null ? null : date.getTime();
    }

}
