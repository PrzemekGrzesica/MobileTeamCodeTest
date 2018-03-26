package com.grzesica.przemek.artistlist.Container;

import android.content.Context;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcherDIBuilder implements IDataFetcherDIBuilder {
    public IDataBaseAdapter mDataBaseAdapter;
    public IHttpHandlerDIBuilder mHttpHandlerDIBuilder;

    public DataFetcherDIBuilder dataBaseAdapter(Context context){
//        mDataBaseAdapter = DataBaseAdapter.newInstance(context);
        mDataBaseAdapter = new DataBaseAdapter(context);
        return this;
    }
    public DataFetcherDIBuilder httpHandlerDIBuilder(){
        mHttpHandlerDIBuilder = new HttpHandlerDIBuilder();
        return this;
    }

    @Override
    public DataFetcher build(Context context) {
        return new DataFetcher (this, context);
    }
}
