package com.grzesica.przemek.artistlist;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by przemek on 13.12.17. Getting data from json file.
 */
public class GetData extends AsyncTask<Void, Void, Void> {

    /*@Override
    protected void onPreExecute() {
        super.onPreExecute();
        // Showing progress dialog
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        pDialog.show();

    }*/

    DataBaseHandler dbHandler;

    @Override
    protected Void doInBackground(Void... arg0) {

        HttpHandler httpHandler = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = httpHandler.jsonServiceCall(jsonUrl);

        Log.e(TAG, "Response from url: " + jsonStr);

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray artArray = jsonObj.getJSONArray("artists");

                // Looping through All Artist
                for (int i = 0; i < artArray.length(); i++) {
                    JSONObject artObj = artArray.getJSONObject(i);

                    String artistId = artObj.getString("id");
                    String genres = artObj.getString("genres");
                    String artistpictureUrl = artObj.getString("picture");
                    String name = artObj.getString("name");
                    String description = artObj.getString("description");

                    dbHandler.createArtistListRecords(artistId, genres, artistpictureUrl, name, description);
                }

                // Getting JSON Array node
                JSONArray albArray = jsonObj.getJSONArray("albums");

                // Looping through all Albums
                for (int i = 0; i < albArray.length(); i++) {
                    JSONObject albObj = artArray.getJSONObject(i);

                    String albumId = albObj.getString("id");
                    String artistId = albObj.getString("artistId");
                    String title = albObj.getString("title");
                    String type = albObj.getString("type");
                    String albumPictureUrl = albObj.getString("picture");

                    dbHandler.createAlbumListRecords(artistId, albumId, title, type, albumPictureUrl);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "Couldn't get json from server. Check LogCat for possible errors!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            });

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        // Dismiss the progress dialog
        if (pDialog.isShowing())
            pDialog.dismiss();
        /**
         * Updating parsed JSON data into ListView
         * */
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, contactList,
                R.layout.list_item, new String[]{"name", "email",
                "mobile"}, new int[]{R.id.name,
                R.id.email, R.id.mobile});

        lv.setAdapter(adapter);
    }

}

