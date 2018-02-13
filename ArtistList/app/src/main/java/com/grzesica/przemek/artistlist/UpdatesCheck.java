package com.grzesica.przemek.artistlist;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by przemek on 13.02.18.
 */

public class UpdatesCheck extends AsyncTask<Void, Void, Void> {

    Context context;

    protected UpdatesCheck(Context context){
        this.context = context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
