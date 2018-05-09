package com.grzesica.przemek.artistlist.DI;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;
import android.widget.CursorAdapter;

import com.grzesica.przemek.artistlist.Adapter.ArtistListAdapter;
import com.grzesica.przemek.artistlist.Adapter.CursorManager;
import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Container.BitmapFactoryOptions;
import com.grzesica.przemek.artistlist.Container.ExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.ExtendedURL;
import com.grzesica.przemek.artistlist.Container.IBitmapFactoryOptions;
import com.grzesica.przemek.artistlist.Container.IExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.IExtendedUrl;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.ArtistFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.AlbumsListActivity;
import com.grzesica.przemek.artistlist.Viewer.SettingsActivity;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
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
    public CursorAdapter provideArtistListAdapter(Context context){
        Cursor cursor = new CursorManager().getCursor();
        if (cursor!=null) {
            return new ArtistListAdapter(context, cursor);
        }else{
            return null;
        }
    }

    @Provides
    public ICursorManager provideCursorManager(){
        return new CursorManager();
    }

    @Provides
    public IntentService provideDataFetchingService() {
        return new DataFetchingService();
    }

    @Provides
    @Named("albumsListActivity")
    public Parcelable provideIntentAlbAct(Context context){
        return new Intent(context, AlbumsListActivity.class);
    }

    @Provides
    @Named("dataFetchingService")
    public Parcelable provideIntentDFS(Context context){
        return new Intent(context, DataFetchingService.class);
    }


    @Provides
    @Named("contentValues")
    public Parcelable provideContentValues(){
        return new ContentValues();
    }

    @Provides
    @Named("settingsActivity")
    public Parcelable providesIntentSettingsActivity(Context context){
        return new Intent(context, SettingsActivity.class);
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
    public IJsonObjectExtended provideJsonObjectExtended() {
        return new JsonObjectExtended();
    }

    @Provides
    @Named("stringBuilder")
    public Appendable provideStringBuilder(){
        return new StringBuilder();
    }

    @Provides
    @Named("stringBuffer")
    public Appendable provideStringBuffer(){
        return new StringBuffer();
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
