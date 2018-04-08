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
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Viewer.ArtistListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcher implements IDataFetcher {

    private AbstractExecutorService mThreadPoolExecutor;
    private Context mContext;
    private IDataBaseAdapterDIBuilder mDataBaseAdapterDIBuilder;
    private IHttpHandlerDIBuilder mHttpHandlerDIBuilder;
    private IJsonObjectExtended mJsonObjectExtended;
    private final Handler mHandler;

    // Gets the number of available cores
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final long KEEP_ALIVE_TIME = 2000;
    // Sets the Time Unit to Milliseconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    public static final String JSON_URL = "http://i.img.co/data/data.json";

    public DataFetcher(IDataFetcherDIBuilder builder, Context context){
        this.mContext = context;
        this.mDataBaseAdapterDIBuilder = ((DataFetcherDIBuilder)builder).mDataBaseAdapterDIBuilder;
        this.mJsonObjectExtended = ((DataFetcherDIBuilder)builder).mJsonObjectExtended;
        this.mHttpHandlerDIBuilder = ((DataFetcherDIBuilder)builder).mHttpHandlerDIBuilder;
        this.mHandler = ((DataFetcherDIBuilder)builder).mHandler;
        this.mThreadPoolExecutor = ((DataFetcherDIBuilder)builder).mThreadPoolExecutor;
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
                JSONObject jsonObj = (JSONObject) mJsonObjectExtended.setJsonObject(jsonStr);
                // Getting JSON Array node
                JSONArray artistJsonArray = jsonObj.getJSONArray("artists");

                ((DataBaseAdapter)dataBaseAdapter).open(1, true);

                ((ThreadPoolExecutor) mThreadPoolExecutor).setCorePoolSize(NUMBER_OF_CORES + 1);
                ((ThreadPoolExecutor) mThreadPoolExecutor).setMaximumPoolSize(NUMBER_OF_CORES + 3);
                ((ThreadPoolExecutor) mThreadPoolExecutor).setKeepAliveTime(KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT);

                // Looping through All Artist
                for (int i = 0; i < artistJsonArray.length(); i++) {
                    JSONObject artistJsonObj = artistJsonArray.getJSONObject(i);
                    Runnable artistFetchingRunnable = new ArtistFetchingRunnable(artistJsonObj, dataBaseAdapter);
                    ((ThreadPoolExecutor) mThreadPoolExecutor).execute(artistFetchingRunnable);
                }
                // Getting JSON Array node
                JSONArray albumJsonArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albumJsonArray.length(); i++) {
                    JSONObject albumJsonObj = albumJsonArray.getJSONObject(i);
                    Runnable albumFetchingRunnable = new AlbumFetchingRunnable(albumJsonObj, dataBaseAdapter);
                    ((ThreadPoolExecutor) mThreadPoolExecutor).execute(albumFetchingRunnable);
                }
//              Line commented on purpose, please don't hesitate to ask.
//              Todo uncomment line below.
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
                ArtistListActivity.threadPoolSize = ((ThreadPoolExecutor) mThreadPoolExecutor).getPoolSize();
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
                ArtistListActivity.threadPoolSize = ((ThreadPoolExecutor) mThreadPoolExecutor).getPoolSize();
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
