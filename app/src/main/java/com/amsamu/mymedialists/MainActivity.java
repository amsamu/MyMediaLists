package com.amsamu.mymedialists;

import static com.amsamu.mymedialists.util.SharedMethods.loadListsToMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.amsamu.mymedialists.adapters.CarouselAdapter;
import com.amsamu.mymedialists.database.AppDatabase;
import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.databinding.ActivityMainBinding;
import com.amsamu.mymedialists.util.EntryStatus;
import com.google.android.material.carousel.CarouselLayoutManager;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    AppDatabase db;
    public ActivityMainBinding binding;
    CarouselAdapter adapter = new CarouselAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Launching main activity
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpNavMenu();
        setUpRecyclerView();

        logCurrentAppFiles();
        cleanTmpDir();
        logCurrentAppFiles();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        binding.navigation.getMenu().findItem(R.id.nav_lists).getSubMenu().removeGroup(R.id.nav_group_lists);
        setUpNavMenu();
        setUpRecyclerView();
    }

    public void setUpNavMenu() {
        loadListsToMenu(db, binding.navigation.getMenu());
        binding.navigation.getMenu().findItem(R.id.nav_item_home).setCheckable(true); // IMPORTANT: MUST BE SET TO TRUE, OTHERWISE SETCHECKED DOES NOT FULLY WORK!!
        binding.navigation.getMenu().findItem(R.id.nav_item_home).setChecked(true);

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.open());

        binding.navigation.setNavigationItemSelectedListener(item -> {
            handleNavMenuItemSelected(item);
            binding.drawerLayout.close();
            return true;
        });
    }


    public void handleNavMenuItemSelected(MenuItem menuItem) {
        Log.d("MainActivity", "itemId: " + menuItem.getItemId() + " groupId: " + menuItem.getGroupId() + " order: " + menuItem.getOrder());

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
            intent.putExtra("list", -1);
            intent.putExtra("prevActivityIsHome", true);
        } else if (menuItem.getItemId() == R.id.nav_item_options) {
            intent = new Intent(this, OptionsActivity.class);
        } else if (menuItem.getItemId() == R.id.nav_item_about) {
            intent = new Intent(this, AboutActivity.class);
        }

        // Launch new activity
        if (intent != null) {
            startActivity(intent);
        }
        Log.d("MainActivity", "leaving MainActivity");
    }

    public void setUpRecyclerView() {
        binding.recyclerView.setLayoutManager(new CarouselLayoutManager());
        adapter.setOnItemClickListener(entry -> {
            launchEntryDetails(entry.getId());
        });
        binding.recyclerView.setAdapter(adapter);
        loadEntries();
    }

    public void loadEntries() {
        List<Entry> entryList = db.entryDao().getAllOnStatus(EntryStatus.ONGOING);
        adapter.submitList(entryList);
        setUpEmptyView(entryList);
    }

    public void setUpEmptyView(List<Entry> entryList) {
        if (entryList == null || entryList.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
        }
    }

    public void launchEntryDetails(int entryId) {
        Intent intent = new Intent(this, EntryDetailsActivity.class);
        intent.putExtra("entry", entryId);
        startActivity(intent);
    }

    public void cleanTmpDir() {
        File tmpEntriesImages = new File(getApplicationContext().getFilesDir(), "entries_images/tmp");
        if (tmpEntriesImages.isDirectory()) {
            boolean cleaned = emptyDir(tmpEntriesImages);
            Log.d("MainActivity", "entries_images/tmp cleaned correctly?: " + cleaned);
        }
    }

    public boolean emptyDir(File directory) {
        boolean success = true;
        File[] ls = directory.listFiles();
        for (File f : ls) {
            if (f.isDirectory()) {
                success = emptyDir(f);
            } else {
                success = f.delete();
            }
        }
        return success;
    }

    public void logCurrentAppFiles() {
        File entriesImages = new File(getApplicationContext().getFilesDir(), "entries_images/");
        Log.d("app files", "ls entries_images\n" + ls(entriesImages));
        File tmpEntriesImages = new File(getApplicationContext().getFilesDir(), "entries_images/tmp");
        Log.d("app files", "ls entries_images/tmp\n" + ls(tmpEntriesImages));
    }

    public String ls(File directory) {
        StringBuilder printLs = new StringBuilder();
        String[] list = directory.list();
        if (list != null) {
            Arrays.sort(list);
            for (String fileName : list) {
                File file = new File(directory, fileName);
                if (file.isDirectory()) {
                    printLs.append("d");
                } else if (file.isFile()) {
                    printLs.append("-");
                } else {
                    printLs.append(" ");
                }
                printLs.append("\t" + fileName + "\n");
            }
        }
        return printLs.toString();
    }

}