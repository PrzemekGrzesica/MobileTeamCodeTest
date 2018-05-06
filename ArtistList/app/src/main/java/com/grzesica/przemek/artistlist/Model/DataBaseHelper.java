package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by przemek on 01.05.2018
 */
@Singleton
public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Name
    public static final String DATABASE_NAME = "artistDb.db";

    // Table Names
    static final String TABLE_ARTIST_LIST = "artist";
    static final String TABLE_ALBUM_LIST = "albums";
    static final String TABLE_MD5_KEYS = "md5Keys";

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

    // TABLE_ALBUM_LIST - table create statement
    static final String CREATE_TABLE_ALBUM_LIST = "CREATE TABLE IF NOT EXISTS "
            + TABLE_ALBUM_LIST + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ALBUM_ID
            + " TEXT," + KEY_ARTIST_ID + " TEXT," + KEY_ALBUM_TITLE + " TEXT," + KEY_TYPE + " TEXT,"
            + KEY_ALBUM_PICTURE_URL + " TEXT," + KEY_ALBUM_PICTURE_BLOB + " BLOB" + ")";

    // TABLE_MD5_KEYS - table create statement
    static final String CREATE_TABLE_MD5_KEYS = "CREATE TABLE IF NOT EXISTS "
            + TABLE_MD5_KEYS + "(" + _id + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MD5_KEYS
            + " TEXT" + ")";

    private SQLiteDatabase mDataBase;
    private Context mContext;
    @Inject
    Parcelable mContentValues;

    public DataBaseHelper(Context context, int dbVersion) {
        super(context, DATABASE_NAME, null, dbVersion);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase dataBase) {
        // Creating required tables
        try {
            dataBase.execSQL(CREATE_TABLE_ARTIST_LIST);
            dataBase.execSQL(CREATE_TABLE_ALBUM_LIST);
            dataBase.execSQL(CREATE_TABLE_MD5_KEYS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(SQLiteDatabase database){
        if (database == null){
            onCreate(database);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dataBase, int oldVersion, int newVersion) {
        // 0n upgrade drop older tables
        dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST_LIST);
        dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM_LIST);
        dataBase.execSQL("DROP TABLE IF EXISTS " + TABLE_MD5_KEYS);
        // Create new tables
        onCreate(dataBase);
    }
}


