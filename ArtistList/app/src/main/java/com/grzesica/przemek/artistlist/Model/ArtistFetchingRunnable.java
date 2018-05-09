package com.grzesica.przemek.artistlist.Model;

import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Viewer.ArtistListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

public class ArtistFetchingRunnable implements Runnable {

    private IJsonObjectExtended mArtistJsonObj;
    private IDataBaseManager mDataBaseManager;
    private IHttpHandler mHttpHandler;
    private AbstractExecutorService mThreadPoolExecutor;

    @Inject
    public ArtistFetchingRunnable(IJsonObjectExtended artistJsonObj, IDataBaseManager dataBaseManager,
                                  IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor){
        this.mArtistJsonObj = artistJsonObj;
        this.mDataBaseManager = dataBaseManager;
        this.mHttpHandler = httpHandler;
        this.mThreadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void run() {
        try {
            HttpHandler httpHandler = (HttpHandler) mHttpHandler;
            JSONObject jsonObject = (JSONObject)mArtistJsonObj;
            String artistId = jsonObject.getString("id");
            String genres = jsonObject.getString("genres");
            String artistPictureUrl = jsonObject.getString("picture");
            byte[] artistPicture = httpHandler.getBlob(httpHandler.downloadImage(artistPictureUrl));
            int i = 0;
            while (artistPicture == null && i < 6){
                artistPicture = httpHandler.getBlob(httpHandler.downloadImage(artistPictureUrl));
                i++;
            }
            String name = jsonObject.getString("name");
            String description = jsonObject.getString("description");
            ((DataBaseManager) mDataBaseManager).putArtistListRecords(artistId, genres, artistPictureUrl, artistPicture, name, description);
            long taskCount = ((ThreadPoolExecutor) mThreadPoolExecutor).getTaskCount();
            long completedTaskCount = ((ThreadPoolExecutor) mThreadPoolExecutor).getCompletedTaskCount();
            if (taskCount > completedTaskCount + 1){
                ArtistListActivity.serviceFlag = true;
            }else{
                ArtistListActivity.serviceFlag = false;
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
