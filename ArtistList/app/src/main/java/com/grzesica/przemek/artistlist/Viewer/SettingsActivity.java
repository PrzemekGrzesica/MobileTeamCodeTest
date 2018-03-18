package com.grzesica.przemek.artistlist.Viewer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.R;


public class SettingsActivity extends AppCompatActivity {
//    Context context = this.getApplicationContext();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public void onClickUpdate(View view){
//        Integer databaseVersion = (Integer) 2;

//        DependencyInjectionBuilder depInjBuilder = new DependencyInjectionBuilder();
//        HttpHandler httpHandler = depInjBuilder
//                                    .byteArrayOutputStream()
//                                    .strBuilder()
//                                    .extendedUrl()
//                                    .extendedBufferedReader()
//                                    .build();
////        new GetData(getApplicationContext(), httpHandler).execute(databaseVersion);
        Context context = this.getApplicationContext();
        Intent intent = new Intent(context, DataFetchingService.class);
        intent.putExtra(DataFetchingService.STR_MESSAGE, "Nie wiem co napisać...");
        context.startService(intent);
    }

    public void onClickCancel(View view){
        finish();
    }

}
