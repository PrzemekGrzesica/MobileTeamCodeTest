package com.grzesica.przemek.artistlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by przemek on 22.11.17.
 * SQLHelper for creation two types of databases (author list and album list of author)
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    DataBaseHelper(Context context, String dbName, int dbVersion){
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String authListString = "CREATE TABLE ARTIST_LIST(_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
                                + "NAME TEXT, DESCRIPTION TEXT, GENRES TEXT, "
                                + "AUTHOR_IMAGE_ID INTEGER);";
//        String albumListString = "CREATE TABLE ALBUM_LIST(_id INTEGER PRIMARY KEY AUTOINCREMENT ,"
//                + "TITLE TEXT, ALBUM_IMAGE_ID INTEGER);";
        db.execSQL(authListString);
        /*for (int i : i < 1
             ) {
            
        }
        insertArtist.(db, strName, strDescr, strGenres, R.drawable.?);*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
