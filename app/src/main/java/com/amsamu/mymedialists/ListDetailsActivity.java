package com.amsamu.mymedialists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.view.ContextThemeWrapper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amsamu.mymedialists.dao.MediaListDao;
import com.amsamu.mymedialists.data.MediaList;
import com.amsamu.mymedialists.databinding.ActivityListDetailsBinding;

import java.util.List;

public class ListDetailsActivity extends AppCompatActivity {

    public ActivityListDetailsBinding binding;

    ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("ListDetailsActivity","created ListDetailsActivity");

        setUpActionBar();
    }

    public void setUpActionBar() {
        startSupportActionMode(actModeCallback);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    ActionMode.Callback actModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.details_toolbar, menu);
            mode.setTitle(R.string.new_list);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if(item.getItemId() == R.id.action_save){
                insertarEnBD();
                volverAMainActivity();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            volverAMainActivity();
        }
    };

    public void volverAMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void insertarEnBD() {
        AppDatabase bd = AppDatabase.getDatabase(getApplicationContext());
        MediaListDao mListDao = bd.mediaListDao();
        mListDao.insertAll(new MediaList(mListDao.getHighestId() + 1, binding.editText2.getText().toString()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}