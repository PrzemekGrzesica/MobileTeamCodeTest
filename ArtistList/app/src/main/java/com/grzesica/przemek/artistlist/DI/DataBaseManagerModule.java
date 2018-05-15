package com.grzesica.przemek.artistlist.DI;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.DATABASE_NAME;

@Module
public class DataBaseManagerModule {

    @Provides
    @Singleton
    public IDataBaseManager provideDataBaseManager(@Named("AlbumsContentValues") Parcelable albumsContentValues,
                                                   @Named("ArtistContentValues") Parcelable artistContentValues,
                                                   @Named("MD5ContentValues") Parcelable mMD5KeyContentValues,
                                                   @Named("dataFetchingService") Parcelable intent,
                                                   Context context, IGuiContainer guiContainer) {
        return new DataBaseManager(albumsContentValues, artistContentValues, mMD5KeyContentValues, intent, context, guiContainer);
    }

    @Provides
    @Singleton
    @Named("Present")
    public SQLiteOpenHelper providePresentDatabaseHelper(Context context, IDataBaseManager dataBaseManager) {
        return new DataBaseHelper(context, DATABASE_NAME, ((DataBaseManager)dataBaseManager).getDbVersion());
    }
}
