package com.grzesica.przemek.artistlist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class ArtisListAdapter extends CursorAdapter{


    public ArtisListAdapter(Context context, Cursor cursor, int flag){

        super(context, cursor, 0);
    }

    public void bindView(View view, Context context, Cursor cursor){

        TextView tvName = (TextView) view.findViewById(R.id.tvName);

        TextView tvGenres = (TextView) view.findViewById(R.id.tvGenres);

        String strName = cursor.getString(cursor.getColumnIndex("name"));

        String strGenres = cursor.getString(cursor.getColumnIndex("genres"));

        tvName.setText(strName);

        tvGenres.setText(strGenres);

        //todo bind the image to the artistImageView

    }

    public View newView(Context context, Cursor cursor, ViewGroup parent){
        return LayoutInflater.from(context).inflate(R.layout.artist_list_row, parent,false);
    }
}