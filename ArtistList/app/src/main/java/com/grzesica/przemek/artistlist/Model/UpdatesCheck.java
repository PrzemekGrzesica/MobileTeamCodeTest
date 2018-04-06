package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Container.DataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.DataFetcherDIBuilder;
import com.grzesica.przemek.artistlist.Container.HttpHandlerDIBuilder;
import com.grzesica.przemek.artistlist.Container.IDataBaseAdapterDIBuilder;
import com.grzesica.przemek.artistlist.Container.UpdatesCheckDIBuilder;
import com.grzesica.przemek.artistlist.Viewer.SettingsActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by przemek on 13.02.18.
 * todo DI
 */

public class UpdatesCheck extends AsyncTask<Integer, Void, String> {


    private Context mContext;
    private Comparable mBoolean;
    private Handler mHandler;
    private IDataBaseAdapter mDataBaseAdapter;
    private IDataBaseAdapterDIBuilder mDataBaseAdapterDIBuilder;

    public UpdatesCheck(UpdatesCheckDIBuilder builder, Context context) {
        this.mContext = context;
//        this.mHandler = new Handler(mContext.getMainLooper());
        this.mHandler = ((UpdatesCheckDIBuilder) builder).mHandler;
        this.mDataBaseAdapterDIBuilder = ((UpdatesCheckDIBuilder) builder).mDataBaseAdapterDIBuilder;
    }

    @Override
    protected void onPostExecute(String strBoolean) {
        super.onPostExecute(strBoolean);
        if (strBoolean != null) {
            boolean updatesAvailability = Boolean.parseBoolean(strBoolean);
            if (updatesAvailability) {
                Intent intent = new Intent(mContext, SettingsActivity.class);
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
        HttpHandlerDIBuilder depInjBuilder = new HttpHandlerDIBuilder();
        HttpHandler httpHandler = depInjBuilder
                .byteArrayOutputStream()
                .strBuilder()
                .extendedUrl()
                .extendedBufferedReader()
                .build();

        String jsonStr = httpHandler.jsonServiceCall(DataFetcher.JSON_URL);

        DataBaseAdapterDIBuilder dataBaseAdapterDIBuilder = (DataBaseAdapterDIBuilder) mDataBaseAdapterDIBuilder;
        mDataBaseAdapter = dataBaseAdapterDIBuilder
                .contentValues()
                .dataBaseHelperDIBuilder()
                .build(mContext);

        //Open existing database - flag = 0
        mDataBaseAdapter.open(0, false);
        Cursor cursor = ((DataBaseAdapter) mDataBaseAdapter).getMd5Key();
        cursor.moveToFirst();
        try {
            oldMD5Key = cursor.getString(cursor.getColumnIndex("md5Key"));
        } catch (Exception e) {
            oldMD5Key = "EmptyOld";
        }
        mDataBaseAdapter.close();
        String updatesAvailability = null;

        if (jsonStr != null) {
            updatesAvailability = "false";
            newMD5Key = new MD5checkSum().stringToMD5(jsonStr);
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
        mHandler.post(r);
    }
}

class MD5checkSum {
    public String stringToMD5(String jsonString) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(jsonString.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}