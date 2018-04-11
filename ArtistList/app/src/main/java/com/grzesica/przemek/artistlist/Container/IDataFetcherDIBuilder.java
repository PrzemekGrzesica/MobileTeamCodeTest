package com.grzesica.przemek.artistlist.Container;

import android.content.Context;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.DataFetcher;

/**
 * Created by przemek on 05.03.18.
 * An abstraction for dependency injection through DataFetcher class constructor
 * by means of a builder.
 */

public interface IDataFetcherDIBuilder {
    DataFetcher build(Context context);
}

