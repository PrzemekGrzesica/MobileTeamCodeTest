package com.grzesica.przemek.artistlist.Container;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;

/**
 * Created by przemek on 28.03.18.
 */

public interface IDataBaseHelperDIBuilder{
    DataBaseAdapter.DataBaseHelper build(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion);
}