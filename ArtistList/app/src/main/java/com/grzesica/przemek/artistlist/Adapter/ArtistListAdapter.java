package com.grzesica.przemek.artistlist.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.grzesica.przemek.artistlist.R;

import java.io.ByteArrayInputStream;

public class ArtistListAdapter extends CursorAdapter{


    public ArtistListAdapter(Context context, Cursor cursor, int flag){
        super(context, cursor, 0);
    }

    public void bindView(View view, Context context, Cursor cursor){

        TextView tvName = (TextView) view.findViewById(R.id.tvArtistListName);
        TextView tvGenres = (TextView) view.findViewById(R.id.tvArtistListGenres);
        ImageView ivArtist = (ImageView) view.findViewById(R.id.ivArtistImage);

        String strName = cursor.getString(cursor.getColumnIndex("name"));
        String strGenres = cursor.getString(cursor.getColumnIndex("genres"));

        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex("artistPictureBlob"));
        if (imageByteArray!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
            Bitmap artistImage = BitmapFactory.decodeStream(imageStream);
            ivArtist.setImageBitmap(artistImage);
        }
        tvName.setText(strName);
        tvGenres.setText(strGenres);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.artist_list_row, parent,false);
    }
}