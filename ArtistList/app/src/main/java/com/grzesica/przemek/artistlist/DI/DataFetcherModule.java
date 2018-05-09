package com.grzesica.przemek.artistlist.DI;

import android.content.Context;

import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataFetcher;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class DataFetcherModule {
    @Provides
    public IDataFetcher provideDataFetcher(AbstractExecutorService threadPoolExecutor, Context context, IDataBaseManager dataBaseManager,
                                           IHttpHandler httpHandler, IJsonObjectExtended jsonObjectExtended, IExtendedHandler extendedHandler,
                                           @Named("ArtistFetching") Runnable artistFetching, @Named("AlbumsFetching") Runnable albumsFetching){
        return new DataFetcher(threadPoolExecutor, context, dataBaseManager, httpHandler, jsonObjectExtended, extendedHandler,
                    artistFetching, albumsFetching);
    }
}
