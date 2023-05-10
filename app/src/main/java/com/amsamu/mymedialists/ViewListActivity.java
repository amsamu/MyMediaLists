package com.amsamu.mymedialists;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.amsamu.mymedialists.dao.MediaListDao;
import com.amsamu.mymedialists.data.MediaList;
import com.amsamu.mymedialists.databinding.ActivityViewListBinding;

import java.util.ArrayList;

public class ViewListActivity extends AppCompatActivity {

    public ActivityViewListBinding binding;
    int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get selected list from invoking activity
        listId = getIntent().getExtras().getInt("selectedList")+1;
        Log.d("ViewListActivity", "Launched ViewListActivity, selectedList:" + listId);

        setUpNavMenu();
    }

    public void setUpNavMenu() {

        loadListsToMenu();
        binding.navigation.getMenu().getItem(listId).setChecked(true); // Check/highlight this activity's corresponding menu item

        // Switch activities when clicking on an option from the navigation menu

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.open());

        binding.navigation.setNavigationItemSelectedListener(item -> {
            //item.setChecked(true);
            handleNavMenuItemSelected(item);
            binding.drawerLayout.close();
            return true;
        });
    }

    public void handleNavMenuItemSelected(MenuItem menuItem) {
        Log.d("ViewListActivity", "itemId: " + menuItem.getItemId() + " groupId: " + menuItem.getGroupId() + " order: " + menuItem.getOrder());

        // If clicked on this activity's menu item, do nothing
        if (menuItem.getItemId() == listId) {
            return;
        }

        Intent intent = null;
        if (menuItem.getItemId() == R.id.nav_item_home) {
            intent = new Intent(this, MainActivity.class);
        } else if (menuItem.getGroupId() == R.id.nav_group_lists) {
            intent = new Intent(this, ViewListActivity.class);
            intent.putExtra("selectedList", menuItem.getItemId());
        } else if (menuItem.getItemId() == R.id.nav_item_new_list) {
            intent = new Intent(this, ListDetailsActivity.class);
            intent.putExtra("selectedList", -1);
        }

        // Launch new activity
        if (intent != null) {
            startActivity(intent);
            if(menuItem.getItemId() != R.id.nav_item_new_list) {
                finish(); // destroy this activity so it doesn't stay in the background
            }
        }
        Log.d("ViewListActivity", "leaving ViewListActivity");
    }


    public void loadListsToMenu() {
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 1, 0, "Films");
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 2, 0, "Series");
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 3, 0, "Books");
        AppDatabase bd = AppDatabase.getDatabase(getApplicationContext());
        MediaListDao mListDao = bd.mediaListDao();
        ArrayList<MediaList> mediaListArrayList = (ArrayList<MediaList>) mListDao.getAll();

        for (int i = 0; i < mediaListArrayList.size(); i++) {
            binding.navigation.getMenu().add(R.id.nav_group_lists, i, 0, mediaListArrayList.get(i).name);
        }
    }

}