package com.grzesica.przemek.artistlist.Module;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Container.BitmapFactoryOptions;
import com.grzesica.przemek.artistlist.Container.ExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.ExtendedURL;
import com.grzesica.przemek.artistlist.Container.IBitmapFactoryOptions;
import com.grzesica.przemek.artistlist.Container.IExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.IExtendedUrl;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.Viewer.AlbumsListActivity;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.DATABASE_NAME;

/**
 * Created by przemek on 28.04.18.
 */
@Module
public class ApplicationModule {

    @Provides
    public IDataBaseManager provideDataBaseManager() {
        return new DataBaseManager();
    }

    @Provides
    public AsyncTask provideUpdatesCheck(){
        return new UpdatesCheck();
    }

    @Provides
    @Named("intent")
    public Parcelable provideIntent(Context context){
        return new Intent(context, AlbumsListActivity.class);
    }

    @Provides
    @Named("contentValues")
    public Parcelable provideContentValues(){
        return new ContentValues();
    }

    @Provides
    public SQLiteOpenHelper provideDataBaseHelper(Context context) {
        String strDbPath = context.getDatabasePath(DATABASE_NAME).toString();
        int dbVersion  = SQLiteDatabase.openDatabase(strDbPath, null, 0).getVersion();
        return new DataBaseHelper(context, dbVersion);
    }

    @Provides
    public AbstractExecutorService provideThreadPoolExecutor() {
        return new ThreadPoolExecutor(1, 1, 1,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
    }

    @Provides
    public IHttpHandler provideHttpHandler() {
        return new HttpHandler();
    }

    @Provides
    public IJsonObjectExtended provideJsonObjectExtended() {
        return new JsonObjectExtended();
    }

    @Provides
    public Appendable provideStringBuilder(){
        return new StringBuilder();
    }

    @Provides
    public OutputStream provideOutputStream(){
        return new ByteArrayOutputStream();
    }

    @Provides
    public IExtendedUrl provideExtendedUrl(){
        return new ExtendedURL();
    }

    @Provides
    public IBitmapFactoryOptions provideBitmapFactoryOptions(){
        return new BitmapFactoryOptions();
    }

    @Provides
    public IExtendedBufferReader provideExtendedBufferReader(){
        return new ExtendedBufferReader();
    }
}
