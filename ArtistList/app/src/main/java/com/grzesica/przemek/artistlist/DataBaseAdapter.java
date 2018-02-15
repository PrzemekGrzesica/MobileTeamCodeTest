package com.grzesica.przemek.artistlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by przemek on 22.11.17.
 * SQLHelper for creation three types of tables (artist list table, albums list of an artist table, MD5 sum of json file table).
 * Internal static class has been created to avoid an implicit reference to an external class.
 */

public class DataBaseAdapter {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "artistDb.db";

    // Table Names
    private static final String TABLE_ARTIST_LIST = "artist";
    private static final String TABLE_ALBUM_LIST = "albums";
    private static final String TABLE_MD5_KEYS = "md5Keys";

    // Common column names
    private static final String _id = "_id";
    private static final String KEY_ARTIST_ID = "artistId";

    // TABLE_ARTIST_LIST - column names
    private static final String KEY_GENRES = "genres";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ARTIST_PICTURE_URL = "artistPictureUrl";
    private static final String KEY_ARTIST_PICTURE_BLOB = "artistPictureBlob";

    // TABLE_ALBUM_LIST - column names
    private static final String KEY_ALBUM_ID = "albumId";
    private static final String KEY_ALBUM_TITLE = "albumTitle";
    private static final String KEY_TYPE = "type";
    private static final String KEY_ALBUM_PICTURE_BLOB = "albumPictureBlob";
    private static final String KEY_ALBUM_PICTURE_URL = "albumPictureUrl";

    // TABLE_MD5_KEYS - column names
    private static final String KEY_MD5_KEYS = "md5Key";

    // TABLE_ARTIST_LIST - table create statement
    private static final String CREATE_TABLE_ARTIST_LIST = "CREATE TABLE "
            + TABLE_ARTIST_LIST + "(" + _id + " INTEGER PRIMARY KEY," + KEY_ARTIST_ID + " TEXT,"
            + KEY_GENRES + " TEXT," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_ARTIST_PICTURE_URL + " TEXT," + KEY_ARTIST_PICTURE_BLOB + " BLOB" + ")";

    // TABLE_ALBUM_LIST - table create statement
    private static final String CREATE_TABLE_ALBUM_LIST = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ALBUM_LIST + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ALBUM_ID
            + " TEXT," + KEY_ARTIST_ID + " TEXT," + KEY_ALBUM_TITLE + " TEXT," + KEY_TYPE + " TEXT,"
            + KEY_ALBUM_PICTURE_URL + " TEXT," + KEY_ALBUM_PICTURE_BLOB + " BLOB" + ")";

    // TABLE_MD5_KEYS - table create statement
    private static final String CREATE_TABLE_MD5_KEYS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MD5_KEYS + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MD5_KEYS
            + " TEXT" + ")";

    private SQLiteDatabase db;
    private Context context;
    private DataBaseHelper dbHelper;

    protected DataBaseAdapter(Context context){
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
            db.execSQL(CREATE_TABLE_MD5_KEYS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 0n upgrade drop older tables
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MD5_KEYS);
            // Create new tables
            onCreate(db);
        }
    }

    public DataBaseAdapter open(){
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e("MYAPP", "SqliteException: " + e);
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public DataBaseAdapter close(){
        dbHelper.close();
        return this;
    }

    public long createArtistListRecords(String artistId, String genres, String artistPictureUrl,
                                        byte[] artistPicture, String name, String description) {

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST_ID, artistId);
        values.put(KEY_GENRES, genres);
        values.put(KEY_ARTIST_PICTURE_URL, artistPictureUrl);
        values.put(KEY_ARTIST_PICTURE_BLOB, artistPicture);
        values.put(KEY_NAME, name);
        values.put(KEY_DESCRIPTION, description);

        return db.insert(TABLE_ARTIST_LIST, null, values);
    }

    public long createAlbumListRecords(String artistId, String albumId, String albumTitle,
                                       String type, String albumPictureUrl, byte[] albumPicture) {

        ContentValues values = new ContentValues();
        values.put(KEY_ARTIST_ID, artistId);
        values.put(KEY_ALBUM_ID, albumId);
        values.put(KEY_ALBUM_TITLE, albumTitle);
        values.put(KEY_TYPE, type);
        values.put(KEY_ALBUM_PICTURE_URL, albumPictureUrl);
        values.put(KEY_ALBUM_PICTURE_BLOB, albumPicture);

        return db.insert(TABLE_ALBUM_LIST, null, values);
    }

    public long createMD5KeysRecords(String md5Key){
        ContentValues values = new ContentValues();
        values.put(KEY_MD5_KEYS, md5Key);
        return db.insert(TABLE_MD5_KEYS, null, values);
    }

    public Cursor getArtistListItems() {
        String[] columns = {_id, KEY_ARTIST_ID, KEY_NAME, KEY_GENRES, KEY_DESCRIPTION, KEY_ARTIST_PICTURE_URL, KEY_ARTIST_PICTURE_BLOB};
        return db.query(TABLE_ARTIST_LIST, columns, null, null, null, null, null);
    }

    public Cursor getAlbumsListItems(String artistId) {
        String[] columns = {_id, KEY_ARTIST_ID, KEY_ALBUM_ID, KEY_ALBUM_TITLE, KEY_TYPE, KEY_ALBUM_PICTURE_URL, KEY_ALBUM_PICTURE_BLOB};
        return db.query(TABLE_ALBUM_LIST, columns, KEY_ARTIST_ID + " = " + artistId, null, null, null, null);
    }

    public Cursor getMd5Key(){
        String[] columns = {_id, KEY_MD5_KEYS};
        return db.query(TABLE_MD5_KEYS, columns, null, null, null, null, null);
    }
}
