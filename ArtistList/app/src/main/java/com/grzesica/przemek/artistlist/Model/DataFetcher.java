package com.grzesica.przemek.artistlist.Model;


import android.content.Context;
import android.util.Log;

import com.grzesica.przemek.artistlist.Container.DataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Container.HttpHandlerDIBuilder;
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
    private IHttpHandler mHttpHandler;
    private IHttpHandlerDIBuilder mHttpHandlerDIBuilder;
    private JSONArray mArtistJsonArray;
    private JSONArray mAlbumJsonArray;

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

    public DataFetcher(IDataFetcherDIBuilder builder){
        this.mDataBaseAdapter = ((DataFetcherDIBuilder) builder).mDataBaseAdapter;
        this.mHttpHandlerDIBuilder = ((DataFetcherDIBuilder)builder).mHttpHandlerDIBuilder;
    }

    @Override
    public void getData(){

//        HttpHandlerDIBuilder depInjBuilder = new HttpHandlerDIBuilder();
        HttpHandlerDIBuilder depInjBuilder = (HttpHandlerDIBuilder) mHttpHandlerDIBuilder;
        mHttpHandler = depInjBuilder
                .byteArrayOutputStream()
                .strBuilder()
                .extendedUrl()
                .extendedBufferedReader()
                .build();

        String jsonStr = mHttpHandler.jsonServiceCall(JSON_URL);


        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray artistJsonArray = jsonObj.getJSONArray("artists");

                mDataBaseAdapter = DataBaseAdapter.newInstance(mContext);
                ((DataBaseAdapter)mDataBaseAdapter).open(2);
                //
                ((DataBaseAdapter)mDataBaseAdapter).createMD5KeysRecords(new MD5checkSum().stringToMD5(jsonStr));
                // Looping through All Artist
                ThreadPoolExecutor mThreadPoolExecutor = new ThreadPoolExecutor(
                        NUMBER_OF_CORES + 5,   // Initial pool size
                        NUMBER_OF_CORES + 8,   // Max pool size
                        KEEP_ALIVE_TIME,       // Time idle thread waits before terminating
                        KEEP_ALIVE_TIME_UNIT,  // Sets the Time Unit for KEEP_ALIVE_TIME
                        new LinkedBlockingDeque<Runnable>());


                for (int i = 0; i < artistJsonArray.length(); i++) {
                    JSONObject artistJsonObj = artistJsonArray.getJSONObject(i);
                    Runnable artistFetchingRunnable = new ArtistFetchingRunnable(artistJsonObj);
                    mThreadPoolExecutor.execute(artistFetchingRunnable);

//                    String artistId = artObj.getString("id");
//                    String genres = artObj.getString("genres");
//                    String artistPictureUrl = artObj.getString("picture");
//                    byte[] artistPicture = mHttpHandler.getBlob(mHttpHandler.downloadImage(artistPictureUrl));
//                    String name = artObj.getString("name");
//                    String description = artObj.getString("description");
//                    ((DataBaseAdapter)mDataBaseAdapter).createArtistListRecords(artistId, genres, artistPictureUrl, artistPicture, name, description);
                }
                // Getting JSON Array node
                JSONArray albumJsonArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albumJsonArray.length(); i++) {
                    JSONObject albumJsonObj = albumJsonArray.getJSONObject(i);
                    Runnable albumFetchingRunnable = new AlbumFetchingRunnable(albumJsonObj);
                    mThreadPoolExecutor.execute(albumFetchingRunnable);

//                    String albumId = albObj.getString("id");
//                    String artistId = albObj.getString("artistId");
//                    String title = albObj.getString("title");
//                    String type = albObj.getString("type");
//                    String albumPictureUrl = albObj.getString("picture");
//                    byte[] albumPicture = mHttpHandler.getBlob(mHttpHandler.downloadImage(albumPictureUrl));
//                    ((DataBaseAdapter)mDataBaseAdapter).createAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
                }
                //
                mDataBaseAdapter.close();
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                /*runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });*/

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });*/

        }
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
            } {

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
            } {

            }
        }
    };
}
