package com.grzesica.przemek.artistlist.Viewer.AlbumsList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.R;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import java.io.ByteArrayInputStream;
import java.io.Closeable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class AlbumsListActivity extends AppCompatActivity {

    public static final String STR_ARTIST_DATA_ID = "artistDataId";

    private ListView mLvAlbums;
    private TextView mTvName;
    private TextView mTvGenres;
    private TextView mTvDescription;
    private ImageView mIvArtist;
    @Inject
    Provider<AsyncTask> mUpdatesCheck;
    @Inject
    Provider<Closeable> mByteArrayInputStream;
    @Inject
    ICursorManager mCursorManager;
    @Inject
    Provider<IGuiContainer> mSingletonGuiCont;
    @Inject
    @Named("AlbumsAdapter")
    Provider<CursorAdapter> mAlbumsListAdapter;
    @Inject
    IGuiContainer mGuiContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_list_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.albumsToolbar);
        setSupportActionBar(toolbar);

        ArtistListApplication.getApplicationComponent().inject(this);

        initUiElements();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Context context = getApplicationContext();
        boolean serviceFlag = ((GuiContainer) mGuiContainer).getFetchingServiceFlag();
        if (serviceFlag == false) {
            UpdatesCheck updatesCheck = (UpdatesCheck) mUpdatesCheck.get();
            updatesCheck.execute();
        } else {
            String text = "Database upgrade is undergoing...";
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initUiElements() {
        mLvAlbums = (ListView) findViewById(R.id.albumsListView);
        mTvName = (TextView) findViewById(R.id.tvAlbumListName);
        mTvGenres = (TextView) findViewById(R.id.tvAlbumListGenres);
        mTvDescription = (TextView) findViewById(R.id.tvAlbumListDescription);
        mIvArtist = (ImageView) findViewById(R.id.albumListArtistImageView);

        long artistDataId = (long) getIntent().getExtras().get(STR_ARTIST_DATA_ID);

        Cursor cursor = mCursorManager.getArtistListCursor((int) artistDataId);

        String strName = cursor.getString(cursor.getColumnIndex("name"));
        String strGenres = cursor.getString(cursor.getColumnIndex("genres"));
        String strDescription = cursor.getString(cursor.getColumnIndex("description"));
        String strArtistId = cursor.getString(cursor.getColumnIndex("artistId"));
        String artistDataArray[] = {strName, strGenres, strDescription};
        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex("artistPictureBlob"));
        GuiContainer singletonGuiCont = (GuiContainer) mSingletonGuiCont.get();
        singletonGuiCont.setImageByteArray(imageByteArray);

        fillUiElements(artistDataArray, imageByteArray);
        fillListView(strArtistId);
    }

    private void fillUiElements(String[] artistData, byte[] imageByteArray) {
        mTvName.setText("Names: " + artistData[0]);
        mTvGenres.setText("Genres: " + artistData[1]);
        mTvDescription.setText(artistData[2]);
        if (imageByteArray != null) {
            ByteArrayInputStream imageStream = (ByteArrayInputStream) mByteArrayInputStream.get();
            Bitmap artistImage = BitmapFactory.decodeStream(imageStream);
            mIvArtist.setImageBitmap(artistImage);
        }
    }

    private void fillListView(String artistId) {
        mCursorManager.getAlbumsListCursor(artistId);
        mLvAlbums.setAdapter(mAlbumsListAdapter.get());
    }

    public void onClickCancel(View view) {
        finish();
    }
}
