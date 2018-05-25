package com.grzesica.przemek.artistlist.Viewer.ArtistList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities.IMySwipeRefreshLayout;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;
import com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities.SwipeRefreshRunnable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class ArtistListActivity extends AppCompatActivity {

    @Inject
    IGuiContainer mGuiContainer;
    @Inject
    Provider<AsyncTask> mUpdatesCheck;
    @Inject
    ICursorManager mCursorManager;
    @Inject
    IArtistListUI mArtistListUI;
    @Inject
    @Named("swipeRefresh")
    Runnable mSwipeRefreshRunnable;
    @Inject
    IMySwipeRefreshLayout mMySwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list_activity);

        ArtistListApplication.getApplicationComponent().inject(this);

        ListView artistListView = (ListView) findViewById(R.id.artistListView);
        SwipeRefreshLayout swipeRefLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        ((SwipeRefreshRunnable) mSwipeRefreshRunnable).setArtistListView(artistListView);


        mMySwipeRefreshLayout.setSwipeRefreshLayout(swipeRefLayout);
        swipeRefLayout.setOnRefreshListener(mMySwipeRefreshLayout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.artistToolbar);
        setSupportActionBar(toolbar);

        mArtistListUI.initUiElements(artistListView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artist_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean serviceFlag = mGuiContainer.getFetchingServiceFlag();
        if (serviceFlag == false) {
            UpdatesCheck updatesCheck = (UpdatesCheck) mUpdatesCheck.get();
            updatesCheck.execute();
        } else {
            String text = "Database upgrade is undergoing...";
            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
