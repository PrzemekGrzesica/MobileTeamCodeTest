package com.grzesica.przemek.artistlist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class ArtistListActivity extends AppCompatActivity {

    private ArtistListAdapter artistListAdapter;
    private Cursor dbCursor;
    private DataBaseAdapter dbAdapter;
    private ListView lvArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list_activity);
        initUiElements();
    }

    protected void initUiElements() {
        lvArtist = (ListView) findViewById(R.id.artistListView);
        fillListViewData();
        initListViewOnItemClick();
    }

    private void fillListViewData() {
        dbAdapter = new DataBaseAdapter(getApplicationContext());
        dbAdapter.open();
        dbCursor = getAllEntriesFromDb(1);
        if (dbCursor.moveToFirst() == false) {
            GetData getData = new GetData(getApplicationContext());
            getData.execute();
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
