package com.grzesica.przemek.artistlist;

import android.app.Application;

import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Module.DaggerDataFetcherComponent;
import com.grzesica.przemek.artistlist.Module.DaggerHttpHandlerComponent;
import com.grzesica.przemek.artistlist.Module.DataFetcherComponent;
import com.grzesica.przemek.artistlist.Module.DataFetcherModule;
import com.grzesica.przemek.artistlist.Module.HttpHandlerComponent;
import com.grzesica.przemek.artistlist.Module.HttpHandlerModule;

/**
 * Created by przemek on 16.04.18.
 */

public class ArtistListApplication extends Application {

    private static DataFetcherComponent dataFetcherComponent;
    private static HttpHandlerComponent httpHandlerComponent;
    public static DataFetcherComponent getDataFetcherComponent(){
        return dataFetcherComponent;
    }
    public static HttpHandlerComponent getHttpHandlerComponent(){
        return httpHandlerComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataFetcherComponent = buildDataFetcherComponent();
        httpHandlerComponent = buildHttpHandlerComponent();
    }
    public DataFetcherComponent buildDataFetcherComponent(){
        return DaggerDataFetcherComponent.builder()
                .dataFetcherModule(new DataFetcherModule())
                .build();
    }

    public HttpHandlerComponent buildHttpHandlerComponent() {
        return DaggerHttpHandlerComponent.builder()
                .httpHandlerModule(new HttpHandlerModule())
                .build();
    }

}
