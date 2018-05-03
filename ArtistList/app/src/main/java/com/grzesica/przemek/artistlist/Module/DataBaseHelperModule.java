package com.grzesica.przemek.artistlist.Module;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Module.Annotations.ApplicationContext;
import com.grzesica.przemek.artistlist.Module.Annotations.DatabaseInfo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by przemek on 28.04.18.
 */

@Module
public class DataBaseHelperModule {

    private final Context mContext;

    public DataBaseHelperModule(Context context){
        this.mContext = context;
    }

    @Provides
    public Parcelable provideContentValues(){
        return new ContentValues();
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mContext;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return "artistDb.db";
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return 1;
    }
}
