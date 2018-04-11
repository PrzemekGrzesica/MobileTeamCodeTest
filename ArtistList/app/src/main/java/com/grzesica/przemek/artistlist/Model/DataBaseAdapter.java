package com.grzesica.przemek.artistlist.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Container.DataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.IDataBaseAdapterDIBuilder;

/**
 * Created by przemek on 22.11.17.
 * SQLHelper for creation three types of tables (artist list table, albums list of an artist table, MD5 sum of json file table).
 * Internal static class has been created to avoid an implicit reference to an external class.
 */

public class DataBaseAdapter implements IDataBaseAdapter {

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

    private SQLiteDatabase mDataBase;
    private Context mContext;
    private IDataBaseHelperDIBuilder mDataBaseHelperDIBuilder;
    private Parcelable mContentValues;
    private SQLiteOpenHelper mDataBaseHelper;
    private volatile static DataBaseAdapter uniqueInstance;

    public DataBaseAdapter(IDataBaseAdapterDIBuilder builder, Context context){
        this.mContext = context;
        this.mDataBaseHelperDIBuilder = ((DataBaseAdapterDIBuilder)builder).mDataBaseHelperDIBuilder;
        this.mContentValues = ((DataBaseAdapterDIBuilder)builder).mContentValues;
    }

    public static DataBaseAdapter newInstance(DataBaseAdapterDIBuilder builder, Context context){
        if(uniqueInstance==null){
            synchronized (DataBaseAdapter.class){
                if(uniqueInstance==null){
                    uniqueInstance = new DataBaseAdapter(builder, context);
                }
            }
        }
        return uniqueInstance;
    }

    public interface IDataBaseHelperDIBuilder{
        DataBaseAdapter.DataBaseHelper build(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion);
    }

    public static class DataBaseHelperDIBuilder implements IDataBaseHelperDIBuilder{

        @Override
        public DataBaseAdapter.DataBaseHelper build(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
            return new DataBaseAdapter.DataBaseHelper(this, context, dbName, factory, dbVersion);
        }
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(IDataBaseHelperDIBuilder builder, Context context, String dbName, CursorFactory factory, int dbVersion) {
            super(context, dbName, factory, dbVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase dataBase) {
            // Creating required tables
            dataBase.execSQL(CREATE_TABLE_ARTIST_LIST);
            dataBase.execSQL(CREATE_TABLE_ALBUM_LIST);
            dataBase.execSQL(CREATE_TABLE_MD5_KEYS);
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

    @Override
    public DataBaseAdapter open(int dbVersionFlag, boolean writableFlag){

        String strDbPath = mContext.getDatabasePath(DATABASE_NAME).toString();
        int dbPresentVersion;
        try{
            dbPresentVersion  = SQLiteDatabase.openDatabase(strDbPath, null, 0).getVersion();
        } catch (Exception e) {
            dbPresentVersion = 1;
            writableFlag = true;
        }
        int dbVersion = dbVersionFlag + dbPresentVersion;
        DataBaseHelperDIBuilder dataBaseHelperDIBuilder = (DataBaseHelperDIBuilder) mDataBaseHelperDIBuilder;
        mDataBaseHelper = dataBaseHelperDIBuilder
                .build(mContext, DATABASE_NAME, null, dbVersion);

        DataBaseHelper dataBaseHelper = (DataBaseHelper)mDataBaseHelper;
        if (writableFlag == true && dbVersion == 1) {
            try {
                mDataBase = dataBaseHelper.getWritableDatabase();
            } catch (SQLException e) {
                mDataBase = dataBaseHelper.getReadableDatabase();
            }
        }else{
            mDataBase = dataBaseHelper.getReadableDatabase();
        }
        return this;
    }

    @Override
    public DataBaseAdapter close(){
        ((DataBaseHelper)mDataBaseHelper).close();
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

        return mDataBase.insert(TABLE_ARTIST_LIST, null, values);
    }

    public long createAlbumListRecords(String artistId, String albumId, String albumTitle,
                                       String type, String albumPictureUrl, byte[] albumPicture) {

        ContentValues values = (ContentValues)mContentValues;
        values.put(KEY_ARTIST_ID, artistId);
        values.put(KEY_ALBUM_ID, albumId);
        values.put(KEY_ALBUM_TITLE, albumTitle);
        values.put(KEY_TYPE, type);
        values.put(KEY_ALBUM_PICTURE_URL, albumPictureUrl);
        values.put(KEY_ALBUM_PICTURE_BLOB, albumPicture);

        return mDataBase.insert(TABLE_ALBUM_LIST, null, values);
    }

    public synchronized long createMD5KeysRecords(String md5Key){
        ContentValues values = (ContentValues)mContentValues;
        values.put(KEY_MD5_KEYS, md5Key);
        return mDataBase.insert(TABLE_MD5_KEYS, null, values);
    }

    public Cursor getArtistListItems() {
        String[] columns = {_id, KEY_ARTIST_ID, KEY_NAME, KEY_GENRES, KEY_DESCRIPTION, KEY_ARTIST_PICTURE_URL, KEY_ARTIST_PICTURE_BLOB};
        return mDataBase.query(TABLE_ARTIST_LIST, columns, null, null, null, null, _id);
    }

    public Cursor getAlbumsListItems(String artistId) {
        String[] columns = {_id, KEY_ARTIST_ID, KEY_ALBUM_ID, KEY_ALBUM_TITLE, KEY_TYPE, KEY_ALBUM_PICTURE_URL, KEY_ALBUM_PICTURE_BLOB};
        return mDataBase.query(TABLE_ALBUM_LIST, columns, KEY_ARTIST_ID + " = " + artistId, null, null, null, null);
    }

    public Cursor getMd5Key(){
        String[] columns = {_id, KEY_MD5_KEYS};
        return mDataBase.query(TABLE_MD5_KEYS, columns, null, null, null, null, null);
    }
}



/**
 * Created by przemek on 28.03.18.
 */

