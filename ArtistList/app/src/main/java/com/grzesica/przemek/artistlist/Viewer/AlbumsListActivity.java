package com.grzesica.przemek.artistlist.Viewer;

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

import com.grzesica.przemek.artistlist.Adapter.AlbumsListAdapter;
import com.grzesica.przemek.artistlist.Container.DataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.IUpdatesCheckDIBuilder;
import com.grzesica.przemek.artistlist.Container.UpdatesCheckDIBuilder;
import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;

import java.io.ByteArrayInputStream;

public class AlbumsListActivity extends AppCompatActivity {

    public static final String STR_ARTIST_DATA_ID = "artistDataId";

    private Cursor mCursor;
    private IDataBaseAdapter mDataBaseAdapter;
    private ListView mLvAlbums;
    private TextView mTvName;
    private TextView mTvGenres;
    private TextView mTvDescription;
    private ImageView mIvArtist;
    private IUpdatesCheckDIBuilder mUpdatesCheckDIBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.albumsToolbar);
        setSupportActionBar(toolbar);

        long artistDataId = (long) getIntent().getExtras().get(STR_ARTIST_DATA_ID);

        DataBaseAdapterDIBuilder depInjBuilder = new DataBaseAdapterDIBuilder();

        mDataBaseAdapter = depInjBuilder
                .contentValues()
                .dataBaseHelperDIBuilder()
                .build(getApplicationContext());
        //Open existing database - VersionFlag = 0
        mDataBaseAdapter.open(0, false);

        getArtistTable((int) artistDataId);

        String strName = mCursor.getString(mCursor.getColumnIndex("name"));
        String strGenres = mCursor.getString(mCursor.getColumnIndex("genres"));
        String strDescription = mCursor.getString(mCursor.getColumnIndex("description"));
        String strArtistId = mCursor.getString(mCursor.getColumnIndex("artistId"));
        String artistDataArray[] = {strName, strGenres, strDescription};
        byte[] imageByteArray = mCursor.getBlob(mCursor.getColumnIndex("artistPictureBlob"));

        initUiElements();
        fillUiElements(artistDataArray, imageByteArray);

        fillListView(strArtistId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_list_menu, menu);
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
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    private Cursor getArtistTable(int position) {
        mCursor =((DataBaseAdapter)mDataBaseAdapter).getArtistListItems();
        if (mCursor != null) {
            startManagingCursor(mCursor);
            mCursor.moveToPosition(--position);
        }
        return mCursor;
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

    private void fillListView(String artistId) {

        mCursor = getAlbumTable(artistId);
        AlbumsListAdapter albumsListAdapter = new AlbumsListAdapter(getApplicationContext(), mCursor, 0);
        mLvAlbums.setAdapter(albumsListAdapter);
    }

    private Cursor getAlbumTable(String position) {
//        DataBaseAdapter dataBaseAdapter = mDataBaseAdapter;
        mCursor = ((DataBaseAdapter)mDataBaseAdapter).getAlbumsListItems(position);
        if (mCursor != null) {
            startManagingCursor(mCursor);
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
