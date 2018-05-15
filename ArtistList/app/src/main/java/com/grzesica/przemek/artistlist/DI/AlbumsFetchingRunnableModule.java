package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.AlbumsFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumsFetchingRunnableModule {
    @Provides
    @Named("AlbumsFetching")
    public Runnable provideAlbumsFetchingRunnable(IDataBaseManager dataBaseManager, JsonObjectExtended albumsJsonObj,
                                                  IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor, IGuiContainer guiContainer){
        return new AlbumsFetchingRunnable(dataBaseManager, albumsJsonObj, httpHandler, threadPoolExecutor, guiContainer);
    }
}
