package com.amsamu.mymedialists;

import androidx.appcompat.app.AppCompatActivity;

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
        binding.topAppBar.setNavigationOnClickListener(v -> returnToMainActivity());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_save){
                insertToDB();
                returnToMainActivity();
            }
            return true;
        });
    }

    public void returnToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void insertToDB() {
        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        MediaListDao mListDao = db.mediaListDao();
        mListDao.insertAll(new MediaList(mListDao.getHighestId() + 1, binding.editText2.getText().toString()));
    }

}