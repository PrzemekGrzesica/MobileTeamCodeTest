package com.grzesica.przemek.artistlist.Container;

import android.content.Context;
import android.os.Handler;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcherDIBuilder implements IDataFetcherDIBuilder {

    public IDataBaseAdapterDIBuilder mDataBaseAdapterDIBuilder;
    public IHttpHandlerDIBuilder mHttpHandlerDIBuilder;
    public Handler mHandler;

    public DataFetcherDIBuilder dataBaseAdapter(){
        mDataBaseAdapterDIBuilder = new DataBaseAdapterDIBuilder();
        return this;
    }
    public DataFetcherDIBuilder httpHandlerDIBuilder(){
        mHttpHandlerDIBuilder = new HttpHandlerDIBuilder();
        return this;
    }

    public DataFetcherDIBuilder handler(){
        mHandler= new Handler();
        return this;
    }

    @Override
    public DataFetcher build(Context context) {
        return new DataFetcher (this, context);
    }
}
