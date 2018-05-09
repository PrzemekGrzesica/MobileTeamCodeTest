package com.grzesica.przemek.artistlist.Adapter;

import android.database.Cursor;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;

import javax.inject.Inject;

public class CursorManager implements ICursorManager{

    @Inject
    IDataBaseManager mDataBaseManager;

    public CursorManager(){
        ArtistListApplication.getApplicationComponent().inject(this);}

    @Override
    public Cursor getCursor(){
        DataBaseManager dataBaseManager = (DataBaseManager) mDataBaseManager;
        dataBaseManager.open();
        try {
            Cursor cursor = dataBaseManager.getArtistListRecords();
//            startManagingCursor(cursor);
            int position = 1;
            cursor.moveToPosition(--position);
            return cursor;
        }catch (Exception e){
            return null;
        }
    }
}
