package com.grzesica.przemek.artistlist.DI;

import android.content.Context;

import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataFetcher;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataFetcherModule {
    @Provides
    @Singleton
    public IDataFetcher provideDataFetcher(AbstractExecutorService threadPoolExecutor, Context context, IDataBaseManager dataBaseManager,
                                           IHttpHandler httpHandler, IJsonObjectExtended jsonObjectExtended, IExtendedHandler extendedHandler){
        return new DataFetcher(threadPoolExecutor, context, dataBaseManager, httpHandler, jsonObjectExtended, extendedHandler);
    }

    @Provides
    public IJsonObjectExtended provideJsonObjectExtended() {
        return new JsonObjectExtended();
    }

}
