package com.grzesica.przemek.artistlist.Container;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;

/**
 * Created by przemek on 28.03.18.
 */

public class DataBaseHelperDIBuilder implements IDataBaseHelperDIBuilder{

    public SQLiteOpenHelper mDataBaseHelper;

//    public DataBaseHelperDIBuilder dataBaseHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion){
//        mDataBaseHelper = new DataBaseAdapter.DataBaseHelper(context, dbName, factory, dbVersion);
//        return this;
//    }

    @Override
    public DataBaseAdapter.DataBaseHelper build(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        return new DataBaseAdapter.DataBaseHelper(this, context, dbName, factory, dbVersion);
    }
}