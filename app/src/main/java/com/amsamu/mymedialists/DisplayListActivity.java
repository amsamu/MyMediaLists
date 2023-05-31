package com.amsamu.mymedialists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.amsamu.mymedialists.dao.MediaListDao;
import com.amsamu.mymedialists.data.MediaList;
import com.amsamu.mymedialists.data.Title;
import com.amsamu.mymedialists.databinding.ActivityDisplayListBinding;

import java.util.ArrayList;
import java.util.List;

public class DisplayListActivity extends AppCompatActivity {

    AppDatabase db;
    public ActivityDisplayListBinding binding;
    int listId;
    MediaListAdapter adapter = new MediaListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        binding = ActivityDisplayListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get selected list from invoking activity
        listId = getIntent().getExtras().getInt("selectedList");
        Log.d("DisplayListActivity", "Launched DisplayListActivity, selectedList:" + listId);

        setUpTopBar();
        setUpNavMenu();
        setUpFAB();
        setUpRecyclerView();
    }

    public void setUpTopBar() {
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            binding.topAppBar.getMenu().findItem(item.getItemId()).setChecked(true);
            switch (item.getItemId()) {
                case R.id.sort_by_release_date:
                    loadTitles("releaseDate");
                    break;
                case R.id.sort_by_name:
                    loadTitles("name");
                    break;
                case R.id.sort_by_status:
                    loadTitles("status");
                    break;
            }
            return true;
        });
    }

    public void setUpRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        loadTitles("name");
    }

    public void setUpNavMenu() {
        loadListsToMenu();
        binding.navigation.getMenu().findItem(listId).setCheckable(true);
        binding.navigation.getMenu().findItem(listId).setChecked(true); // Check/highlight this activity's corresponding menu item

        // Switch activities when clicking on an option from the navigation menu

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.open());

        binding.navigation.setNavigationItemSelectedListener(item -> {
            //item.setChecked(true);
            handleNavMenuItemSelected(item);
            binding.drawerLayout.close();
            return true;
        });
    }

    public void setUpFAB() {
        binding.floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EntryDetailsActivity.class);
            intent.putExtra("containerList", listId);
            startActivity(intent);
            finish();
        });
    }

    public void handleNavMenuItemSelected(MenuItem menuItem) {
        Log.d("DisplayListActivity", "itemId: " + menuItem.getItemId() + " groupId: " + menuItem.getGroupId() + " order: " + menuItem.getOrder());

        // If clicked on this activity's menu item, do nothing
        if (menuItem.getItemId() == listId) {
            return;
        }

        Intent intent = null;
        if (menuItem.getItemId() == R.id.nav_item_home) {
            intent = new Intent(this, MainActivity.class);
        } else if (menuItem.getGroupId() == R.id.nav_group_lists) {
            intent = new Intent(this, DisplayListActivity.class);
            intent.putExtra("selectedList", menuItem.getItemId());
        } else if (menuItem.getItemId() == R.id.nav_item_new_list) {
            intent = new Intent(this, ListDetailsActivity.class);
            intent.putExtra("selectedList", -1);
        }

        // Launch new activity
        if (intent != null) {
            startActivity(intent);
            if (menuItem.getItemId() != R.id.nav_item_new_list) {
                finish(); // destroy this activity so it doesn't stay in the background
            }
        }
        Log.d("DisplayListActivity", "leaving DisplayListActivity");
    }

    public void loadTitles(String fieldToOrderBy){
        switch(fieldToOrderBy){
            case "name":
                adapter.submitList(db.titleDao().getAllOrderedByName());
                break;
        }
    }


    public void loadListsToMenu() {
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 1, 0, "Films");
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 2, 0, "Series");
//        binding.navigation.getMenu().add(R.id.nav_group_lists, 3, 0, "Books");
        MediaListDao mListDao = db.mediaListDao();
        ArrayList<MediaList> mediaListArrayList = (ArrayList<MediaList>) mListDao.getAll();

        for (int i = 0; i < mediaListArrayList.size(); i++) {
            binding.navigation.getMenu().add(R.id.nav_group_lists, i, 0, mediaListArrayList.get(i).name);
            Log.d("menuItems", mediaListArrayList.get(i).name + " " + i);
        }
    }

}