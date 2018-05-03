package com.grzesica.przemek.artistlist;

import android.app.Application;

import com.grzesica.przemek.artistlist.Module.ArtistListActivityComponent;
import com.grzesica.przemek.artistlist.Module.ArtistListActivityModule;
import com.grzesica.przemek.artistlist.Module.DaggerArtistListActivityComponent;
import com.grzesica.przemek.artistlist.Module.DaggerDataBaseAdapterComponent;
import com.grzesica.przemek.artistlist.Module.DaggerDataFetcherComponent;
import com.grzesica.przemek.artistlist.Module.DaggerHttpHandlerComponent;
import com.grzesica.przemek.artistlist.Module.DataBaseHelperComponent;
import com.grzesica.przemek.artistlist.Module.DataBaseHelperModule;
import com.grzesica.przemek.artistlist.Module.DataFetcherComponent;
import com.grzesica.przemek.artistlist.Module.DataFetcherModule;
import com.grzesica.przemek.artistlist.Module.HttpHandlerComponent;
import com.grzesica.przemek.artistlist.Module.HttpHandlerModule;

/**
 * Created by przemek on 16.04.18.
 */

public class ArtistListApplication extends Application {

    private static ArtistListActivityComponent artistListActivityComponent;
    private static DataBaseHelperComponent dataBaseHelperComponent;
    private static DataFetcherComponent dataFetcherComponent;
    private static HttpHandlerComponent httpHandlerComponent;

    public static ArtistListActivityComponent getArtistListActivityComponent(){
        return artistListActivityComponent;
    }
    public static DataBaseHelperComponent getDataBaseHelperComponent(){
        return dataBaseHelperComponent;
    }
    public static DataFetcherComponent getDataFetcherComponent(){
        return dataFetcherComponent;
    }
    public static HttpHandlerComponent getHttpHandlerComponent(){
        return httpHandlerComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        artistListActivityComponent = buildArtistListActivityComponent();
        dataBaseHelperComponent = buildDataBaseHelperComponent();
        dataFetcherComponent = buildDataFetcherComponent();
        httpHandlerComponent = buildHttpHandlerComponent();
    }
    public DataFetcherComponent buildDataFetcherComponent(){
        return DaggerDataFetcherComponent.builder()
                .dataFetcherModule(new DataFetcherModule(getApplicationContext()))
                .build();
    }

    public HttpHandlerComponent buildHttpHandlerComponent() {
        return DaggerHttpHandlerComponent.builder()
                .httpHandlerModule(new HttpHandlerModule())
                .build();
    }

    public DataBaseHelperComponent buildDataBaseHelperComponent(){
        return DaggerDataBaseAdapterComponent.builder()
                .dataBaseHelperModule(new DataBaseHelperModule(getApplicationContext()))
                .build();
    }

    public ArtistListActivityComponent buildArtistListActivityComponent(){
        return DaggerArtistListActivityComponent.builder()
                .artistListActivityModule(new ArtistListActivityModule(getApplicationContext()))
                .build();
    }

}
