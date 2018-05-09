package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.ArtistFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistFetchingRunnableModule {
    @Provides
    @Named("ArtistFetching")
    public Runnable provideArtistFetchingRunnable(IJsonObjectExtended artistJsonObj, IDataBaseManager dataBaseManager,
                                                  IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor){
        return new ArtistFetchingRunnable(artistJsonObj, dataBaseManager, httpHandler, threadPoolExecutor);
    }
}
