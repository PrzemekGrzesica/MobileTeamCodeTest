package com.grzesica.przemek.artistlist.Model;

import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

public class ArtistFetchingRunnable implements Runnable {

    private JSONObject mArtistJsonObj;
    private DataBaseManager mDataBaseManager;
    private GuiContainer mGuiContainer;
    private HttpHandler mHttpHandler;
    private ThreadPoolExecutor mThreadPoolExecutor;

    @Inject
    public ArtistFetchingRunnable(IDataBaseManager dataBaseManager, JsonObjectExtended artistJsonObj,
                                  IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor, IGuiContainer guiContainer){
        this.mDataBaseManager = (DataBaseManager) dataBaseManager;
        this.mHttpHandler = (HttpHandler) httpHandler;
        this.mGuiContainer = (GuiContainer) guiContainer;
        this.mThreadPoolExecutor = (ThreadPoolExecutor) threadPoolExecutor;
        this.mArtistJsonObj = (JsonObjectExtended) artistJsonObj;
    }

    @Override
    public void run() {
        try {
            HttpHandler httpHandler = mHttpHandler;
            JSONObject jsonObject = mArtistJsonObj;
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
            mDataBaseManager.putArtistListRecords(artistId, genres, artistPictureUrl, artistPicture, name, description);
            long taskCount = mThreadPoolExecutor.getTaskCount();
            long completedTaskCount = mThreadPoolExecutor.getCompletedTaskCount();
            if (taskCount > completedTaskCount + 1){
                mGuiContainer.setServiceFlag(true);
            }else{
                mGuiContainer.setServiceFlag(false);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setArtistJsonObj(JSONObject artistJsonObj){
        this.mArtistJsonObj = artistJsonObj;
    }
}
