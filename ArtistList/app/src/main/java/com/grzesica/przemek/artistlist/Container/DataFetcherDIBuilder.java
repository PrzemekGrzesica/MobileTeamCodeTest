package com.grzesica.przemek.artistlist.Container;

import android.content.Context;
import android.os.Handler;

import com.grzesica.przemek.artistlist.Model.DataFetcher;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcherDIBuilder implements IDataFetcherDIBuilder {

    public IDataBaseAdapterDIBuilder mDataBaseAdapterDIBuilder;
    public IHttpHandlerDIBuilder mHttpHandlerDIBuilder;
    public IJsonObjectExtended mJsonObjectExtended;
    public Handler mHandler;
    public AbstractExecutorService mThreadPoolExecutor;

    public DataFetcherDIBuilder dataBaseAdapter(){
        mDataBaseAdapterDIBuilder = new DataBaseAdapterDIBuilder();
        return this;
    }
    public DataFetcherDIBuilder httpHandlerDIBuilder(){
        mHttpHandlerDIBuilder = new HttpHandlerDIBuilder();
        return this;
    }

    public DataFetcherDIBuilder jsonObjectExtended(){
        mJsonObjectExtended = new JsonObjectExtended();
        return this;
    }

    public DataFetcherDIBuilder handler(Context context){
        mHandler = new Handler(context.getMainLooper());
        return this;
    }

    public DataFetcherDIBuilder threadPoolExecutor(){
        mThreadPoolExecutor = new ThreadPoolExecutor(1, 1, 1,
                                  TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
        return this;
    }
    @Override
    public DataFetcher build(Context context) {
        return new DataFetcher (this, context);
    }
}
