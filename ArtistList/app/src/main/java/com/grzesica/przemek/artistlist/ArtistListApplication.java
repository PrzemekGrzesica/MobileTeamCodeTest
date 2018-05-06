package com.grzesica.przemek.artistlist;

import android.app.Application;

import com.grzesica.przemek.artistlist.Module.ApplicationComponent;
import com.grzesica.przemek.artistlist.Module.ApplicationModule;
import com.grzesica.przemek.artistlist.Module.ContextModule;
import com.grzesica.przemek.artistlist.Module.DaggerApplicationComponent;

/**
 * Created by przemek on 16.04.18.
 */

public class ArtistListApplication extends Application {

//    private static AlbumsListActivityComponent albumsListActivityComponent;
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
                .contextModule(new ContextModule(this))
                .build();
    }

//    public UpdatesCheckComponent buildUpdatesCheckComponent(){
//        return DaggerUpdatesCheckComponent.builder()
//                .updatesCheckModule(new UpdatesCheckModule())
//                .contextModule(new ContextModule(this))
//                .build();
//    }


}
