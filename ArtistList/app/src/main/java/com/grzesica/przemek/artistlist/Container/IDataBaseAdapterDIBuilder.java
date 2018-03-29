package com.grzesica.przemek.artistlist.Container;

import android.content.Context;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;

/**
 * Created by przemek on 27.03.18.
 * An abstraction for dependency injection through DataBaseAdapter class constructor by means of a builder.
 */

public interface IDataBaseAdapterDIBuilder {
    DataBaseAdapter build(Context context);
}
