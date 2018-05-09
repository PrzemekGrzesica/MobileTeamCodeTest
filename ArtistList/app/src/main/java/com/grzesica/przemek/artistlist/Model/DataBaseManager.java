package com.grzesica.przemek.artistlist.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

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

    private SQLiteDatabase mDatabase;

    private @Named("contentValues")
    Parcelable mContentValues;

    private SQLiteOpenHelper mDataBaseHelper;

    @Inject
    public DataBaseManager(@Named("contentValues") Parcelable contentValues, SQLiteOpenHelper dataBaseHelper) {
        this.mContentValues = contentValues;
        this.mDataBaseHelper = dataBaseHelper;
    }


    @Override
    public SQLiteDatabase open() {
        mDatabase = mDataBaseHelper.getWritableDatabase();
        return mDatabase;
    }

    @Override
    public DataBaseManager close() {
        mDataBaseHelper.close();
        return this;
    }

    public long putArtistListRecords(String artistId, String genres, String artistPictureUrl,
                                     byte[] artistPicture, String name, String description){
        ContentValues values = (ContentValues) mContentValues;
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
        ContentValues values = (ContentValues) mContentValues;
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

        ContentValues values = (ContentValues) mContentValues;
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

}
