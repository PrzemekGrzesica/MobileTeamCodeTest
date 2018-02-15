package com.grzesica.przemek.artistlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public void onClickUpdate(View view){
        Integer databaseVersion = (Integer) 2;
        new GetData(getApplicationContext()).execute(databaseVersion);
    }

    public void onClickCancel(View view){
        finish();
    }

}
