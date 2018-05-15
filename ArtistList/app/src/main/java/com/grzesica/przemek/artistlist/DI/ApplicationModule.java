package com.grzesica.przemek.artistlist.DI;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.AlbumsListActivity;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;
import com.grzesica.przemek.artistlist.Viewer.SettingsActivity;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by przemek on 28.04.18.
 */
@Module
public class ApplicationModule {


    @Provides
    public Closeable provideByteArrayInputStream(IGuiContainer singletonGuiCont){
        GuiContainer sgc = (GuiContainer)singletonGuiCont;
        return new ByteArrayInputStream(sgc.getImageByteArray());
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
    @Named("ArtistContentValues")
    public Parcelable provideArtistContentValues(){
        return new ContentValues();
    }

    @Provides
    @Named("AlbumsContentValues")
    public Parcelable provideAlbumsContentValues(){
        return new ContentValues();
    }

    @Provides
    @Named("MD5ContentValues")
    public Parcelable provideMD5ContentValues(){
        return new ContentValues();
    }

    @Provides
    @Named("settingsActivity")
    public Parcelable providesIntentSettingsActivity(Context context){
        return new Intent(context, SettingsActivity.class);
    }



    @Provides
    public AbstractExecutorService provideThreadPoolExecutor() {
        return new ThreadPoolExecutor(1, 1, 1,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
    }








}
