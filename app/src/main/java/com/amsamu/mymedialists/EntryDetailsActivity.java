package com.amsamu.mymedialists;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amsamu.mymedialists.databinding.ActivityEntryDetailsBinding;

public class EntryDetailsActivity extends AppCompatActivity {

    public ActivityEntryDetailsBinding binding;
    public int listId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEntryDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("EntryDetailsActivity","created EntryDetailsActivity");
        listId = getIntent().getExtras().getInt("containerList");
        setUpTopBar();
    }

    public void setUpTopBar() {
        binding.topAppBar.setNavigationOnClickListener(v -> returnToDisplayList());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.action_save){
                insertToDB();
                returnToDisplayList();
            }
            return true;
        });
    }

    public void returnToDisplayList(){
        Intent intent = new Intent(this, DisplayListActivity.class);
        intent.putExtra("selectedList", listId);
        startActivity(intent);
        finish();
    }

    public void insertToDB(){

    }

}