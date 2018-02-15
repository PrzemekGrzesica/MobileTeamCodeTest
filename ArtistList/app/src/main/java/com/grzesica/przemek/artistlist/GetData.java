package com.grzesica.przemek.artistlist;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 13.12.17. Getting data from json file.
 */
public class GetData extends AsyncTask<Integer, Void, Void> {

    public static final String JSON_URL = "http://i.img.co/data/data.json";
    Context context;
    private DataBaseAdapter dbAdapter;

    protected GetData(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, "Database is creating...", Toast.LENGTH_LONG).show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Integer... dbVersionFlag) {

        HttpHandler httpHandler = new HttpHandler();
        String jsonStr = httpHandler.jsonServiceCall(JSON_URL);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray artArray = jsonObj.getJSONArray("artists");

                dbAdapter = new DataBaseAdapter(context);
                dbAdapter.open(dbVersionFlag[0]);
                //
                dbAdapter.createMD5KeysRecords(new MD5checkSum().stringToMD5(jsonStr));
                // Looping through All Artist
                for (int i = 0; i < artArray.length(); i++) {
                    JSONObject artObj = artArray.getJSONObject(i);
                    String artistId = artObj.getString("id");
                    String genres = artObj.getString("genres");
                    String artistPictureUrl = artObj.getString("picture");
                    byte[] artistPicture = httpHandler.getBlob(httpHandler.downloadImage(artistPictureUrl));
                    String name = artObj.getString("name");
                    String description = artObj.getString("description");
                    dbAdapter.createArtistListRecords(artistId, genres, artistPictureUrl, artistPicture, name, description);
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
                    byte[] albumPicture = httpHandler.getBlob(httpHandler.downloadImage(albumPictureUrl));
                    dbAdapter.createAlbumListRecords(artistId, albumId, title, type, albumPictureUrl, albumPicture);
                }
                //
                dbAdapter.close();
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
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }
}

