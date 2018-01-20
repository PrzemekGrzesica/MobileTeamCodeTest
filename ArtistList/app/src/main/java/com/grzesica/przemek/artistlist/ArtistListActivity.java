package com.grzesica.przemek.artistlist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.SimpleCursorAdapter;
import android.widget.CursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ArtistListActivity extends AppCompatActivity {

    private ArtistListAdapter artistListAdapter;
    private Cursor dbCursor;
    private DataBaseAdapter dbAdapter;
    private ListView lvArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_list);

    }

    private void initUiElements() {
        lvArtist = (ListView) findViewById(R.id.artistListView);
    }

    private void initListView() {
        fillListViewData();
        initListViewOnItemClick();
    }

    private void fillListViewData() {
        dbAdapter = new DataBaseAdapter(getApplicationContext());
        dbAdapter.open();
        dbCursor = getAllEntriesFromDb();
        artistListAdapter = new ArtistListAdapter(this, dbCursor, 0);
        lvArtist.setAdapter(artistListAdapter);
    }


    private Cursor getAllEntriesFromDb() {
        dbCursor = dbAdapter.getArtistListItems();
        if (dbCursor != null) {
            startManagingCursor(dbCursor);
            dbCursor.moveToFirst();
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
                        startActivity(intent);
                    }
                };

        lvArtist = (ListView) findViewById(R.id.artistListView);
        lvArtist.setOnItemClickListener(itemClickListener);
    }


}
