package com.grzesica.przemek.artistlist.Model;

import com.grzesica.przemek.artistlist.Container.IJsonObjectExtended;
import com.grzesica.przemek.artistlist.Viewer.ArtistListActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

public class AlbumsFetchingRunnable implements Runnable {

    private IJsonObjectExtended mAlbumsJsonObj;
    private IDataBaseManager mDataBaseManager;
    private IHttpHandler mHttpHandler;
    private AbstractExecutorService mThreadPoolExecutor;

    @Inject
    public AlbumsFetchingRunnable(IJsonObjectExtended albumsJsonObj, IDataBaseManager dataBaseManager,
                                 IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor){
        this.mAlbumsJsonObj = albumsJsonObj;
        this.mDataBaseManager = dataBaseManager;
        this.mHttpHandler = httpHandler;
        this.mThreadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public void run() {
        try {
            HttpHandler httpHandler = (HttpHandler) mHttpHandler;
            JSONObject jsonObject = (JSONObject) mAlbumsJsonObj;
            String albumId = jsonObject.getString("id");
            String artistId = jsonObject.getString("artistId");
            String title = jsonObject.getString("title");
            String type = jsonObject.getString("type");
            String albumPictureUrl = jsonObject.getString("picture");
            byte[] albumPicture = httpHandler.getBlob(httpHandler.downloadImage(albumPictureUrl));
            int i = 0;
            while (albumPicture==null && i < 6){
                albumPicture = httpHandler.getBlob(httpHandler.downloadImage(albumPictureUrl));
                i++;
            }
            ((DataBaseManager)mDataBaseManager).putAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
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

