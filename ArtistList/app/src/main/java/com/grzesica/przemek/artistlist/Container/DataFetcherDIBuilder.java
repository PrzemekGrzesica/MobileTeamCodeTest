package com.grzesica.przemek.artistlist.Container;

import com.grzesica.przemek.artistlist.Adapter.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Adapter.IDataBaseAdapter;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcherDIBuilder implements IDataFetcherDIBuilder {
    public IDataBaseAdapter mDataBaseAdapter;
    public DataFetcherDIBuilder dataBaseAdapter(){
        mDataBaseAdapter = new DataBaseAdapter();
        return this;
    }
}
