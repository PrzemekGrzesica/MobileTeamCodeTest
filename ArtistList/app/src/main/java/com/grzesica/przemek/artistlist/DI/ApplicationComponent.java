package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Model.AlbumsFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.ArtistFetchingRunnable;
import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.AlbumsList.AlbumsListActivity;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.ArtistListActivity;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.ArtistListUI;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities.MySwipeRefreshLayout;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities.SwipeRefreshRunnable;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;
import com.grzesica.przemek.artistlist.Viewer.SettingsActivity.SettingsActivity;

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

    void inject(AlbumsListActivity albumsListActivity);

    void inject(DataBaseManager dataBaseManager);

    void inject(DataBaseHelper dataBaseHelper);

    void inject(DataFetcher dataFetcher);

    void inject(DataFetchingService dataFetchingService);

    void inject(SettingsActivity settingsActivity);

    void inject(ArtistListUI artistListUI);

    AlbumsFetchingRunnable getAlbumsFetchingRunnable();

    ArtistFetchingRunnable getArtistFetchingRunnable();

    IGuiContainer getGuiContainer();

    IDataBaseManager getDataBaseManager();

    MySwipeRefreshLayout getMySwipeRefreshLayout();

    SwipeRefreshRunnable getSwipeRefreshRunnable();

}
