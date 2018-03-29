package com.grzesica.przemek.artistlist.Container;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteClosable;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;

/**
 * Created by przemek on 27.03.18.
 */

public class DataBaseAdapterDIBuilder implements IDataBaseAdapterDIBuilder {

    public IDataBaseHelperDIBuilder mDataBaseHelper;
    public Parcelable mContentValues;

    public DataBaseAdapterDIBuilder dataBaseHelperDIBuilder(){
        mDataBaseHelper = new DataBaseHelperDIBuilder();
        return this;
    }

    public DataBaseAdapterDIBuilder contentValues(){
        mContentValues = new ContentValues();
        return this;
    }

    @Override
    public DataBaseAdapter build(Context context) {
        return new DataBaseAdapter (this, context);
    }
}
//Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion