package com.grzesica.przemek.artistlist.Viewer.ArtistList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.grzesica.przemek.artistlist.Adapter.CursorManager;
import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.AlbumsList.AlbumsListActivity;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class ArtistListUI implements IArtistListUI {

    private Context mContext;
    private CursorManager mCursorManager;
    //    private GuiContainer mGuiContainer;
    @Inject
    @Named("dataFetchingService")
    Provider<Parcelable> mIntentDataFetchingService;
    @Inject
    @Named("ArtistAdapter")
    Provider<CursorAdapter> mArtistListAdapter;
    @Inject
    @Named("albumsListActivity")
    Parcelable mIntentAlbumsListActivity;

    @Inject
    public ArtistListUI(Context context, ICursorManager cursorManager) {
        this.mCursorManager = (CursorManager) cursorManager;
        this.mContext = context;
        ArtistListApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void initUiElements(ListView artistListView) {
        fillListView(artistListView);
        initListViewOnItemClick(artistListView);
    }

    private void fillListView(ListView artistListView) {
        int cursorPosition = 0;
        Cursor cursor = mCursorManager.getArtistListCursor(cursorPosition);
        artistListView.setAdapter(mArtistListAdapter.get());
        if (cursor == null) {
            Intent intent = (Intent) mIntentDataFetchingService.get();
            intent.putExtra(DataFetchingService.STR_MESSAGE, "Please, wait for data fetching ...");
            mContext.startService(intent);
        } else {
            artistListView.setAdapter(mArtistListAdapter.get());
        }
    }

    private void initListViewOnItemClick(ListView artistListView) {
        AdapterView.OnItemClickListener itemClickListener =
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> listView,
                                            View v,
                                            int position,
                                            long id) {
                        Intent intent = (Intent) mIntentAlbumsListActivity;
                        intent.putExtra(AlbumsListActivity.STR_ARTIST_DATA_ID, id);
                        mContext.startActivity(intent);
                    }
                };
        artistListView.setOnItemClickListener(itemClickListener);
    }
}