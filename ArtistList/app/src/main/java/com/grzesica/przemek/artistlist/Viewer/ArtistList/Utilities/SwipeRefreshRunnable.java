package com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities;

import android.widget.ListView;

import com.grzesica.przemek.artistlist.Viewer.ArtistList.ArtistListUI;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.IArtistListUI;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SwipeRefreshRunnable implements Runnable {

    private ArtistListUI mArtistListUI;
    private ListView mArtistListView;

    @Inject
    public SwipeRefreshRunnable(IArtistListUI artistListUI) {
        this.mArtistListUI = (ArtistListUI) artistListUI;
    }

    @Override
    public void run() {
        mArtistListUI.initUiElements(mArtistListView);
    }

    public void setArtistListView(ListView artistListView) {
        this.mArtistListView = artistListView;
    }
}
