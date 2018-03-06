package com.grzesica.przemek.artistlist.Model;


import android.content.Context;
import android.util.Log;

import com.grzesica.przemek.artistlist.Adapter.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Adapter.IDataBaseAdapter;
import com.grzesica.przemek.artistlist.Container.DataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Container.DependencyInjectionBuilder;
import com.grzesica.przemek.artistlist.Container.HttpHandlerDIBuilder;
import com.grzesica.przemek.artistlist.Container.IDataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Container.IDataFetchingHandler;
import com.grzesica.przemek.artistlist.Container.IDependencyInjectionBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 05.03.18.
 */

public class DataFetcher implements IDataFetcher {
    private Context mContext;
    private IDataBaseAdapter mDbAdapter;
    private IHttpHandler mHttpHandler;
    public DataFetcher(IDataFetcherDIBuilder builder){
        this.mDbAdapter = ((DataFetcherDIBuilder) builder).mHttpHandler;
    }

    public void getData(){
        HttpHandlerDIBuilder depInjBuilder = new HttpHandlerDIBuilder();
        HttpHandler mHttpHandler = depInjBuilder
                .byteArrayOutputStream()
                .strBuilder()
                .extendedUrl()
                .extendedBufferedReader()
                .build();

        String jsonStr = mhttpHandler.jsonServiceCall(JSON_URL);


        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray artArray = jsonObj.getJSONArray("artists");

                mDbAdapter = new DataBaseAdapter(mContext);
                mDbAdapter.open(dbVersionFlag[0]);
                //
                mDbAdapter.createMD5KeysRecords(new MD5checkSum().stringToMD5(jsonStr));
                // Looping through All Artist
                for (int i = 0; i < artArray.length(); i++) {
                    JSONObject artObj = artArray.getJSONObject(i);
                    String artistId = artObj.getString("id");
                    String genres = artObj.getString("genres");
                    String artistPictureUrl = artObj.getString("picture");
                    byte[] artistPicture = mhttpHandler.getBlob(mhttpHandler.downloadImage(artistPictureUrl));
                    String name = artObj.getString("name");
                    String description = artObj.getString("description");
                    mDbAdapter.createArtistListRecords(artistId, genres, artistPictureUrl, artistPicture, name, description);
                }
                // Getting JSON Array node
                JSONArray albArray = jsonObj.getJSONArray("albums");
                // Looping through all Albums
                for (int i = 0; i < albArray.length(); i++) {
                    JSONObject albObj = albArray.getJSONObject(i);
                    String albumId = albObj.getString("id");
                    String artistId = albObj.getString("artistId");
                    String title = albObj.getString("title");
                    String type = albObj.getString("type");
                    String albumPictureUrl = albObj.getString("picture");
                    byte[] albumPicture = mhttpHandler.getBlob(mhttpHandler.downloadImage(albumPictureUrl));
                    mDbAdapter.createAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
                }
                //
                mDbAdapter.close();
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
}
