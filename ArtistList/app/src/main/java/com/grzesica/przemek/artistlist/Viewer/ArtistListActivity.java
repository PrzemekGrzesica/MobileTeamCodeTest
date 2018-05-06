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
import android.widget.ListView;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Adapter.ArtistListAdapter;
import com.grzesica.przemek.artistlist.ArtistListApplication;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;

import javax.inject.Inject;
import javax.inject.Named;

public class ArtistListActivity extends AppCompatActivity {

    public static boolean serviceFlag = false;
    public static boolean activityServiceFlag = false;
    @Inject
    AsyncTask mUpdatesCheck;
    @Inject
    IDataBaseManager mDataBaseManager;
    @Inject
    @Named("intent")
    Parcelable mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list_activity);

        ArtistListApplication.getApplicationComponent().inject(this);

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
        if (serviceFlag == false) {
            UpdatesCheck updatesCheck = (UpdatesCheck)mUpdatesCheck;
            updatesCheck.execute();
        }else{
            String text = "Database upgrade is undergoing...";
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void initUiElements() {
        ListView artistListView = (ListView) findViewById(R.id.artistListView);
        fillListViewData(artistListView);
        initListViewOnItemClick(artistListView);
    }

    private void fillListViewData(ListView artistListView) {
        DataBaseManager dataBaseManager = (DataBaseManager) mDataBaseManager;
        //Open existing database flag = 0
        dataBaseManager.open();
        Cursor cursor = getAllEntriesFromDb(dataBaseManager, 1);
        if (cursor.moveToFirst() == false) {
            Intent intent = new Intent(getApplicationContext(), DataFetchingService.class);
            intent.putExtra(DataFetchingService.STR_MESSAGE, "Please, wait for data fetching ...");
            getApplicationContext().startService(intent);
        }
        ArtistListAdapter artistListAdapter = new ArtistListAdapter(getApplicationContext(), cursor, 0);
        artistListView.setAdapter(artistListAdapter);
    }

    private Cursor getAllEntriesFromDb(DataBaseManager dataBaseManager, int position) {
        Cursor cursor = dataBaseManager.getArtistListRecords();
        if (cursor != null) {
            startManagingCursor(cursor);
            cursor.moveToPosition(--position);
        }
        return cursor;
    }

    private void initListViewOnItemClick(ListView artistListView) {
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        Intent intent = (Intent)mIntent;
                        intent.putExtra(AlbumsListActivity.STR_ARTIST_DATA_ID, id);
                        if (serviceFlag == true){
                            activityServiceFlag = true;
                        }else{
                            activityServiceFlag = false;
                        }
                        startActivity(intent);
                    }
                };
        artistListView.setOnItemClickListener(itemClickListener);
    }
}
