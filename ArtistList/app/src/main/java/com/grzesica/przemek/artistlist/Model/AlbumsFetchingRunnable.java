package com.grzesica.przemek.artistlist.Model;

import com.grzesica.przemek.artistlist.Container.JsonObjectExtended;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

public class AlbumsFetchingRunnable implements Runnable {

    private JSONObject mAlbumsJsonObj;
    private GuiContainer mGuiContainer;
    private DataBaseManager mDataBaseManager;
    private HttpHandler mHttpHandler;
    private ThreadPoolExecutor mThreadPoolExecutor;

    @Inject
    public AlbumsFetchingRunnable(IDataBaseManager dataBaseManager, JsonObjectExtended albumsJsonObj,
                                 IHttpHandler httpHandler, AbstractExecutorService threadPoolExecutor, IGuiContainer guiContainer){
        this.mDataBaseManager = (DataBaseManager) dataBaseManager;
        this.mHttpHandler = (HttpHandler) httpHandler;
        this.mGuiContainer = (GuiContainer) guiContainer;
        this.mThreadPoolExecutor = (ThreadPoolExecutor) threadPoolExecutor;
        this.mAlbumsJsonObj = (JsonObjectExtended) albumsJsonObj;
    }

    @Override
    public void run() {
        try {
            HttpHandler httpHandler = mHttpHandler;
            JSONObject jsonObject = mAlbumsJsonObj;
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
            mDataBaseManager.putAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
            long taskCount =  mThreadPoolExecutor.getTaskCount();
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

    public void setAlbumsJsonObj(JSONObject albumsJsonObj){
        this.mAlbumsJsonObj = albumsJsonObj;
    }
}

