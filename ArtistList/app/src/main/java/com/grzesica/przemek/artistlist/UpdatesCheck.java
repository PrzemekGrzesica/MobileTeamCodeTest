package com.grzesica.przemek.artistlist;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by przemek on 13.02.18.
 */

public class UpdatesCheck extends AsyncTask<Void, Void, Void> {

    private DataBaseAdapter dbAdapter;
    Context context;
    boolean updatesAvailable = true;

    protected UpdatesCheck(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        HttpHandler httpHandler = new HttpHandler();

        String jsonUrl = "http://i.img.co/data/data.json";
        String jsonStr = httpHandler.jsonServiceCall(jsonUrl);
        if (jsonStr!=null){
            dbAdapter = new DataBaseAdapter(context);
            dbAdapter.open();
        }
        return null;
    }
}
