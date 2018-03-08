package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.grzesica.przemek.artistlist.Container.HttpHandlerDIBuilder;
import com.grzesica.przemek.artistlist.Viewer.SettingsActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by przemek on 13.02.18.
 */

public class UpdatesCheck extends AsyncTask<Integer, Void, Boolean> {

    private DataBaseAdapter mDataBaseAdapter;
    Context mContext;

    public UpdatesCheck(Context context) {
        this.mContext = context;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        boolean updatesAvailability = new Boolean(aBoolean).booleanValue();
        if (updatesAvailability){
            Intent intent = new Intent(mContext, SettingsActivity.class);
            mContext.startActivity(intent);
        }else{
            Toast.makeText(mContext, "Your application database is up-to-date", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected Boolean doInBackground(Integer... voids) {
        String newMD5Key = null;
        String oldMD5Key = null;
//        HttpHandler httpHandler = new HttpHandler(new StringBuilder(), new ByteArrayOutputStream());
        HttpHandlerDIBuilder depInjBuilder = new HttpHandlerDIBuilder();
        HttpHandler httpHandler = depInjBuilder
                                    .byteArrayOutputStream()
                                    .strBuilder()
                                    .extendedUrl()
                                    .extendedBufferedReader()
                                    .build();

        String jsonStr = httpHandler.jsonServiceCall(GetData.JSON_URL);

        if (jsonStr != null) {
            newMD5Key = new MD5checkSum().stringToMD5(jsonStr);
            mDataBaseAdapter = DataBaseAdapter.newInstance(mContext);
            //Open existing database - flag = 0
            mDataBaseAdapter.open(1);
            Cursor cursor = mDataBaseAdapter.getMd5Key();
            cursor.moveToFirst();
            oldMD5Key = cursor.getString(cursor.getColumnIndex("md5Key"));
            mDataBaseAdapter.close();
        }
        boolean updatesAvailability = false;
        if (newMD5Key.equals(oldMD5Key) == false) {
            updatesAvailability = true;
        }
        return (Boolean) updatesAvailability;
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