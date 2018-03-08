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
import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.GetData;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;

public class ArtistListActivity extends AppCompatActivity {

    private ArtistListAdapter artistListAdapter;
    private Context context;
    private Cursor dbCursor;
    private DataBaseAdapter dbAdapter;
    private ListView lvArtist;
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

        context = this.getApplicationContext();
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
        lvArtist = (ListView) findViewById(R.id.artistListView);
        fillListViewData();
        initListViewOnItemClick();
    }

    private void fillListViewData() {
        dbAdapter = DataBaseAdapter.newInstance(getApplicationContext());
        //Open existing database flag = 0
        dbAdapter.open(0);
        dbCursor = getAllEntriesFromDb(1);
        if (dbCursor.moveToFirst() == false) {
            Integer databaseVersion = (Integer) 1;

            DependencyInjectionBuilder depInjBuilder = new DependencyInjectionBuilder();
            HttpHandler httpHandler = depInjBuilder.byteArrayOutputStream().strBuilder().build();
            new GetData(getApplicationContext(), httpHandler).execute(databaseVersion);
//            new GetData(getApplicationContext(), new HttpHandler(new StringBuilder(), new ByteArrayOutputStream())).execute(databaseVersion);
        }
        artistListAdapter = new ArtistListAdapter(getApplicationContext(), dbCursor, 0);
        lvArtist.setAdapter(artistListAdapter);
    }

    private Cursor getAllEntriesFromDb(int position) {
        dbCursor = dbAdapter.getArtistListItems();
        if (dbCursor != null) {
            startManagingCursor(dbCursor);
            dbCursor.moveToPosition(--position);
        }
        return dbCursor;
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
        lvArtist = (ListView) findViewById(R.id.artistListView);
        lvArtist.setOnItemClickListener(itemClickListener);
    }
}
