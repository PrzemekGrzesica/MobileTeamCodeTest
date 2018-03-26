package com.grzesica.przemek.artistlist.Service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Container.DataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.DataFetcher;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetchingService extends IntentService {

    private Context mContext;
    private Handler mHandler;
    public static final String STR_MESSAGE = "message";
    public static final String INT_DATABASE_VERSION = "dataBaseVersion";

    public DataFetchingService() {
        super("Data fetching service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mHandler = new Handler();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String text = intent.getStringExtra(STR_MESSAGE);
        int dbVersion = intent.getIntExtra(INT_DATABASE_VERSION,0);
        showText(text);
//        Integer databaseVersion = (Integer) 2;
        DataFetcherDIBuilder depInjBuilder = new DataFetcherDIBuilder();
        DataFetcher dataFetcher = depInjBuilder
                .httpHandlerDIBuilder()
                .dataBaseAdapter(mContext)
                .build(mContext);
        dataFetcher.getData();
    }

    private void showText(final String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
