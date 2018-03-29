package com.grzesica.przemek.artistlist.Viewer;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.grzesica.przemek.artistlist.Adapter.ArtistListAdapter;
import com.grzesica.przemek.artistlist.Container.DataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.UpdatesCheckDIBuilder;
import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;

public class ArtistListActivity extends AppCompatActivity {

    private IDataBaseAdapter mDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list_activity);

        DataBaseAdapterDIBuilder dataBaseAdapterDIBuilder = new DataBaseAdapterDIBuilder();
        mDataBaseAdapter = dataBaseAdapterDIBuilder
                .contentValues()
                .dataBaseHelperDIBuilder()
                .build(getApplicationContext());

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
//        initUiElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artist_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        UpdatesCheckDIBuilder updatesCheckDIBuilder = new UpdatesCheckDIBuilder();
        UpdatesCheck updatesCheck = updatesCheckDIBuilder
                .dataBaseAdapterDIBUilder()
                .handler()
                .build(getApplicationContext());

        updatesCheck.execute();
        return super.onOptionsItemSelected(item);
    }

    protected void initUiElements() {
        ListView artistListView = (ListView) findViewById(R.id.artistListView);
        fillListViewData(artistListView);
        initListViewOnItemClick(artistListView);
    }

    private void fillListViewData(ListView artistListView) {

        //Open existing database flag = 0
        mDataBaseAdapter.open(0, false);
        Cursor cursor = getAllEntriesFromDb(((DataBaseAdapter)mDataBaseAdapter), 1);
        if (cursor.moveToFirst() == false) {
            Intent intent = new Intent(getApplicationContext(), DataFetchingService.class);
            intent.putExtra(DataFetchingService.STR_MESSAGE, "Please wait for data...");
            getApplicationContext().startService(intent);
        }
        ArtistListAdapter artistListAdapter = new ArtistListAdapter(getApplicationContext(), cursor, 0);
        artistListView.setAdapter(artistListAdapter);
    }

    private Cursor getAllEntriesFromDb(DataBaseAdapter dataBaseAdapter, int position) {
        Cursor cursor = dataBaseAdapter.getArtistListItems();
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
                        Intent intent = new Intent(ArtistListActivity.this,
                                AlbumsListActivity.class);
                        intent.putExtra(AlbumsListActivity.STR_ARTIST_DATA_ID, id);
                        startActivity(intent);
                    }
                };
        artistListView.setOnItemClickListener(itemClickListener);
    }
}
