package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Viewer.ArtistListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcher implements IDataFetcher {

    private AbstractExecutorService mThreadPoolExecutor;
    private Context mContext;
    private IDataBaseManager mDataBaseManager;
    private IExtendedHandler mExtendedHandler;
    private IHttpHandler mHttpHandler;
    private IJsonObjectExtended mJsonObjectExtended;
    private Runnable mArtistFetching;
    private Runnable mAlbumsFetching;

    // Gets the number of available cores
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final long KEEP_ALIVE_TIME = 2000;
    // Sets the Time Unit to Milliseconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;

    public static final String JSON_URL = "https://drive.google.com/open?id=11LW8Xv1UQQgrbwU0Cj5LaEtubUYvj5EE";//"http://i.img.co/data/data.json";

    @Inject
    public DataFetcher(AbstractExecutorService threadPoolExecutor, Context context, IDataBaseManager dataBaseManager,
                       IHttpHandler httpHandler, IJsonObjectExtended jsonObjectExtended, IExtendedHandler extendedHandler,
                       @Named("ArtistFetching") Runnable artistFetching, @Named("AlbumsFetching") Runnable albumsFetching){
        this.mThreadPoolExecutor = threadPoolExecutor;
        this.mContext = context;
        this.mDataBaseManager = dataBaseManager;
        this.mHttpHandler = httpHandler;
        this.mJsonObjectExtended = jsonObjectExtended;
        this.mExtendedHandler = extendedHandler;
        this.mArtistFetching = artistFetching;
        this.mAlbumsFetching = albumsFetching;
    }

    @Override
    public void getData(){

        DataBaseManager dataBaseManager = (DataBaseManager) mDataBaseManager;

        HttpHandler httpHandler = (HttpHandler) mHttpHandler;
        String jsonStr = httpHandler.jsonServiceCall(JSON_URL);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = (JSONObject) mJsonObjectExtended.setJsonObject(jsonStr);
                // Getting JSON Array node
                JSONArray artistJsonArray = jsonObj.getJSONArray("artists");
                dataBaseManager.open();

                ((ThreadPoolExecutor) mThreadPoolExecutor).setCorePoolSize(NUMBER_OF_CORES + 1);
                ((ThreadPoolExecutor) mThreadPoolExecutor).setMaximumPoolSize(NUMBER_OF_CORES + 3);
                ((ThreadPoolExecutor) mThreadPoolExecutor).setKeepAliveTime(KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT);

                // Looping through All Artist
                for (int i = 0; i < artistJsonArray.length(); i++) {
                    JSONObject artistJsonObj = artistJsonArray.getJSONObject(i);
//                    Runnable artistFetchingRunnable = new ArtistFetchingRunnable(artistJsonObj, dataBaseManager);
                    ((ThreadPoolExecutor) mThreadPoolExecutor).execute(mArtistFetching);
                }
                // Getting JSON Array node
                JSONArray albumJsonArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albumJsonArray.length(); i++) {
                    JSONObject albumJsonObj = albumJsonArray.getJSONObject(i);
//                    Runnable albumFetchingRunnable = new AlbumFetchingRunnable(albumJsonObj, dataBaseManager);
                    ((ThreadPoolExecutor) mThreadPoolExecutor).execute(mAlbumsFetching);
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
        ((Handler)mExtendedHandler).post(r);
    }

//    private class ArtistFetchingRunnable implements Runnable {
//
//        private IJsonObjectExtended mArtistJsonObj;
//        private IDataBaseManager mDataBaseManager;
//
//        @Inject
//        private ArtistFetchingRunnable(IJsonObjectExtended artistJsonObj, IDataBaseManager dataBaseManager){
//            this.mArtistJsonObj = artistJsonObj;
//            this.mDataBaseManager = dataBaseManager;
//        }
//
//        @Override
//        public void run() {
//            try {
//                HttpHandler httpHandler = (HttpHandler) mHttpHandler;
//                JSONObject jsonObject = (JSONObject)mArtistJsonObj;
//                String artistId = jsonObject.getString("id");
//                String genres = jsonObject.getString("genres");
//                String artistPictureUrl = jsonObject.getString("picture");
//                byte[] artistPicture = httpHandler.getBlob(httpHandler.downloadImage(artistPictureUrl));
//                int i = 0;
//                while (artistPicture == null && i < 6){
//                    artistPicture = httpHandler.getBlob(httpHandler.downloadImage(artistPictureUrl));
//                    i++;
//                }
//                String name = jsonObject.getString("name");
//                String description = jsonObject.getString("description");
//                ((DataBaseManager) mDataBaseManager).putArtistListRecords(artistId, genres, artistPictureUrl, artistPicture, name, description);
//                long taskCount = ((ThreadPoolExecutor) mThreadPoolExecutor).getTaskCount();
//                long completedTaskCount = ((ThreadPoolExecutor) mThreadPoolExecutor).getCompletedTaskCount();
//                if (taskCount > completedTaskCount + 1){
//                    ArtistListActivity.serviceFlag = true;
//                }else{
//                    ArtistListActivity.serviceFlag = false;
//                }
//            }catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private class AlbumFetchingRunnable implements Runnable {
//
//        private IJsonObjectExtended mAlbumJsonObj;
//        private IDataBaseManager mDataBaseManager;
//
//        private AlbumFetchingRunnable(IJsonObjectExtended albumJsonObj, IDataBaseManager dataBaseManager){
//            this.mAlbumJsonObj = albumJsonObj;
//            this.mDataBaseManager = dataBaseManager;
//        }
//
//        @Override
//        public void run() {
//            try {
//                HttpHandler httpHandler = (HttpHandler) mHttpHandler;
//                JSONObject jsonObject = (JSONObject) mAlbumJsonObj;
//                String albumId = jsonObject.getString("id");
//                String artistId = jsonObject.getString("artistId");
//                String title = jsonObject.getString("title");
//                String type = jsonObject.getString("type");
//                String albumPictureUrl = jsonObject.getString("picture");
//                byte[] albumPicture = httpHandler.getBlob(httpHandler.downloadImage(albumPictureUrl));
//                int i = 0;
//                while (albumPicture==null && i < 6){
//                    albumPicture = httpHandler.getBlob(httpHandler.downloadImage(albumPictureUrl));
//                    i++;
//                }
//                ((DataBaseManager)mDataBaseManager).putAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
//                long taskCount = ((ThreadPoolExecutor) mThreadPoolExecutor).getTaskCount();
//                long completedTaskCount = ((ThreadPoolExecutor) mThreadPoolExecutor).getCompletedTaskCount();
//                if (taskCount > completedTaskCount + 1){
//                    ArtistListActivity.serviceFlag = true;
//                }else{
//                    ArtistListActivity.serviceFlag = false;
//                }
//            }catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
