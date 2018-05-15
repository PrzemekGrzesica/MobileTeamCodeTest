package com.grzesica.przemek.artistlist.Model;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.DATABASE_NAME;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_ALBUM_ID;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_ALBUM_PICTURE_BLOB;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_ALBUM_PICTURE_URL;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_ALBUM_TITLE;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_ARTIST_ID;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_ARTIST_PICTURE_BLOB;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_ARTIST_PICTURE_URL;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_DESCRIPTION;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_GENRES;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_MD5_KEYS;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_NAME;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.KEY_TYPE;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.TABLE_ALBUM_LIST;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.TABLE_ARTIST_LIST;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper.TABLE_MD5_KEYS;
import static com.grzesica.przemek.artistlist.Model.DataBaseHelper._id;

/**
 * Created by przemek on 03.05.18.
 */
@Singleton
public class DataBaseManager implements IDataBaseManager{

    @Inject @Named("Present")
    Provider<SQLiteOpenHelper> mDatabaseHelper;
    private Context mContext;
    private int mDbVersion;
    private ContentValues mArtistContentValues;
    private ContentValues mAlbumsContentValues;
    private ContentValues mMD5KeyContentValues;
    private SQLiteDatabase mDatabase;
    private GuiContainer mGuiContainer;
    private Intent mIntent;

    @Inject
    public DataBaseManager(@Named("AlbumsContentValues") Parcelable albumsContentValues,
                           @Named("ArtistContentValues") Parcelable artistContentValues,
                           @Named("MD5ContentValues") Parcelable mMD5KeyContentValues,
                           @Named("dataFetchingService") Parcelable intent,
                           Context context, IGuiContainer guiContainer) {
        this.mArtistContentValues = (ContentValues)artistContentValues;
        this.mAlbumsContentValues = (ContentValues)albumsContentValues;
        this.mGuiContainer = (GuiContainer) guiContainer;
        this.mIntent = (Intent)intent;
        this.mMD5KeyContentValues = (ContentValues)mMD5KeyContentValues;
        this.mContext = context;
        ArtistListApplication.getApplicationComponent().inject(this);
    }

    @Override
    public SQLiteDatabase openPresent() {
        setDbVersion(findDbVersion());
        DataBaseHelper databaseHelper = (DataBaseHelper) mDatabaseHelper.get();
        mDatabase = databaseHelper.getReadableDatabase();
        if (isEmptyDatabase()){
            mIntent.putExtra(DataFetchingService.STR_MESSAGE, "Please, wait for data download...");
            mGuiContainer.setServiceFlag(true);
            mContext.startService(mIntent);
        }
        return mDatabase;
    }

    @Override
    public void openNew() {
        setDbVersion(findDbVersion() + 1);
        DataBaseHelper presentDatabaseHelper = (DataBaseHelper) mDatabaseHelper.get();
        mDatabase = presentDatabaseHelper.getWritableDatabase();
        presentDatabaseHelper.onUpgrade(mDatabase, findDbVersion(), findDbVersion() + 1);
    }

    private int findDbVersion(){
        String strDbPath = mContext.getDatabasePath(DATABASE_NAME).toString();
        int dbVersion;
        try{
            dbVersion  = SQLiteDatabase.openDatabase(strDbPath, null, 0).getVersion();
        }catch (Exception e){
            dbVersion = 1;
        }
        return dbVersion;
    }

    public void setDbVersion(int dbVersion){
        mDbVersion = dbVersion;
    }

    public int getDbVersion(){
        return mDbVersion;
    }

    @Override
    public DataBaseManager close() {
//        mDatabaseHelper.close();
        return this;
    }

    public long putArtistListRecords(String artistId, String genres, String artistPictureUrl,
                                     byte[] artistPicture, String name, String description){
        ContentValues values = (ContentValues) mArtistContentValues;
        try {
            values.put(KEY_ARTIST_ID, artistId);
            values.put(KEY_GENRES, genres);
            values.put(KEY_ARTIST_PICTURE_URL, artistPictureUrl);
            values.put(KEY_ARTIST_PICTURE_BLOB, artistPicture);
            values.put(KEY_NAME, name);
            values.put(KEY_DESCRIPTION, description);

        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return mDatabase.insert(TABLE_ARTIST_LIST, null, values);
    }

    public long putAlbumListRecords(String artistId, String albumId, String albumTitle,
                                    String type, String albumPictureUrl, byte[] albumPicture) {
        ContentValues values = (ContentValues) mAlbumsContentValues;
        try {
            values.put(KEY_ARTIST_ID, artistId);
            values.put(KEY_ALBUM_ID, albumId);
            values.put(KEY_ALBUM_TITLE, albumTitle);
            values.put(KEY_TYPE, type);
            values.put(KEY_ALBUM_PICTURE_URL, albumPictureUrl);
            values.put(KEY_ALBUM_PICTURE_BLOB, albumPicture);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return mDatabase.insert(TABLE_ALBUM_LIST, null, values);
    }

    public long putMD5KeysRecords(String md5Key) {

        ContentValues values = (ContentValues) mMD5KeyContentValues;
        try {
            values.put(KEY_MD5_KEYS, md5Key);
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return mDatabase.insert(TABLE_MD5_KEYS, null, values);
    }

    public Cursor getArtistListRecords() {
        String[] columns = {_id, KEY_ARTIST_ID, KEY_NAME, KEY_GENRES, KEY_DESCRIPTION, KEY_ARTIST_PICTURE_URL, KEY_ARTIST_PICTURE_BLOB};
        return mDatabase.query(TABLE_ARTIST_LIST, columns, null, null, null, null, _id);
    }

    public Cursor getAlbumsListRecords(String artistId) {
        String[] columns = {_id, KEY_ARTIST_ID, KEY_ALBUM_ID, KEY_ALBUM_TITLE, KEY_TYPE, KEY_ALBUM_PICTURE_URL, KEY_ALBUM_PICTURE_BLOB};
        return mDatabase.query(TABLE_ALBUM_LIST, columns, KEY_ARTIST_ID + " = " + artistId, null, null, null, null);
    }

    public Cursor getMd5KeyRecord() {
        String[] columns = {_id, KEY_MD5_KEYS};
        return mDatabase.query(TABLE_MD5_KEYS, columns, null, null, null, null, null);
    }

    public boolean isEmptyDatabase(){
        int rows;
        try{
            rows = getArtistListRecords().getCount();
        }catch (Exception e){
            rows = 0;
        }
        if (rows==0){
            return true;
        }else {
            return false;
        }
    }

}
