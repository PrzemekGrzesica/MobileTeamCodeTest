package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.ArtistFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistFetchingRunnableModule {
    @Provides
    @Named("ArtistFetching")
    public Runnable provideArtistFetchingRunnable(IDataBaseManager dataBaseManager, IJsonObjectExtended artistJsonObj,
                                                  IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor, IGuiContainer guiContainer){
        return new ArtistFetchingRunnable(dataBaseManager, artistJsonObj, httpHandler, threadPoolExecutor, guiContainer);
    }
}
