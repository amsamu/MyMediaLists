package com.amsamu.mymedialists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.amsamu.mymedialists.dao.MediaListDao;
import com.amsamu.mymedialists.data.MediaList;
import com.amsamu.mymedialists.databinding.ActivityListDetailsBinding;

public class ListDetailsActivity extends AppCompatActivity {

    public ActivityListDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("ListDetailsActivity","created ListDetailsActivity");

        setUpTopBar();
    }

    public void setUpTopBar() {
        binding.topAppBar.setNavigationOnClickListener(v -> volverAMainActivity());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_save){
                insertarEnBD();
                volverAMainActivity();
            }
            return true;
        });
    }

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