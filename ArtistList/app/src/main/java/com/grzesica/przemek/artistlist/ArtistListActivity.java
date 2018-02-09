package com.grzesica.przemek.artistlist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.InputStream;
import java.sql.Blob;

public class ArtistListActivity extends AppCompatActivity {

    private ArtistListAdapter artistListAdapter;
    private Cursor dbCursor;
    private DataBaseAdapter dbAdapter;
    private ListView lvArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.artist_list_activity);

//        GetData getData = new GetData(getApplicationContext());
//        getData.execute();

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
                        getAllEntriesFromDb((int) id);
                        String strArtistId = dbCursor.getString(dbCursor.getColumnIndex("artistId"));
                        dbAdapter.close();
                        //todo cursor must read artist table in second activity
//                        String strName = dbCursor.getString(dbCursor.getColumnIndex("name"));
//                        String strGenres = dbCursor.getString(dbCursor.getColumnIndex("genres"));
//                        String strDescription = dbCursor.getString(dbCursor.getColumnIndex("description"));
//                        byte[] blArtistPicture = dbCursor.getBlob(dbCursor.getColumnIndex("artistPictureBlob"));
//
//
                        String strArray[] = {strArtistId};

                        intent.putExtra(AlbumsListActivity.artistDataArray, strArray);
                        startActivity(intent);
                    }
                };

        lvArtist = (ListView) findViewById(R.id.artistListView);
        lvArtist.setOnItemClickListener(itemClickListener);
    }
}
