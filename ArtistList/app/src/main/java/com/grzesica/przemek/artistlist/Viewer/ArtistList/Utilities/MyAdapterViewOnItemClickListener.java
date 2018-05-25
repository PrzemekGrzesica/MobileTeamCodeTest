package com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.grzesica.przemek.artistlist.Viewer.AlbumsList.AlbumsListActivity;

import javax.inject.Inject;
import javax.inject.Named;

public class MyAdapterViewOnItemClickListener implements OnItemClickListener {

    private Intent mIntentAlbumsListActivity;
    private Context mContext;

    @Inject
    public MyAdapterViewOnItemClickListener(Context context, @Named("albumsListActivity") Parcelable intentAlbumsListActivity){
        this.mIntentAlbumsListActivity = (Intent) intentAlbumsListActivity;
        this.mContext = context;
    }

    @Override
    public void onItemClick(AdapterView<?> listView, View v, int position, long id) {
        mIntentAlbumsListActivity.putExtra(AlbumsListActivity.STR_ARTIST_DATA_ID, id);
        mContext.startActivity(mIntentAlbumsListActivity);
    }
}
