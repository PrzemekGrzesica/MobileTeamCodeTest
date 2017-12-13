package com.grzesica.przemek.artistlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by przemek on 22.11.17.
 * SQLHelper for creation two types of databases (author list and album list of author)
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "artistDatabase";

    // Table Names
    private static final String TABLE_ARTIST_LIST = "artist";
    private static final String TABLE_ALBUM_LIST = "albums";

    // Common column names
    private static final String KEY_ARTIST_ID = "artistId";

    // TABLE_ARTIST_LIST - column names
    private static final String KEY_GENRES = "genres";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ARTIST_PICTURE_URL = "artistPictureUrl";
    private static final String KEY_ARTIST_PICTURE = "artistPicture";

    // TABLE_ALBUM_LIST - column names
    private static final String KEY_ALBUM_ID = "albumId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ALBUM_PICTURE = "albumPicture";
    private static final String KEY_ALBUM_PICTURE_URL = "albumPictureUrl";

    // TABLE_ARTIST_LIST - table create statement
    private static final String CREATE_TABLE_ARTIST_LIST = "CREATE TABLE "
            + TABLE_ARTIST_LIST + "(" + KEY_ARTIST_ID + " INTEGER PRIMARY KEY," + KEY_GENRES
            + " TEXT," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT"
            + KEY_ARTIST_PICTURE_URL + " TEXT," + KEY_ARTIST_PICTURE + " BLOB" + ")";

    // TABLE_ALBUM_LIST - table create statement
    private static final String CREATE_TABLE_ALBUM_LIST = "CREATE TABLE "
            + TABLE_ALBUM_LIST + "(" + KEY_ALBUM_ID + " INTEGER PRIMARY KEY," + KEY_ARTIST_ID
            + " TEXT," + KEY_TITLE + " TEXT," + KEY_TYPE + " TEXT"
            + KEY_ALBUM_PICTURE_URL + "TEXT," + KEY_ALBUM_PICTURE + " BLOB" + ")";

    DataBaseHelper(Context context, String dbName, int dbVersion){
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Creating required tables
        db.execSQL(CREATE_TABLE_ARTIST_LIST);
        db.execSQL(CREATE_TABLE_ALBUM_LIST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // 0n upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM_LIST);

        // Create new tables
        onCreate(db);
    }
}
