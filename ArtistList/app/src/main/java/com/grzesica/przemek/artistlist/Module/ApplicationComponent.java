package com.grzesica.przemek.artistlist.Module;

import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.Viewer.AlbumsListActivity;
import com.grzesica.przemek.artistlist.Viewer.ArtistListActivity;

import dagger.Component;

/**
 * Created by przemek on 28.04.18.
 */
@Component(modules = {ApplicationModule.class, ContextModule.class})
public interface ApplicationComponent {

    void inject(ArtistListActivity artistListActivity);

    void inject(AlbumsListActivity albumsListActivity);

    void inject(DataBaseManager dataBaseManager);

//    void inject(DataBaseHelper dataBaseHelper);

    void inject(HttpHandler httpHandler);

    void inject(UpdatesCheck updatesCheck);

    void inject(DataFetcher dataFetcher);
}
