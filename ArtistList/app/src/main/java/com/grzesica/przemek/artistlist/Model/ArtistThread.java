package com.grzesica.przemek.artistlist.Model;

import com.grzesica.przemek.artistlist.Service.DataFetchingService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by przemek on 25.03.18.
 * Artist data fetching thread.
 */

public class ArtistThread implements Runnable {

    private JSONObject mArtistJsonObj;
    private IDataBaseAdapter mDataBaseAdapter;
    private IHttpHandler mHttpHandler;

    public ArtistThread(JSONObject artistJsonObj){
        this.mArtistJsonObj = artistJsonObj;
    }

    @Override
    public void run(){
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
