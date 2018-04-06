package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Container.DataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.DataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Container.HttpHandlerDIBuilder;
import com.grzesica.przemek.artistlist.Container.IDataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.IDataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Container.IHttpHandlerDIBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcher implements IDataFetcher {

    private Context mContext;
    private IDataBaseAdapter mDataBaseAdapter;
    private IDataBaseAdapterDIBuilder mDataBaseAdapterDIBuilder;
    private IHttpHandler mHttpHandler;
    private IHttpHandlerDIBuilder mHttpHandlerDIBuilder;
    private final Handler mHandler;

    // Gets the number of available cores
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 2000;
    // Sets the Time Unit to Milliseconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    public static final String JSON_URL = "http://i.img.co/data/data.json";

    public DataFetcher(IDataFetcherDIBuilder builder, Context context){
        this.mDataBaseAdapterDIBuilder = ((DataFetcherDIBuilder)builder).mDataBaseAdapterDIBuilder;
        this.mHttpHandlerDIBuilder = ((DataFetcherDIBuilder)builder).mHttpHandlerDIBuilder;
        this.mContext = context;
        this.mHandler = ((DataFetcherDIBuilder)builder).mHandler;
    }

    @Override
    public void getData(){

        IHttpHandler httpHandler = ((HttpHandlerDIBuilder)mHttpHandlerDIBuilder)
                .byteArrayOutputStream()
                .strBuilder()
                .extendedUrl()
                .extendedBufferedReader()
                .build();
        DataBaseAdapterDIBuilder dataBaseAdapterDIBuilder = (DataBaseAdapterDIBuilder) mDataBaseAdapterDIBuilder;
        IDataBaseAdapter dataBaseAdapter = dataBaseAdapterDIBuilder
                .contentValues()
                .dataBaseHelperDIBuilder()
                .build(mContext);

        String jsonStr = httpHandler.jsonServiceCall(JSON_URL);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray artistJsonArray = jsonObj.getJSONArray("artists");

                ((DataBaseAdapter)dataBaseAdapter).open(1, true);

                ThreadPoolExecutor artistThreadPoolExecutor = new ThreadPoolExecutor(
                        NUMBER_OF_CORES + 1,   // Initial pool size
                        NUMBER_OF_CORES + 2,   // Max pool size
                        KEEP_ALIVE_TIME,       // Time idle thread waits before terminating
                        KEEP_ALIVE_TIME_UNIT,  // Sets the Time Unit for KEEP_ALIVE_TIME
                        new LinkedBlockingDeque<Runnable>());
                // Looping through All Artist
                for (int i = 0; i < artistJsonArray.length(); i++) {
                    JSONObject artistJsonObj = artistJsonArray.getJSONObject(i);
                    Runnable artistFetchingRunnable = new ArtistFetchingRunnable(artistJsonObj, dataBaseAdapter);
                    artistThreadPoolExecutor.execute(artistFetchingRunnable);
                }
                ThreadPoolExecutor albumThreadPoolExecutor = new ThreadPoolExecutor(
                        NUMBER_OF_CORES + 1,   // Initial pool size
                        NUMBER_OF_CORES + 2,   // Max pool size
                        KEEP_ALIVE_TIME,       // Time idle thread waits before terminating
                        KEEP_ALIVE_TIME_UNIT,  // Sets the Time Unit for KEEP_ALIVE_TIME
                        new LinkedBlockingDeque<Runnable>());
                // Getting JSON Array node
                JSONArray albumJsonArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albumJsonArray.length(); i++) {
                    JSONObject albumJsonObj = albumJsonArray.getJSONObject(i);
                    Runnable albumFetchingRunnable = new AlbumFetchingRunnable(albumJsonObj, dataBaseAdapter);
                    albumThreadPoolExecutor.execute(albumFetchingRunnable);
                }

//                ((DataBaseAdapter)mDataBaseAdapter).createMD5KeysRecords(new MD5checkSum().stringToMD5(jsonStr));

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,
                            "Couldn't get server. Check your internet connection!!!",
                            Toast.LENGTH_LONG).show();
                }
            });

        }
    }
    private void runOnUiThread(Runnable r) {
        mHandler.post(r);
    }

    private class ArtistFetchingRunnable implements Runnable {
        private JSONObject mArtistJsonObj;
        private IDataBaseAdapter mDataBaseAdapter;
        private ArtistFetchingRunnable(JSONObject artistJsonObj, IDataBaseAdapter dataBaseAdapter){
            this.mArtistJsonObj = artistJsonObj;
            this.mDataBaseAdapter = dataBaseAdapter;
        }
        @Override
        public void run() {
            try {
                IHttpHandler httpHandler = ((HttpHandlerDIBuilder)mHttpHandlerDIBuilder)
                        .byteArrayOutputStream()
                        .strBuilder()
                        .extendedUrl()
                        .extendedBufferedReader()
                        .build();
                String artistId = mArtistJsonObj.getString("id");
                String genres = mArtistJsonObj.getString("genres");
                String artistPictureUrl = mArtistJsonObj.getString("picture");
                byte[] artistPicture = httpHandler.getBlob(httpHandler.downloadImage(artistPictureUrl));
                int i = 0;
                while (artistPicture == null && i < 6){
                    artistPicture = httpHandler.getBlob(httpHandler.downloadImage(artistPictureUrl));
                    i++;
                }

                String name = mArtistJsonObj.getString("name");
                String description = mArtistJsonObj.getString("description");
                ((DataBaseAdapter) mDataBaseAdapter).createArtistListRecords(artistId, genres, artistPictureUrl, artistPicture, name, description);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class AlbumFetchingRunnable implements Runnable {
        private JSONObject mAlbumJsonObj;
        private IDataBaseAdapter mDataBaseAdapter;
        private AlbumFetchingRunnable(JSONObject albumJsonObj, IDataBaseAdapter dataBaseAdapter){
            this.mAlbumJsonObj = albumJsonObj;
            this.mDataBaseAdapter = dataBaseAdapter;
        }
        @Override
        public void run() {
            try {
                IHttpHandler httpHandler = ((HttpHandlerDIBuilder)mHttpHandlerDIBuilder)
                        .byteArrayOutputStream()
                        .strBuilder()
                        .extendedUrl()
                        .extendedBufferedReader()
                        .build();
                String albumId = mAlbumJsonObj.getString("id");
                String artistId = mAlbumJsonObj.getString("artistId");
                String title = mAlbumJsonObj.getString("title");
                String type = mAlbumJsonObj.getString("type");
                String albumPictureUrl = mAlbumJsonObj.getString("picture");
                byte[] albumPicture = httpHandler.getBlob(httpHandler.downloadImage(albumPictureUrl));
                int i = 0;
                while (albumPicture==null && i < 6){
                    albumPicture = httpHandler.getBlob(httpHandler.downloadImage(albumPictureUrl));
                    i++;
                }
                ((DataBaseAdapter)mDataBaseAdapter).createAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
