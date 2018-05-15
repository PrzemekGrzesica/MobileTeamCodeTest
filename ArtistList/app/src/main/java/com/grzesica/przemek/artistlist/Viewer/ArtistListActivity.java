package com.grzesica.przemek.artistlist.Viewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class ArtistListActivity extends AppCompatActivity {

    @Inject
    Provider<AsyncTask> mUpdatesCheck;
    @Inject
    @Named("albumsListActivity")
    Parcelable mIntentAlbumsListActivity;
    @Inject
    @Named("dataFetchingService")
    Provider<Parcelable> mIntentDataFetchingService;
    @Inject
    ICursorManager mCursorManager;
    @Inject
    @Named("ArtistAdapter")
    Provider<CursorAdapter> mArtistListAdapter;
    @Inject
    IGuiContainer mGuiContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list_activity);

        ArtistListApplication.getApplicationComponent().inject(this);

        boolean serviceFlag = ((GuiContainer)mGuiContainer).getServiceFlag();
        boolean activityServiceFlag = ((GuiContainer)mGuiContainer).getActivityServiceFlag();

        if (serviceFlag || activityServiceFlag){
            initUiElements();
        }

        final SwipeRefreshLayout swipeRefLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initUiElements();
                        swipeRefLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.artistToolbar);
        setSupportActionBar(toolbar);

        initUiElements();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean serviceFlag = ((GuiContainer)mGuiContainer).getServiceFlag();
        boolean activityServiceFlag = ((GuiContainer)mGuiContainer).getActivityServiceFlag();
        if (serviceFlag || activityServiceFlag){
            initUiElements();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artist_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        boolean serviceFlag = ((GuiContainer)mGuiContainer).getServiceFlag();
        if (serviceFlag == false) {
            UpdatesCheck updatesCheck = (UpdatesCheck)mUpdatesCheck.get();
            ((GuiContainer)mGuiContainer).setServiceFlag(true);
            updatesCheck.execute();
        }else{
            String text = "Database upgrade is undergoing...";
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initUiElements() {
        ListView artistListView = (ListView) findViewById(R.id.artistListView);
        fillListView(artistListView);
        initListViewOnItemClick(artistListView);
    }

    private void fillListView(ListView artistListView) {
        int cursorPosition = 0;
        Cursor cursor = mCursorManager.getArtistListCursor(cursorPosition);
        artistListView.setAdapter(mArtistListAdapter.get());
        if(cursor==null) {
            Intent intent = (Intent) mIntentDataFetchingService.get();
            intent.putExtra(DataFetchingService.STR_MESSAGE, "Please, wait for data fetching ...");
            getApplicationContext().startService(intent);
        }else {
            artistListView.setAdapter(mArtistListAdapter.get());
        }
    }

    private void initListViewOnItemClick(ListView artistListView) {
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        Intent intent = (Intent)mIntentAlbumsListActivity;
                        intent.putExtra(AlbumsListActivity.STR_ARTIST_DATA_ID, id);

                        boolean serviceFlag = ((GuiContainer)mGuiContainer).getServiceFlag();
                        boolean activityServiceFlag;

                        if (serviceFlag == true){
                            activityServiceFlag = true;
                        }else{
                            activityServiceFlag = false;
                        }
                        ((GuiContainer)mGuiContainer).setActivityServiceFlag(activityServiceFlag);

                        startActivity(intent);
                    }
                };
        artistListView.setOnItemClickListener(itemClickListener);
    }
}
