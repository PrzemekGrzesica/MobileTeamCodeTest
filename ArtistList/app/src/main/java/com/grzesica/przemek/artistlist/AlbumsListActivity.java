package com.grzesica.przemek.artistlist;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class AlbumsListActivity extends AppCompatActivity {

    public static final String strArtistDataId = "artistDataId";

    private Cursor dbCursor;
    private DataBaseAdapter dbAdapter;
    private ListView lvAlbums;
    private TextView tvName;
    private TextView tvGenres;
    private TextView tvDescription;
    private ImageView ivArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.albumsToolbar);
        setSupportActionBar(toolbar);

        long artistDataId = (long) getIntent().getExtras().get(strArtistDataId);
        dbAdapter = new DataBaseAdapter(getApplicationContext());
        dbAdapter.open();

        getArtistTable((int) artistDataId);

        String strName = dbCursor.getString(dbCursor.getColumnIndex("name"));
        String strGenres = dbCursor.getString(dbCursor.getColumnIndex("genres"));
        String strDescription = dbCursor.getString(dbCursor.getColumnIndex("description"));
        String strArtistId = dbCursor.getString(dbCursor.getColumnIndex("artistId"));
        String artistDataArray[] = {strName, strGenres, strDescription};
        byte[] imageByteArray = dbCursor.getBlob(dbCursor.getColumnIndex("artistPictureBlob"));

        initUiElements();
        fillUiElements(artistDataArray, imageByteArray);

        fillListView(strArtistId);
    }
    private Cursor getArtistTable(int position) {
        dbCursor = dbAdapter.getArtistListItems();
        if (dbCursor != null) {
            startManagingCursor(dbCursor);
            dbCursor.moveToPosition(--position);
        }
        return dbCursor;
    }
    private void initUiElements() {
        lvAlbums = (ListView) findViewById(R.id.albumsListView);
        tvName = (TextView) findViewById(R.id.tvAlbumListName);
        tvGenres = (TextView) findViewById(R.id.tvAlbumListGenres);
        tvDescription = (TextView) findViewById(R.id.tvAlbumListDescription);
        ivArtist = (ImageView) findViewById(R.id.albumListArtistImageView);
    }
    private void fillUiElements(String[] artistData, byte[] imageByteArray){
        tvName.setText("Names: " + artistData[0]);
        tvGenres.setText("Genres: " + artistData[1]);
        tvDescription.setText(artistData[2]);
        if (imageByteArray!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
            Bitmap artistImage = BitmapFactory.decodeStream(imageStream);
            ivArtist.setImageBitmap(artistImage);
        }
    }

    private void fillListView(String artistId) {

        dbCursor = getAlbumTable(artistId);
        AlbumsListAdapter albumsListAdapter = new AlbumsListAdapter(getApplicationContext(), dbCursor, 0);
        lvAlbums.setAdapter(albumsListAdapter);
    }

    private Cursor getAlbumTable(String position) {
        dbCursor = dbAdapter.getAlbumsListItems(position);
        if (dbCursor != null) {
            startManagingCursor(dbCursor);
            dbCursor.moveToFirst();
        }
        return dbCursor;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        UpdatesCheck updatesCheck = new UpdatesCheck(getApplicationContext());
        updatesCheck.execute();
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
