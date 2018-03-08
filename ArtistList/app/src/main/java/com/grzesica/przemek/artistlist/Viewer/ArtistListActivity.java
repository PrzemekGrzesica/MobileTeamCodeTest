package com.grzesica.przemek.artistlist.Viewer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.grzesica.przemek.artistlist.Adapter.ArtistListAdapter;
import com.grzesica.przemek.artistlist.Container.DataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Container.HttpHandlerDIBuilder;
import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.DataFetcher;
import com.grzesica.przemek.artistlist.Model.GetData;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;

public class ArtistListActivity extends AppCompatActivity {

    private ArtistListAdapter mArtistListAdapter;
    private Context mContext;
    private Cursor mCursor;
    private DataBaseAdapter mDataBaseAdapter;
    private ListView mArtistListView;
    private SwipeRefreshLayout swipeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list_activity);
        //todo swipeLayout issue
        /*swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        initUiElements();
                    }
                }, 2000);
            }
        });*/

        mContext = this.getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.artistToolbar);

        setSupportActionBar(toolbar);
        initUiElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.artist_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        UpdatesCheck updatesCheck = new UpdatesCheck(getApplicationContext());
        updatesCheck.execute();
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    protected void initUiElements() {
        mArtistListView = (ListView) findViewById(R.id.artistListView);
        fillListViewData();
        initListViewOnItemClick();
    }

    private void fillListViewData() {
        mDataBaseAdapter = DataBaseAdapter.newInstance(getApplicationContext());
        //Open existing database flag = 0
        mDataBaseAdapter.open(0);
        mCursor = getAllEntriesFromDb(1);
        if (mCursor.moveToFirst() == false) {
            Integer databaseVersion = (Integer) 1;

            HttpHandlerDIBuilder httpHandlerDIBuilder = new HttpHandlerDIBuilder();
            HttpHandler httpHandler = httpHandlerDIBuilder
                                        .byteArrayOutputStream()
                                        .strBuilder()
                                        .build();
//            new GetData(getApplicationContext(), httpHandler).execute(databaseVersion);
//            new GetData(getApplicationContext(), new HttpHandler(new StringBuilder(), new ByteArrayOutputStream())).execute(databaseVersion);
            DataFetcherDIBuilder dataFetcherDIBuilder = new DataFetcherDIBuilder();
            DataFetcher dataFetcher = dataFetcherDIBuilder
                                        .dataBaseAdapter(mContext)
                                        .httpHandlerDIBuilder()
                                        .build();
        }
        mArtistListAdapter = new ArtistListAdapter(getApplicationContext(), mCursor, 0);
        mArtistListView.setAdapter(mArtistListAdapter);
    }

    private Cursor getAllEntriesFromDb(int position) {
        mCursor = mDataBaseAdapter.getArtistListItems();
        if (mCursor != null) {
            startManagingCursor(mCursor);
            mCursor.moveToPosition(--position);
        }
        return mCursor;
    }

    private void initListViewOnItemClick() {
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        Intent intent = new Intent(ArtistListActivity.this,
                                AlbumsListActivity.class);
                        intent.putExtra(AlbumsListActivity.strArtistDataId, id);
                        startActivity(intent);
                    }
                };
        mArtistListView = (ListView) findViewById(R.id.artistListView);
        mArtistListView.setOnItemClickListener(itemClickListener);
    }
}
