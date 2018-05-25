package com.grzesica.przemek.artistlist.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Container.ExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataFetcher;
import com.grzesica.przemek.artistlist.Model.Utilities.IToastRunnable;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetchingService extends IntentService {

    @Inject
    Provider<IDataFetcher> mDataFetcher;
    @Inject
    Provider<IDataBaseManager> mDataBaseManager;
    @Inject
    IExtendedHandler mExtendedHandler;
    public static String STR_MESSAGE = "message";
    @Inject
    IToastRunnable mToastRunnable;

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
        DataFetcher dataFetcher = (DataFetcher) mDataFetcher.get();
        dataFetcher.getEndPointData();
    }

    private void showText(final String text) {
        mToastRunnable.setToastText(text);
        runOnUiThread(mToastRunnable);
    }

    private void runOnUiThread(Runnable r) {
        ((ExtendedHandler)mExtendedHandler).post(r);
    }
}
