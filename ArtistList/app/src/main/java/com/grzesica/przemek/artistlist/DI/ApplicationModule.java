package com.grzesica.przemek.artistlist.DI;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Model.Utilities.IToastRunnable;
import com.grzesica.przemek.artistlist.Model.Utilities.ToastRunnable;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.AlbumsList.AlbumsListActivity;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.ArtistListUI;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities.IMySwipeRefreshLayout;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities.MySwipeRefreshLayout;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.IArtistListUI;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;
import com.grzesica.przemek.artistlist.Viewer.SettingsActivity.SettingsActivity;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities.SwipeRefreshRunnable;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by przemek on 28.04.18.
 */
@Module
public class ApplicationModule {

    @Provides
    public IToastRunnable provideToastRunnable(Context context){
        return new ToastRunnable(context);
    }

    @Provides
    @Singleton
    public IMySwipeRefreshLayout provideMySwipeRefreshLayout(IExtendedHandler handler,
                                                             @Named("swipeRefresh") Runnable runnable){
        return new MySwipeRefreshLayout(handler, runnable);
    }

    @Provides
    public Closeable provideByteArrayInputStream(IGuiContainer singletonGuiCont){
        GuiContainer sgc = (GuiContainer)singletonGuiCont;
        return new ByteArrayInputStream(sgc.getImageByteArray());
    }

    @Provides
    public IArtistListUI provideArtistListUI(Context context, ICursorManager cursorManager){
        return new ArtistListUI(context, cursorManager);
    }

    @Singleton
    @Provides
    @Named("swipeRefresh")
    public Runnable provideSwipeRefreshRunnable(IArtistListUI artistListUI){
        return  new SwipeRefreshRunnable(artistListUI);
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

    @Singleton
    @Provides
    public AbstractExecutorService provideThreadPoolExecutor() {
        return new ThreadPoolExecutor(1, 1, 1,
                TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>());
    }








}
