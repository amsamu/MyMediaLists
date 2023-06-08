package com.amsamu.mymedialists;

import static com.amsamu.mymedialists.util.SharedMethods.loadListsToMenu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.amsamu.mymedialists.adapters.MediaListAdapter;
import com.amsamu.mymedialists.database.tables.Entry;
import com.amsamu.mymedialists.database.tables.MediaList;
import com.amsamu.mymedialists.database.AppDatabase;
import com.amsamu.mymedialists.databinding.ActivityDisplayListBinding;
import com.amsamu.mymedialists.util.SortingField;
import com.amsamu.mymedialists.util.ToastManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class DisplayListActivity extends AppCompatActivity {

    AppDatabase db;
    public ActivityDisplayListBinding binding;
    int listId;
    MediaList list;
    MediaListAdapter adapter;
    int highestListId;
    boolean launchedNewList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        binding = ActivityDisplayListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get selected list from invoking activity
        listId = getIntent().getExtras().getInt("selectedList");
        Log.d("DisplayListActivity", "Launched DisplayListActivity, selectedList:" + listId);

        setUpEverything();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(launchedNewList) {
            int prevHighestId = highestListId;
            highestListId = db.mediaListDao().getHighestId();
            if (highestListId > prevHighestId) {
                listId = highestListId;
            }
        }
        launchedNewList = false;
        setUpEverything();
    }

    public void setUpEverything(){
        list = db.mediaListDao().getMediaList(listId);
        setUpTopBar();
        binding.navigation.getMenu().findItem(R.id.nav_lists).getSubMenu().removeGroup(R.id.nav_group_lists);
        setUpNavMenu();
        setUpFAB();
        adapter = new MediaListAdapter();
        setUpRecyclerView();
    }

    public void setUpTopBar() {
        binding.topAppBar.setTitle(list.name);
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getGroupId() == R.id.group_sort_by) {
                handleSortBySelected(item);
            } else if (item.getItemId() == R.id.action_sort_ascending) {
                handleSortAscendingSelected(item);
            } else if (item.getGroupId() == R.id.disp_list_group_options) {
                handleOptionsSelected(item);
            }
            return true;
        });

        if (list.sortAscending) {
            binding.topAppBar.getMenu().findItem(R.id.action_sort_ascending).setIcon(R.drawable.cil_sort_ascending).setTitle(R.string.sort_ascending);
        }else{
            binding.topAppBar.getMenu().findItem(R.id.action_sort_ascending).setIcon(R.drawable.cil_sort_descending).setTitle(R.string.sort_descending);
        }
        loadSortField();
    }

    public void loadSortField() {
        switch (list.sortField) {
            case STATUS:
                binding.topAppBar.getMenu().findItem(R.id.sort_by_status).setChecked(true);
                break;
            case NAME:
                binding.topAppBar.getMenu().findItem(R.id.sort_by_name).setChecked(true);
                break;
            case AUTHOR:
                binding.topAppBar.getMenu().findItem(R.id.sort_by_author).setChecked(true);
                break;
            case RELEASE_YEAR:
                binding.topAppBar.getMenu().findItem(R.id.sort_by_release_year).setChecked(true);
                break;
            case START_DATE:
                binding.topAppBar.getMenu().findItem(R.id.sort_by_start_date).setChecked(true);
                break;
            case FINISH_DATE:
                binding.topAppBar.getMenu().findItem(R.id.sort_by_finish_date).setChecked(true);
                break;
        }
    }

    public void handleSortBySelected(MenuItem item) {
        binding.topAppBar.getMenu().findItem(item.getItemId()).setChecked(true);
        switch (item.getItemId()) {
            case R.id.sort_by_status:
                list.sortField = SortingField.STATUS;
                break;
            case R.id.sort_by_name:
                list.sortField = SortingField.NAME;
                break;
            case R.id.sort_by_author:
                list.sortField = SortingField.AUTHOR;
                break;
            case R.id.sort_by_release_year:
                list.sortField = SortingField.RELEASE_YEAR;
                break;
            case R.id.sort_by_start_date:
                list.sortField = SortingField.START_DATE;
                break;
            case R.id.sort_by_finish_date:
                list.sortField = SortingField.FINISH_DATE;
                break;
        }
        db.mediaListDao().update(list);
        loadEntries();
    }

    public void handleSortAscendingSelected(MenuItem item) {
        if (list.sortAscending) {
            list.sortAscending = false;
            db.mediaListDao().update(list);
            item.setIcon(R.drawable.cil_sort_descending);
            item.setTitle(R.string.sort_descending);
            loadEntries();
        } else {
            list.sortAscending = true;
            db.mediaListDao().update(list);
            item.setIcon(R.drawable.cil_sort_ascending);
            item.setTitle(R.string.sort_ascending);
            loadEntries();
        }
    }

    public void handleOptionsSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_rename_list:
                launchListDetails(listId);
                break;
            case R.id.action_delete_list:
                confirmDeleteList();
                break;
        }
    }

    public void launchListDetails(int listId) {
        Intent intent = new Intent(this, ListDetailsActivity.class);
        intent.putExtra("list", listId);
        intent.putExtra("prevActivityIsHome", false);
        startActivity(intent);
    }

    public void confirmDeleteList() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.question_delete_list)
                .setMessage(R.string.message_delete_list)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                })
                .setPositiveButton(R.string.delete, (dialog, which) -> {
                    deleteList();
                    finish();
                })
                .show();
    }

    public void setUpRecyclerView() {
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setOnItemClickListener(entry -> {
            launchEntryDetails(entry.getId());
        });
        binding.recyclerView.setAdapter(adapter);
        loadEntries();
    }

    public void loadEntries() {
        List<Entry> entryList = null;

        if (list.sortAscending) {
            entryList = getEntryListAsc();
        } else {
            entryList = getEntryListDesc();
        }
        if(entryList != null) {
            adapter.submitList(entryList);
        }
        setUpEmptyView(entryList);
    }

    public void setUpEmptyView(List<Entry> entryList){
        if(entryList == null || entryList.isEmpty()){
            binding.emptyView.setVisibility(View.VISIBLE);
        }else{
            binding.emptyView.setVisibility(View.GONE);
        }
    }

    public List<Entry> getEntryListAsc() {
        switch (list.sortField) {
            case STATUS:
                return db.entryDao().getOrderedByStatusAsc(listId);
            case NAME:
                return db.entryDao().getOrderedByNameAsc(listId);
            case AUTHOR:
                return db.entryDao().getOrderedByAuthorAsc(listId);
            case RELEASE_YEAR:
                return db.entryDao().getOrderedByReleaseYearAsc(listId);
            case START_DATE:
                return db.entryDao().getOrderedByStartDateAsc(listId);
            case FINISH_DATE:
                return db.entryDao().getOrderedByFinishDateAsc(listId);
            default:
                return null;
        }
    }

    public List<Entry> getEntryListDesc() {
        switch (list.sortField) {
            case STATUS:
                return db.entryDao().getOrderedByStatusDesc(listId);
            case NAME:
                return db.entryDao().getOrderedByNameDesc(listId);
            case AUTHOR:
                return db.entryDao().getOrderedByAuthorDesc(listId);
            case RELEASE_YEAR:
                return db.entryDao().getOrderedByReleaseYearDesc(listId);
            case START_DATE:
                return db.entryDao().getOrderedByStartDateDesc(listId);
            case FINISH_DATE:
                return db.entryDao().getOrderedByFinishDateDesc(listId);
            default:
                return null;
        }
    }

    public void setUpNavMenu() {
        loadListsToMenu(db, binding.navigation.getMenu());
        binding.navigation.getMenu().findItem(listId).setCheckable(true);
        binding.navigation.getMenu().findItem(listId).setChecked(true); // Check/highlight this activity's corresponding menu item

        // Switch activities when clicking on an option from the navigation menu

        binding.topAppBar.setNavigationOnClickListener(v -> binding.drawerLayout.open());

        binding.navigation.setNavigationItemSelectedListener(item -> {
            handleNavMenuItemSelected(item);
            binding.drawerLayout.close();
            return true;
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
            finish();
            return;
        } else if (menuItem.getGroupId() == R.id.nav_group_lists) {
            intent = new Intent(this, DisplayListActivity.class);
            intent.putExtra("selectedList", menuItem.getItemId());
        } else if (menuItem.getItemId() == R.id.nav_item_new_list) {
            launchedNewList = true;
            highestListId = db.mediaListDao().getHighestId();
            launchListDetails(-1);
        } else if (menuItem.getItemId() == R.id.nav_item_settings) {
            intent = new Intent(this, SettingsActivity.class);
        }

        // Launch new activity
        if (intent != null) {
            startActivity(intent);
            if (menuItem.getItemId() != R.id.nav_item_new_list && menuItem.getItemId() != R.id.nav_item_settings) {
                finish(); // destroy this activity so it doesn't stay in the background
            }
        }
        Log.d("DisplayListActivity", "leaving DisplayListActivity");
    }

    public void setUpFAB() {
        binding.floatingActionButton.setOnClickListener(v -> {
            launchEntryDetails(-1);
        });
    }

    public void launchEntryDetails(int entryId) {
        Intent intent = new Intent(this, EntryDetailsActivity.class);
        intent.putExtra("containerList", listId);
        intent.putExtra("entry", entryId);
        startActivity(intent);
    }

    public void deleteList() {
        db.entryDao().deleteAllInList(listId);
        db.mediaListDao().delete(list);
        ToastManager.showToast(this, R.string.list_deleted, Toast.LENGTH_SHORT);
    }

}