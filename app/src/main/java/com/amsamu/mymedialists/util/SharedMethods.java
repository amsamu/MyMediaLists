package com.amsamu.mymedialists.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;

import com.amsamu.mymedialists.database.AppDatabase;
import com.amsamu.mymedialists.R;
import com.amsamu.mymedialists.database.dao.MediaListDao;
import com.amsamu.mymedialists.database.tables.MediaList;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SharedMethods {

    public static DateTimeFormatter simpleFormatter = DateTimeFormatter.ofPattern("uuuuMMdd-HHmmss");

    public static void loadListsToMenu(AppDatabase db, Menu menu) {
        MediaListDao mListDao = db.mediaListDao();
        ArrayList<MediaList> mediaListArrayList = (ArrayList<MediaList>) mListDao.getAllOrderedByNameAsc();
        Menu listsSubmenu = menu.findItem(R.id.nav_lists).getSubMenu();
        for (MediaList list : mediaListArrayList) {
            listsSubmenu.add(R.id.nav_group_lists, list.id, 0, list.name);
            listsSubmenu.findItem(list.id).setIcon(R.drawable.baseline_library_books_24);
            Log.d("menuItems", list.name + " " + list.id);
        }
    }

    public static String formatDate(Context context, Date date) {
        return date == null ? null : DateFormat.getDateFormat(context).format(date);
    }

    public static void showInfoDialog(Context context, CharSequence message){
        new MaterialAlertDialogBuilder(context)
                .setMessage(message)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                })
                .show();
    }

    public static void showInfoDialog(Context context, int messageId){
        showInfoDialog(context, context.getString(messageId));
    }

}
