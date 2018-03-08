package com.grzesica.przemek.artistlist.Container;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcherDIBuilder implements IDataFetcherDIBuilder {
    public IDataBaseAdapter mDataBaseAdapter;
    public DataFetcherDIBuilder dataBaseAdapter(){
        mDataBaseAdapter = DataBaseAdapter.newInstance();
        return this;
    }
}
