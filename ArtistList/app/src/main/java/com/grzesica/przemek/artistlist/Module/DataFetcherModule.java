package com.grzesica.przemek.artistlist.Module;

import android.content.Context;

import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;

/**
 * Created by przemek on 18.04.18.
 */
@Module
public class DataFetcherModule {

    private final Context mContext;

    public DataFetcherModule(Context context){
        this.mContext = context;
    }

    @Provides
    public AbstractExecutorService provideThreadPoolExecutor() {
        return new ThreadPoolExecutor(1, 1, 1,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
    }

    @Provides
    public IHttpHandler provideHttpHandler() {
        return new HttpHandler();
    }

    @Provides
    public IJsonObjectExtended provideJsonObjectExtended() {
        return new JsonObjectExtended();
    }

    @Provides
    public IDataBaseAdapter provideDataBaseAdapter() {
        return new DataBaseAdapter(mContext);
    }
}