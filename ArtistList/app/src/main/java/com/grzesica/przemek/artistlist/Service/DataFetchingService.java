package com.grzesica.przemek.artistlist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataFetcher;

import javax.inject.Inject;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetchingService extends IntentService {

    @Inject IDataFetcher mDataFetcher;
    @Inject IExtendedHandler mExtendedHandler;
    public static final String STR_MESSAGE = "message";

    @Inject
    public DataFetchingService() {
        super("Data fetching service");
        ArtistListApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String text = intent.getStringExtra(STR_MESSAGE);
        showText(text);
        DataFetcher dataFetcher = (DataFetcher) mDataFetcher;
        dataFetcher.getData();
    }

    private void showText(final String text) {
        ((Handler)mExtendedHandler).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
