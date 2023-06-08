package com.amsamu.mymedialists;

import static com.amsamu.mymedialists.util.SharedMethods.showInfoDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amsamu.mymedialists.database.tables.MediaList;
import com.amsamu.mymedialists.database.AppDatabase;
import com.amsamu.mymedialists.databinding.ActivityListDetailsBinding;
import com.amsamu.mymedialists.util.ToastManager;

public class ListDetailsActivity extends AppCompatActivity {

    AppDatabase db;
    public ActivityListDetailsBinding binding;
    int listId;
    boolean isNewList = false;
    MediaList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getDatabase(getApplicationContext());
        binding = ActivityListDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listId = getIntent().getExtras().getInt("list");
        Log.d("ListDetailsActivity","list: " + listId);

        if(listId == -1){
            isNewList = true;
            listId = db.mediaListDao().getHighestId() + 1;
            list = new MediaList(listId);
        }else{
            list = db.mediaListDao().getMediaList(listId);
            binding.topAppBar.setTitle(R.string.rename_list);
            binding.editTextListName.setText(list.name);
        }

        Log.d("ListDetailsActivity","created ListDetailsActivity");

        setUpTopBar();
    }

    public void setUpTopBar() {
        binding.topAppBar.setNavigationOnClickListener(v -> finish());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_save){
                clickedActionSave();
            }
            return true;
        });
    }

    public void clickedActionSave() {
        if(!binding.editTextListName.getText().toString().isBlank()){
            saveList();
        }else{
            showInfoDialog(this, R.string.must_fill_list_name);
        }
    }

    public void saveList(){
        // Save name from name field with trailing and leading spaces removed
        list.name = binding.editTextListName.getText().toString().trim();
        if(isNewList){
            insertToDB();
        }else{
            updateToDB();
        }
        finish();
    }

    public void insertToDB(){
        db.mediaListDao().insertAll(list);
        ToastManager.showToast(this, R.string.list_created, Toast.LENGTH_SHORT);
    }

    public void updateToDB(){
        db.mediaListDao().update(list);
        ToastManager.showToast(this, R.string.list_updated, Toast.LENGTH_SHORT);

    }

}