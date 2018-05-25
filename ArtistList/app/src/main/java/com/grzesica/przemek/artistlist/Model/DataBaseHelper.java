package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by przemek on 01.05.2018
 */
@Singleton
public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "presentDb.db";

    // Table Names
    public static final String TABLE_ARTIST_LIST = "artist";
    public static final String NEW_TABLE_ARTIST_LIST = "newArtist";
    public static final String TABLE_ALBUM_LIST = "albums";
    public static final String NEW_TABLE_ALBUM_LIST = "newAlbums";
    public static final String TABLE_MD5_KEYS = "md5Keys";
    public static final String NEW_TABLE_MD5_KEYS = "newMd5Keys";

    // Common column names
    static final String _id = "_id";
    static final String KEY_ARTIST_ID = "artistId";

    // TABLE_ARTIST_LIST - column names
    static final String KEY_GENRES = "genres";
    static final String KEY_NAME = "name";
    static final String KEY_DESCRIPTION = "description";
    static final String KEY_ARTIST_PICTURE_URL = "artistPictureUrl";
    static final String KEY_ARTIST_PICTURE_BLOB = "artistPictureBlob";

    // TABLE_ALBUM_LIST - column names
    static final String KEY_ALBUM_ID = "albumId";
    static final String KEY_ALBUM_TITLE = "albumTitle";
    static final String KEY_TYPE = "type";
    static final String KEY_ALBUM_PICTURE_BLOB = "albumPictureBlob";
    static final String KEY_ALBUM_PICTURE_URL = "albumPictureUrl";

    // TABLE_MD5_KEYS - column names
    static final String KEY_MD5_KEYS = "md5Key";

    // TABLE_ARTIST_LIST - table create statement
    static final String CREATE_TABLE_ARTIST_LIST = "CREATE TABLE "
            + TABLE_ARTIST_LIST + "(" + _id + " INTEGER PRIMARY KEY," + KEY_ARTIST_ID + " TEXT,"
            + KEY_GENRES + " TEXT," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_ARTIST_PICTURE_URL + " TEXT," + KEY_ARTIST_PICTURE_BLOB + " BLOB" + ")";

    // NEW_TABLE_ARTIST_LIST - table create statement
    static final String CREATE_NEW_TABLE_ARTIST_LIST = "CREATE TABLE "
            + NEW_TABLE_ARTIST_LIST + "(" + _id + " INTEGER PRIMARY KEY," + KEY_ARTIST_ID + " TEXT,"
            + KEY_GENRES + " TEXT," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_ARTIST_PICTURE_URL + " TEXT," + KEY_ARTIST_PICTURE_BLOB + " BLOB" + ")";

    // TABLE_ALBUM_LIST - table create statement
    static final String CREATE_TABLE_ALBUM_LIST = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ALBUM_LIST + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ALBUM_ID
            + " TEXT," + KEY_ARTIST_ID + " TEXT," + KEY_ALBUM_TITLE + " TEXT," + KEY_TYPE + " TEXT,"
            + KEY_ALBUM_PICTURE_URL + " TEXT," + KEY_ALBUM_PICTURE_BLOB + " BLOB" + ")";

    // NEW_TABLE_ALBUM_LIST - table create statement
    static final String CREATE_NEW_TABLE_ALBUM_LIST = "CREATE TABLE IF NOT EXISTS "
            + NEW_TABLE_ALBUM_LIST + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ALBUM_ID
            + " TEXT," + KEY_ARTIST_ID + " TEXT," + KEY_ALBUM_TITLE + " TEXT," + KEY_TYPE + " TEXT,"
            + KEY_ALBUM_PICTURE_URL + " TEXT," + KEY_ALBUM_PICTURE_BLOB + " BLOB" + ")";

    // TABLE_MD5_KEYS - table create statement
    static final String CREATE_TABLE_MD5_KEYS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MD5_KEYS + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MD5_KEYS
            + " TEXT" + ")";

    // NEW_TABLE_MD5_KEYS - table create statement
    static final String CREATE_NEW_TABLE_MD5_KEYS = "CREATE TABLE IF NOT EXISTS "
            + NEW_TABLE_MD5_KEYS + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MD5_KEYS
            + " TEXT" + ")";

    @Inject
    public DataBaseHelper(Context context, String databaseName, int dbVersion) {
        super(context, databaseName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        // Creating required tables
        try {
            dataBase.execSQL(CREATE_TABLE_ARTIST_LIST);
            dataBase.execSQL(CREATE_TABLE_ALBUM_LIST);
            dataBase.execSQL(CREATE_TABLE_MD5_KEYS);
            dataBase.execSQL(CREATE_NEW_TABLE_ARTIST_LIST);
            dataBase.execSQL(CREATE_NEW_TABLE_ALBUM_LIST);
            dataBase.execSQL(CREATE_NEW_TABLE_MD5_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @Override
    public void onOpen(SQLiteDatabase database) {
        if (database == null) {
            onCreate(database);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            dataBase.execSQL("DROP TABLE IF EXISTS " + NEW_TABLE_ARTIST_LIST);
            dataBase.execSQL("DROP TABLE IF EXISTS " + NEW_TABLE_ALBUM_LIST);
            dataBase.execSQL("DROP TABLE IF EXISTS " + NEW_TABLE_MD5_KEYS);
            // Create new tables
            try {
                dataBase.execSQL(CREATE_NEW_TABLE_ARTIST_LIST);
                dataBase.execSQL(CREATE_NEW_TABLE_ALBUM_LIST);
                dataBase.execSQL(CREATE_NEW_TABLE_MD5_KEYS);
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
            }
        } else {
            dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_LIST);
            dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM_LIST);
            dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_MD5_KEYS);
            try {
                dataBase.execSQL(CREATE_TABLE_ARTIST_LIST);
                dataBase.execSQL(CREATE_TABLE_ALBUM_LIST);
                dataBase.execSQL(CREATE_TABLE_MD5_KEYS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}


