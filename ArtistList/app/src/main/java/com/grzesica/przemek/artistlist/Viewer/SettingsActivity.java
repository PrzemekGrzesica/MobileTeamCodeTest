package com.grzesica.przemek.artistlist.Viewer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.R;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public void onClickUpdate(View view){
        Context context = this.getApplicationContext();
        Intent intent = new Intent(context, DataFetchingService.class);
        intent.putExtra(DataFetchingService.STR_MESSAGE, "Please, wait for database refreshing...");
        context.startService(intent);
        finish();
    }

    public void onClickCancel(View view){
        finish();
    }

}
