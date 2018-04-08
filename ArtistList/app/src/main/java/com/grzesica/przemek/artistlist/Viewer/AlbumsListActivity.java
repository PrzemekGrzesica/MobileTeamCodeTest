package com.grzesica.przemek.artistlist.Viewer;

import android.content.Context;
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

import com.grzesica.przemek.artistlist.Adapter.AlbumsListAdapter;
import com.grzesica.przemek.artistlist.Container.DataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.UpdatesCheckDIBuilder;
import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.R;

import java.io.ByteArrayInputStream;

public class AlbumsListActivity extends AppCompatActivity {

    public static final String STR_ARTIST_DATA_ID = "artistDataId";

    private ListView mLvAlbums;
    private TextView mTvName;
    private TextView mTvGenres;
    private TextView mTvDescription;
    private ImageView mIvArtist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.albumsToolbar);
        setSupportActionBar(toolbar);

        long artistDataId = (long) getIntent().getExtras().get(STR_ARTIST_DATA_ID);

        DataBaseAdapterDIBuilder depInjBuilder = new DataBaseAdapterDIBuilder();
        IDataBaseAdapter dataBaseAdapter = depInjBuilder
                .contentValues()
                .dataBaseHelperDIBuilder()
                .build(getApplicationContext());
        //Open existing database - VersionFlag = 0
        dataBaseAdapter.open(0, false);

        Cursor cursor = getArtistTable((int) artistDataId, dataBaseAdapter);

        String strName = cursor.getString(cursor.getColumnIndex("name"));
        String strGenres = cursor.getString(cursor.getColumnIndex("genres"));
        String strDescription = cursor.getString(cursor.getColumnIndex("description"));
        String strArtistId = cursor.getString(cursor.getColumnIndex("artistId"));
        String artistDataArray[] = {strName, strGenres, strDescription};
        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex("artistPictureBlob"));

        initUiElements();
        fillUiElements(artistDataArray, imageByteArray);
        fillListView(strArtistId, dataBaseAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        if (ArtistListActivity.serviceFlag == false) {
            UpdatesCheckDIBuilder updatesCheckDIBuilder = new UpdatesCheckDIBuilder();
            UpdatesCheck updatesCheck = updatesCheckDIBuilder
                    .dataBaseAdapterDIBUilder()
                    .handler()
                    .build(context);
            updatesCheck.execute();
        }else{
            String text = "Database upgrade is undergoing...";
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private Cursor getArtistTable(int position, IDataBaseAdapter dataBaseAdapter) {
        Cursor cursor = ((DataBaseAdapter)dataBaseAdapter).getArtistListItems();
        if (cursor != null) {
            startManagingCursor(cursor);
            cursor.moveToPosition(--position);
        }
        return cursor;
    }

    private void initUiElements() {
        mLvAlbums = (ListView) findViewById(R.id.albumsListView);
        mTvName = (TextView) findViewById(R.id.tvAlbumListName);
        mTvGenres = (TextView) findViewById(R.id.tvAlbumListGenres);
        mTvDescription = (TextView) findViewById(R.id.tvAlbumListDescription);
        mIvArtist = (ImageView) findViewById(R.id.albumListArtistImageView);
    }

    private void fillUiElements(String[] artistData, byte[] imageByteArray){
        mTvName.setText("Names: " + artistData[0]);
        mTvGenres.setText("Genres: " + artistData[1]);
        mTvDescription.setText(artistData[2]);
        if (imageByteArray!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
            Bitmap artistImage = BitmapFactory.decodeStream(imageStream);
            mIvArtist.setImageBitmap(artistImage);
        }
    }

    private void fillListView(String artistId, IDataBaseAdapter dataBaseAdapter) {
        Cursor cursor = getAlbumTable(artistId, dataBaseAdapter);
        AlbumsListAdapter albumsListAdapter = new AlbumsListAdapter(getApplicationContext(), cursor, 0);
        mLvAlbums.setAdapter(albumsListAdapter);
    }

    private Cursor getAlbumTable(String position, IDataBaseAdapter dataBaseAdapter) {
        Cursor cursor = ((DataBaseAdapter)dataBaseAdapter).getAlbumsListItems(position);
        if (cursor != null) {
            startManagingCursor(cursor);
            cursor.moveToFirst();
        }
        return cursor;
    }
}
