package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Parcelable;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Container.IExtendedHandler;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by przemek on 13.02.18.
 *
 */

public class UpdatesCheck extends AsyncTask<Integer, Void, String> {

    private IMD5checkSum mMD5checkSum;
    private Context mContext;
    private IDataBaseManager mDataBaseManager;
    private IExtendedHandler mExtendedHandler;
    private IHttpHandler mHttpHandler;
    private Parcelable mIntentSettingsActivity;

    @Inject
    public UpdatesCheck(IMD5checkSum md5checkSum, Context context, IDataBaseManager dataBaseManager,
                        IExtendedHandler extendedHandler,IHttpHandler httpHandler,
                        @Named("settingsActivity") Parcelable intentSettingsActivity) {
        this.mMD5checkSum = md5checkSum;
        this.mContext = context;
        this.mDataBaseManager = dataBaseManager;
        this.mExtendedHandler = extendedHandler;
        this.mHttpHandler = httpHandler;
        this.mIntentSettingsActivity = intentSettingsActivity;
    }

    @Override
    protected void onPostExecute(String strBoolean) {
        super.onPostExecute(strBoolean);
        if (strBoolean != null) {
            boolean updatesAvailability = Boolean.parseBoolean(strBoolean);
            if (updatesAvailability) {
                Intent intent = (Intent)mIntentSettingsActivity;
                mContext.startActivity(intent);
            } else {
                Toast.makeText(mContext, "Your application database is up-to-date", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected String doInBackground(Integer... voids) {

        String newMD5Key;
        String oldMD5Key;

        HttpHandler httpHandler = (HttpHandler) mHttpHandler;
        String jsonStr = httpHandler.jsonServiceCall(DataFetcher.JSON_URL);

        mDataBaseManager.open();
        Cursor cursor = ((DataBaseManager) mDataBaseManager).getMd5KeyRecord();
        cursor.moveToFirst();
        try {
            oldMD5Key = cursor.getString(cursor.getColumnIndex("md5Key"));
        } catch (Exception e) {
            oldMD5Key = "EmptyOld";
        }
        mDataBaseManager.close();
        String updatesAvailability = null;
        if (jsonStr != null) {
            updatesAvailability = "false";
            newMD5Key = ((MD5checkSum)mMD5checkSum).stringToMD5(jsonStr);
            if (newMD5Key.equals(oldMD5Key) != true) {
                updatesAvailability = "True";
            }
        } else {
            newMD5Key = "EmptyNew";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext, "Couldn't get server. Check your internet connection!!!", Toast.LENGTH_LONG).show();
                }
            });
        }
        return updatesAvailability;
    }

    private void runOnUiThread(Runnable r) {
        ((Handler)mExtendedHandler).post(r);
    }
}

