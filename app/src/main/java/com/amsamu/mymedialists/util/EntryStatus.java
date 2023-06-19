package com.amsamu.mymedialists.util;

import android.content.Context;
import android.util.Log;

import com.amsamu.mymedialists.R;

public enum EntryStatus {
    ONGOING, COMPLETED, PLANNED, DROPPED, ON_HOLD;

    public CharSequence getString(Context context) {
        Log.d("EntryStatus", context.getString(getStringId(context)));
        return context.getString(getStringId(context));
    }

    public int getStringId(Context context) {
        switch (this) {
            case ONGOING:
                return R.string.ongoing;
            case COMPLETED:
                return R.string.completed;
            case PLANNED:
                return R.string.planned;
            case DROPPED:
                return R.string.dropped;
            case ON_HOLD:
                return R.string.onhold;
            default:
                return 0;
        }
    }

    public static EntryStatus fromString(Context context, CharSequence string) {
        if (string.equals(context.getString(R.string.ongoing))) {
            return ONGOING;
        }
        if (string.equals(context.getString(R.string.completed))) {
            return COMPLETED;
        }
        if (string.equals(context.getString(R.string.planned))) {
            return PLANNED;
        }
        if (string.equals(context.getString(R.string.dropped))) {
            return DROPPED;
        }
        if (string.equals(context.getString(R.string.onhold))) {
            return ON_HOLD;
        }
        return null;
    }

}
