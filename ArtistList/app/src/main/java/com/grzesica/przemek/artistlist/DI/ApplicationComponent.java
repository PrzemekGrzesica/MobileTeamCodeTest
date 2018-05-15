package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Adapter.AlbumsListAdapter;
import com.grzesica.przemek.artistlist.Adapter.ArtistListAdapter;
import com.grzesica.przemek.artistlist.Adapter.CursorManager;
import com.grzesica.przemek.artistlist.Model.AlbumsFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.ArtistFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.MD5checkSum;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.AlbumsListActivity;
import com.grzesica.przemek.artistlist.Viewer.ArtistListActivity;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;
import com.grzesica.przemek.artistlist.Viewer.SettingsActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by przemek on 28.04.18.
 */
@Component(modules = {AlbumsFetchingRunnableModule.class, AlbumsListAdapterModule.class, ApplicationModule.class, ArtistFetchingRunnableModule.class,
        ArtistListAdapterModule.class, ContextModule.class, CursorManagerModule.class, DataBaseManagerModule.class, DataFetcherModule.class,
        ExtendedHandlerModule.class, HttpHandlerModule.class, MD5checkSumModule.class, GuiContModule.class, UpdatesCheckModule.class})
@Singleton
public interface ApplicationComponent {

    void inject(ArtistListActivity artistListActivity);

    void inject(ArtistListAdapter artistListAdapter);

    void inject(AlbumsListActivity albumsListActivity);

    void inject(AlbumsListAdapter albumsListAdapter);

    void inject(MD5checkSum md5checkSum);

    void inject(CursorManager cursorManager);

    void inject(DataBaseManager dataBaseManager);

    void inject(DataBaseHelper dataBaseHelper);

    void inject(DataFetcher dataFetcher);

    void inject(DataFetchingService dataFetchingService);

    void inject(HttpHandler httpHandler);

    void inject(UpdatesCheck updatesCheck);

    void inject(SettingsActivity settingsActivity);

    IDataBaseManager getDataBaseManager();

    AlbumsListAdapter getAlbumsListAdapter();

    ArtistListAdapter getArtistListAdapter();

    IGuiContainer getSingletonGuiCont();

    ArtistFetchingRunnable getArtistFetchingRunnable();

    AlbumsFetchingRunnable getAlbumsFetchingRunnable();

}
