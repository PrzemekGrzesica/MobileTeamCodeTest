package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Container.ExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 05.03.18.
 */
public class DataFetcher implements IDataFetcher {

    private ThreadPoolExecutor mThreadPoolExecutor;
    private Context mContext;
    private DataBaseManager mDataBaseManager;
    private ExtendedHandler mExtendedHandler;
    private HttpHandler mHttpHandler;
    private JsonObjectExtended mJsonObjectExtended;
    @Inject
    @Named("AlbumsFetching")
    Provider<Runnable> mAlbumsFetching;
    @Inject
    @Named("ArtistFetching")
    Provider<Runnable> mArtistFetching;

    // Gets the number of available cores
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    // Sets the amount of time an idle thread waits before terminating
    private static final long KEEP_ALIVE_TIME = 2000;
    // Sets the Time Unit to Milliseconds
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.MILLISECONDS;
    //todo Json-server localhost is needed for emulator
    public static final String JSON_URL = "http://10.0.2.2:3000/db";

    @Inject
    public DataFetcher(AbstractExecutorService threadPoolExecutor, Context context, IDataBaseManager dataBaseManager,
                       IHttpHandler httpHandler, IJsonObjectExtended jsonObjectExtended, IExtendedHandler extendedHandler) {
        this.mThreadPoolExecutor = (ThreadPoolExecutor) threadPoolExecutor;
        this.mContext = context;
        this.mDataBaseManager = (DataBaseManager) dataBaseManager;
        this.mHttpHandler = (HttpHandler) httpHandler;
        this.mJsonObjectExtended = (JsonObjectExtended) jsonObjectExtended;
        this.mExtendedHandler = (ExtendedHandler) extendedHandler;
        ArtistListApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void getData() {

        mDataBaseManager.openNew();

        HttpHandler httpHandler = mHttpHandler;
        String jsonStr = httpHandler.jsonServiceCall(JSON_URL);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = mJsonObjectExtended.setJsonObject(jsonStr);
                // Getting JSON Array node
                JSONArray artistJsonArray = jsonObj.getJSONArray("artists");

                mThreadPoolExecutor.setCorePoolSize(NUMBER_OF_CORES + 1);
                mThreadPoolExecutor.setMaximumPoolSize(NUMBER_OF_CORES + 3);
                mThreadPoolExecutor.setKeepAliveTime(KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT);

                // Looping through All Artist
                for (int i = 0; i < artistJsonArray.length(); i++) {
                    JSONObject artistJsonObj = artistJsonArray.getJSONObject(i);
                    ArtistFetchingRunnable artistFetchingRunnable = (ArtistFetchingRunnable)mArtistFetching.get();
                    artistFetchingRunnable.setArtistJsonObj(artistJsonObj);
                    mThreadPoolExecutor.execute(artistFetchingRunnable);
                }
                // Getting JSON Array node
                JSONArray albumJsonArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albumJsonArray.length(); i++) {
                    JSONObject albumJsonObj = albumJsonArray.getJSONObject(i);
                    AlbumsFetchingRunnable albumsFetchingRunnable = (AlbumsFetchingRunnable)mAlbumsFetching.get();
                    albumsFetchingRunnable.setAlbumsJsonObj(albumJsonObj);
                    mThreadPoolExecutor.execute(albumsFetchingRunnable);
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
        mExtendedHandler.post(r);
    }
}
