package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.ArtistFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistFetchingRunnableModule {
    @Provides
//    @Singleton
    @Named("ArtistFetching")
    public Runnable provideArtistFetchingRunnable(IDataBaseManager dataBaseManager, JsonObjectExtended artistJsonObj,
                                                  IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor, IGuiContainer guiContainer){
        return new ArtistFetchingRunnable(dataBaseManager, artistJsonObj, httpHandler, threadPoolExecutor, guiContainer);
    }

    @Provides
    public JsonObjectExtended provideJsonObjectExtended(){
        return new JsonObjectExtended();
    }
}
