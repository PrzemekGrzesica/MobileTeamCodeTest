package com.grzesica.przemek.artistlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by przemek on 22.11.17.
 * SQLHelper for creation two types of tables (authors list table and albums list of an author table).
 * Internal static class has been created to avoid an implicit reference to an external class.
 */

public class DataBaseAdapter {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "artistDatabase.db";

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
    private static final String KEY_ARTIST_PICTURE_PATH = "artistPicturePath";

    // TABLE_ALBUM_LIST - column names
    private static final String KEY_ALBUM_ID = "albumId";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ALBUM_PICTURE_PATH = "albumPicturePath";
    private static final String KEY_ALBUM_PICTURE_URL = "albumPictureUrl";

    // TABLE_ARTIST_LIST - table create statement
    private static final String CREATE_TABLE_ARTIST_LIST = "CREATE TABLE "
            + TABLE_ARTIST_LIST + "(" + KEY_ARTIST_ID + " INTEGER PRIMARY KEY," + KEY_GENRES
            + " TEXT," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT"
            + KEY_ARTIST_PICTURE_URL + " TEXT," + KEY_ARTIST_PICTURE_PATH + " BLOB" + ")";

    // TABLE_ALBUM_LIST - table create statement
    private static final String CREATE_TABLE_ALBUM_LIST = "CREATE TABLE "
            + TABLE_ALBUM_LIST + "(" + KEY_ALBUM_ID + " INTEGER PRIMARY KEY," + KEY_ARTIST_ID
            + " TEXT," + KEY_TITLE + " TEXT," + KEY_TYPE + " TEXT"
            + KEY_ALBUM_PICTURE_URL + "TEXT," + KEY_ALBUM_PICTURE_PATH + " BLOB" + ")";

    private SQLiteDatabase db;
    private Context context;
    private DataBaseHelper dbHelper;

    public DataBaseAdapter(Context context){
        this.context = context;
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context, String dbName, CursorFactory factory, int dbVersion) {
            super(context, dbName, factory, dbVersion);
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

    public DataBaseAdapter open(){
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public DataBaseAdapter close(){
        dbHelper.close();
    }

    public long createArtistListRecords(String artistId, String genres, String artistPictureUrl,
                                        String name, String description) {

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST_ID, artistId);
        values.put(KEY_GENRES, genres);
        values.put(KEY_ARTIST_PICTURE_URL, artistPictureUrl);
//       ToDo: Should be written a path to a file stored in internal memory
//        values.put(KEY_ARTIST_PICTURE_PATH, getPicturePath(artistPictureUrl);
        values.put(KEY_NAME, name);
        values.put(KEY_DESCRIPTION, description);

        return db.insert(TABLE_ARTIST_LIST, null, values);
    }

    public long createAlbumListRecords(String artistId, String albumId, String title,
                                       String type, String albumPictureUrl) {

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST_ID, artistId);
        values.put(KEY_ALBUM_ID, albumId);
        values.put(KEY_TITLE, title);
        values.put(KEY_TYPE, type);
        values.put(KEY_ALBUM_PICTURE_URL, albumPictureUrl);
//       ToDo: Should be written a path to a file stored in internal memory
//        values.put(KEY_ALBUM_PICTURE_PATH, getPicturePath(albumPictureUrl);

        return db.insert(TABLE_ARTIST_LIST, null, values);
    }
}
