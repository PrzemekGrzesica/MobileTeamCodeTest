package com.grzesica.przemek.artistlist.DI;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DataBaseManagerModule {
    @Provides
    public IDataBaseManager provideDataBaseManager(@Named("contentValues") Parcelable contentValues, SQLiteOpenHelper dataBaseHelper) {
        return new DataBaseManager(contentValues, dataBaseHelper);
    }
}
