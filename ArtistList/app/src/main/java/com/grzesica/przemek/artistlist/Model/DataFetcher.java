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

    /*
     * Gets the number of available cores
     * (not always the same as the maximum number of cores)
     */
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final int KEEP_ALIVE_TIME = 1000;

    // Sets the Time Unit to Milliseconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;


    public static final String JSON_URL = "http://i.img.co/data/data.json";

    public DataFetcher(IDataFetcherDIBuilder builder, Context context){
        this.mDataBaseAdapterDIBuilder = ((DataFetcherDIBuilder)builder).mDataBaseAdapterDIBuilder;
        this.mHttpHandlerDIBuilder = ((DataFetcherDIBuilder)builder).mHttpHandlerDIBuilder;
        this.mContext = context;
        this.mHandler = new Handler(mContext.getMainLooper());
    }

    @Override
    public void getData(){

        HttpHandlerDIBuilder depInjBuilder = (HttpHandlerDIBuilder) mHttpHandlerDIBuilder;
        mHttpHandler = depInjBuilder
                .byteArrayOutputStream()
                .strBuilder()
                .extendedUrl()
                .extendedBufferedReader()
                .build();

        DataBaseAdapterDIBuilder dataBaseAdapterDIBuilder = (DataBaseAdapterDIBuilder) mDataBaseAdapterDIBuilder;
        mDataBaseAdapter = dataBaseAdapterDIBuilder
                .contentValues()
                .dataBaseHelperDIBuilder()
                .build(mContext);

        String jsonStr = mHttpHandler.jsonServiceCall(JSON_URL);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray artistJsonArray = jsonObj.getJSONArray("artists");

                ((DataBaseAdapter)mDataBaseAdapter).open(1, true);

                ((DataBaseAdapter)mDataBaseAdapter).createMD5KeysRecords(new MD5checkSum().stringToMD5(jsonStr));
                // Looping through All Artist
                ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                        NUMBER_OF_CORES + 5,   // Initial pool size
                        NUMBER_OF_CORES + 8,   // Max pool size
                        KEEP_ALIVE_TIME,       // Time idle thread waits before terminating
                        KEEP_ALIVE_TIME_UNIT,  // Sets the Time Unit for KEEP_ALIVE_TIME
                        new LinkedBlockingDeque<Runnable>());

                for (int i = 0; i < artistJsonArray.length(); i++) {
                    JSONObject artistJsonObj = artistJsonArray.getJSONObject(i);
                    Runnable artistFetchingRunnable = new ArtistFetchingRunnable(artistJsonObj);
                    threadPoolExecutor.execute(artistFetchingRunnable);
                }
                // Getting JSON Array node
                JSONArray albumJsonArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albumJsonArray.length(); i++) {
                    JSONObject albumJsonObj = albumJsonArray.getJSONObject(i);
                    Runnable albumFetchingRunnable = new AlbumFetchingRunnable(albumJsonObj);
                    threadPoolExecutor.execute(albumFetchingRunnable);
                }
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
        private ArtistFetchingRunnable(JSONObject artistJsonObj){
            this.mArtistJsonObj = artistJsonObj;
        }
        @Override
        public void run() {
            try {
                String artistId = mArtistJsonObj.getString("id");
                String genres = mArtistJsonObj.getString("genres");
                String artistPictureUrl = mArtistJsonObj.getString("picture");
                byte[] artistPicture = mHttpHandler.getBlob(mHttpHandler.downloadImage(artistPictureUrl));
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
        private AlbumFetchingRunnable(JSONObject albumJsonObj){
            this.mAlbumJsonObj = albumJsonObj;
        }
        @Override
        public void run() {
            try {
                String albumId = mAlbumJsonObj.getString("id");
                String artistId = mAlbumJsonObj.getString("artistId");
                String title = mAlbumJsonObj.getString("title");
                String type = mAlbumJsonObj.getString("type");
                String albumPictureUrl = mAlbumJsonObj.getString("picture");
                byte[] albumPicture = mHttpHandler.getBlob(mHttpHandler.downloadImage(albumPictureUrl));
                ((DataBaseAdapter)mDataBaseAdapter).createAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
