package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.AlbumsFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumsFetchingRunnableModule {
    @Provides
    @Named("AlbumsFetching")
    public Runnable provideAlbumsFetchingRunnable(IJsonObjectExtended albumstJsonObj, IDataBaseManager dataBaseManager,
                                                  IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor){
        return new AlbumsFetchingRunnable(albumstJsonObj, dataBaseManager, httpHandler, threadPoolExecutor);
    }
}
