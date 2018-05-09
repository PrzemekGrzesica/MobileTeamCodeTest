package com.grzesica.przemek.artistlist.Application;

import android.app.Application;

import com.grzesica.przemek.artistlist.DI.ApplicationComponent;
import com.grzesica.przemek.artistlist.DI.ApplicationModule;
import com.grzesica.przemek.artistlist.DI.ContextModule;
import com.grzesica.przemek.artistlist.DI.DaggerApplicationComponent;
import com.grzesica.przemek.artistlist.DI.DataFetcherModule;

/**
 * Created by przemek on 16.04.18.
 */

public class ArtistListApplication extends Application {

    private static ApplicationComponent applicationComponent;

    public static ApplicationComponent getApplicationComponent(){
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = buildApplicationComponent();
    }

    public ApplicationComponent buildApplicationComponent(){
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule())
                .dataFetcherModule(new DataFetcherModule())
                .contextModule(new ContextModule(this))
                .build();
    }
}
