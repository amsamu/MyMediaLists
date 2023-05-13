package com.amsamu.mymedialists;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.amsamu.mymedialists.dao.MediaListDao;
import com.amsamu.mymedialists.data.MediaList;
import com.amsamu.mymedialists.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Launching main activity
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpNavMenu();
    }

    public void setUpNavMenu() {
        loadListsToMenu();
        binding.navigation.getMenu().findItem(R.id.nav_item_home).setCheckable(true); // IMPORTANT: MUST BE SET TO TRUE, OTHERWISE SETCHECKED DOES NOT FULLY WORK!!
        binding.navigation.getMenu().findItem(R.id.nav_item_home).setChecked(true);

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.open());

        binding.navigation.setNavigationItemSelectedListener(item -> {
            //item.setChecked(true);
            handleNavMenuItemSelected(item);
            binding.drawerLayout.close();
            return true;
        });


        //binding.navigation.getMenu().getItem(0).setChecked(true); // Check/highlight this activity's corresponding menu item

        // Switch activities when clicking on an option from the navigation menu

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Show nav menu button
    }


    public void handleNavMenuItemSelected(MenuItem menuItem) {
        Log.d("MainActivity", "itemId: " +  menuItem.getItemId() + " groupId: " + menuItem.getGroupId() + " order: " + menuItem.getOrder());

        // If clicked on this activity's menu item, do nothing
        if (menuItem.getItemId() == R.id.nav_item_home) {
            return;
        }

        Intent intent = null;
        if (menuItem.getGroupId() == R.id.nav_group_lists) {
            intent = new Intent(this, DisplayListActivity.class);
            intent.putExtra("selectedList", menuItem.getItemId());
        } else if (menuItem.getItemId() == R.id.nav_item_new_list) {
            intent = new Intent(this, ListDetailsActivity.class);
            intent.putExtra("selectedList", -1);
        }

        // Launch new activity
        if (intent != null) {
            startActivity(intent);
            // finish(); // destroy this activity so it doesn't stay in the background
        }
        Log.d("MainActivity", "leaving MainActivity");
    }


    public void loadListsToMenu() {
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 1, 0, "Films");
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 2, 0, "Series");
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 3, 0, "Books");
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        MediaListDao mListDao = db.mediaListDao();
        ArrayList<MediaList> mediaListArrayList = (ArrayList<MediaList>) mListDao.getAll();

        for (int i = 0; i < mediaListArrayList.size(); i++) {
            binding.navigation.getMenu().add(R.id.nav_group_lists, i, 0, mediaListArrayList.get(i).name);
        }
    }

}