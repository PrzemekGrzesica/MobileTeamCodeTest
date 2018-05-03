package com.grzesica.przemek.artistlist.Module;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Module.Annotations.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by przemek on 28.04.18.
 */
@Module
public class ArtistListActivityModule {

    private final Context mContext;

    public ArtistListActivityModule(Context context) {
        this.mContext = context;
    }

    @ApplicationContext
    @Provides
    public Context provideContext(){
        return mContext;
    }

    @Provides
    public SQLiteOpenHelper provideDataBaseHelper() {
        return new DataBaseHelper(mContext, );
    }


}
