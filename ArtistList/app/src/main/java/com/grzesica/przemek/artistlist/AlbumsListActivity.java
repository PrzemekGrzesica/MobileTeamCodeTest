package com.grzesica.przemek.artistlist;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class AlbumsListActivity extends AppCompatActivity {

    public static final String artistDataArray = "artistData";

    private Cursor dbCursor;
    private DataBaseAdapter dbAdapter;
    private ListView lvAlbums;
    private TextView tvName;
    private TextView tvGenres;
    private TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_activity);

        String artistData[] = (String[]) getIntent().getExtras().get(artistDataArray);

        initUiElements();
        fillUiElements(artistData);

        fillListView(artistData[0]);
    }
    private void initUiElements() {
        lvAlbums = (ListView) findViewById(R.id.albumsListView);
        tvName = (TextView) findViewById(R.id.tvAlbumListName);
        tvGenres = (TextView) findViewById(R.id.tvAlbumListGenres);
        tvDescription = (TextView) findViewById(R.id.tvAlbumListDescription);
    }
    private void fillUiElements(String... artistData){
        tvName.setText(artistData[1]);
        tvGenres.setText(artistData[2]);
        tvDescription.setText(artistData[3]);
    }

    private void fillListView(String artistId) {
        dbAdapter = new DataBaseAdapter(getApplicationContext());
        dbAdapter.open();
        dbCursor = getAllEntriesFromDb(artistId);
        AlbumsListAdapter albumsListAdapter = new AlbumsListAdapter(getApplicationContext(), dbCursor, 0);
        lvAlbums.setAdapter(albumsListAdapter);
    }


    private Cursor getAllEntriesFromDb(String position) {
        dbCursor = dbAdapter.getAlbumsListItems(position);
        if (dbCursor != null) {
            startManagingCursor(dbCursor);
            dbCursor.moveToFirst();
        }
        return dbCursor;
    }
}
