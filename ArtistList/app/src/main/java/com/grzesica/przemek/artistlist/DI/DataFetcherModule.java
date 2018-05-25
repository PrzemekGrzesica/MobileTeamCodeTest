package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataFetcher;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;
import com.grzesica.przemek.artistlist.Model.Utilities.IToastRunnable;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import java.util.concurrent.AbstractExecutorService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataFetcherModule {
    @Provides
    @Singleton
    public IDataFetcher provideDataFetcher(AbstractExecutorService threadPoolExecutor,
                                           IDataBaseManager dataBaseManager, IToastRunnable toastRunnable,
                                           IHttpHandler httpHandler, IJsonObjectExtended jsonObjectExtended,
                                           IExtendedHandler extendedHandler, IGuiContainer guiContainer){
        return new DataFetcher(threadPoolExecutor, dataBaseManager, toastRunnable, httpHandler,
                                jsonObjectExtended, extendedHandler, guiContainer);
    }

    @Provides
    public IJsonObjectExtended provideJsonObjectExtended() {
        return new JsonObjectExtended();
    }

}
