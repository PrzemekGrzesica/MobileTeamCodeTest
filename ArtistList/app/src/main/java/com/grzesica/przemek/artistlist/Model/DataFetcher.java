package com.grzesica.przemek.artistlist.Model;

import android.util.Log;

import com.grzesica.przemek.artistlist.Application.ArtistListApplication;
import com.grzesica.przemek.artistlist.Container.ExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Model.Utilities.IToastRunnable;
import com.grzesica.przemek.artistlist.Model.Utilities.ToastRunnable;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 05.03.18.
 */
@Singleton
public class DataFetcher implements IDataFetcher {

    private DataBaseManager mDataBaseManager;
    private ExtendedHandler mExtendedHandler;
    private GuiContainer mGuiContainer;
    private HttpHandler mHttpHandler;
    private JsonObjectExtended mJsonObjectExtended;
    private ThreadPoolExecutor mThreadPoolExecutor;
    private ToastRunnable mToastRunnable;
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
    public DataFetcher(AbstractExecutorService threadPoolExecutor, IDataBaseManager dataBaseManager,
                       IToastRunnable toastRunnable, IHttpHandler httpHandler,
                       IJsonObjectExtended jsonObjectExtended,
                       IExtendedHandler extendedHandler, IGuiContainer guiContainer) {
        this.mThreadPoolExecutor = (ThreadPoolExecutor) threadPoolExecutor;
        this.mDataBaseManager = (DataBaseManager) dataBaseManager;
        this.mHttpHandler = (HttpHandler) httpHandler;
        this.mGuiContainer = (GuiContainer) guiContainer;
        this.mJsonObjectExtended = (JsonObjectExtended) jsonObjectExtended;
        this.mExtendedHandler = (ExtendedHandler) extendedHandler;
        this.mToastRunnable = (ToastRunnable) toastRunnable;
        ArtistListApplication.getApplicationComponent().inject(this);
    }

    @Override
    public void getEndPointData() {

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
                    ArtistFetchingRunnable artistFetchingRunnable = (ArtistFetchingRunnable) mArtistFetching.get();
                    artistFetchingRunnable.setArtistJsonObj(artistJsonObj);
                    mThreadPoolExecutor.execute(artistFetchingRunnable);
                }
                // Getting JSON Array node
                JSONArray albumJsonArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albumJsonArray.length(); i++) {
                    JSONObject albumJsonObj = albumJsonArray.getJSONObject(i);
                    AlbumsFetchingRunnable albumsFetchingRunnable = (AlbumsFetchingRunnable) mAlbumsFetching.get();
                    albumsFetchingRunnable.setAlbumsJsonObj(albumJsonObj);
                    mThreadPoolExecutor.execute(albumsFetchingRunnable);
                }
//              Line commented on purpose, please don't hesitate to ask.
//              Todo uncomment line below.
//                ((DataBaseAdapter)mDataBaseAdapter).createMD5KeysRecords(new MD5checkSum().stringToMD5(jsonStr));

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                mToastRunnable.setToastText("Json parsing error: " + e.getMessage());
                runOnUiThread(mToastRunnable);
            }
        } else {
            mGuiContainer.setArtistFetchingServiceFlag(false);
            mGuiContainer.setAlbumsFetchingServiceFlag(false);
            mToastRunnable.setToastText("Couldn't get server. Check your internet connection!!!");
            runOnUiThread(mToastRunnable);
        }
    }

    private void runOnUiThread(Runnable r) {
        mExtendedHandler.post(r);
    }
}
