package com.grzesica.przemek.artistlist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by przemek on 02.02.18.
 */

public class AlbumsListAdapter extends CursorAdapter {


    public AlbumsListAdapter(Context context, Cursor cursor, int flag){
        super(context, cursor, 0);
    }

    public void bindView(View view, Context context, Cursor cursor){

        TextView tvTitle = (TextView) view.findViewById(R.id.albumTitle);

        String strTitle = cursor.getString(cursor.getColumnIndex("albumTitle"));

        tvTitle.setText(strTitle);


        //todo bind the image to the artistImageView

    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.album_list_row, parent,false);
    }
}