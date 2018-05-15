package com.grzesica.przemek.artistlist.Viewer;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.R;

import javax.inject.Inject;
import javax.inject.Named;

public class SettingsActivity extends AppCompatActivity {

    @Inject
    @Named("dataFetchingService")
    Parcelable mIntent;
    @Inject
    IGuiContainer mGuiContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        ArtistListApplication.getApplicationComponent().inject(this);
    }

    public void onClickUpdate(View view){
        Context context = this.getApplicationContext();
        Intent intent = (Intent)mIntent;
        intent.putExtra(DataFetchingService.STR_MESSAGE, "Please, wait FOR database refreshing...");
        ((GuiContainer)mGuiContainer).setServiceFlag(true);
        context.startService(intent);
        finish();
    }

    public void onClickCancel(View view){
        ((GuiContainer)mGuiContainer).setServiceFlag(false);
        finish();
    }

}
