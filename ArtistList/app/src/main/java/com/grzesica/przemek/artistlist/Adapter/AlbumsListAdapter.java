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

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by przemek on 02.02.18.
 */

public class AlbumsListAdapter extends CursorAdapter {

    @Inject
    public AlbumsListAdapter(Context context,  @Named("AlbumsListCursor") Cursor cursor){
        super(context, cursor, 0);
    }

    public void bindView(View view, Context context, Cursor cursor){

        TextView tvTitle = (TextView) view.findViewById(R.id.albumTitle);
        ImageView ivAlbum = (ImageView) view.findViewById(R.id.ivAlbumImage);

        String strTitle = cursor.getString(cursor.getColumnIndex("albumTitle"));

        byte[] imageByteArray = cursor.getBlob(cursor.getColumnIndex("albumPictureBlob"));
        if (imageByteArray!=null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
            Bitmap albumImage = BitmapFactory.decodeStream(imageStream);
            ivAlbum.setImageBitmap(albumImage);
        }
        tvTitle.setText(strTitle);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.album_list_row, parent,false);
    }
}