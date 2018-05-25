package com.grzesica.przemek.artistlist.Model;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Container.ExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Model.Utilities.IToastRunnable;
import com.grzesica.przemek.artistlist.Model.Utilities.ToastRunnable;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Created by przemek on 13.02.18.
 *
 */
@Singleton
public class UpdatesCheck extends AsyncTask<Integer, Void, String> {

    private Context mContext;
    private DataBaseManager mDataBaseManager;
    private ExtendedHandler mExtendedHandler;
    private GuiContainer mGuiContainer;
    private HttpHandler mHttpHandler;
    private Intent mIntentSettingsActivity;
    private MD5checkSum mMD5checkSum;
    private ToastRunnable mToastRunnable;

    @Inject
    public UpdatesCheck(IMD5checkSum md5checkSum, Context context, IDataBaseManager dataBaseManager,
                        IExtendedHandler extendedHandler, IHttpHandler httpHandler,
                        @Named("settingsActivity") Parcelable intentSettingsActivity,
                        IGuiContainer guiContainer, IToastRunnable toastRunnable) {
        this.mMD5checkSum = (MD5checkSum) md5checkSum;
        this.mContext = context;
        this.mDataBaseManager = (DataBaseManager) dataBaseManager;
        this.mExtendedHandler = (ExtendedHandler) extendedHandler;
        this.mGuiContainer = (GuiContainer) guiContainer;
        this.mHttpHandler = (HttpHandler) httpHandler;
        this.mIntentSettingsActivity = (Intent) intentSettingsActivity;
        this.mToastRunnable = (ToastRunnable) toastRunnable;
    }

    @Override
    protected void onPostExecute(String strBoolean) {
        super.onPostExecute(strBoolean);
        if (strBoolean != null) {
            boolean updatesAvailability = Boolean.parseBoolean(strBoolean);
            if (updatesAvailability) {
                Intent intent = mIntentSettingsActivity;
                mContext.startActivity(intent);
            } else {
                mToastRunnable.setToastText("Your application database is up-to-date");
                runOnUiThread(mToastRunnable);
            }
        }
    }

    @Override
    protected String doInBackground(Integer... voids) {

        String newMD5Key;
        String oldMD5Key;

        HttpHandler httpHandler = mHttpHandler;
        String jsonStr = httpHandler.jsonServiceCall(DataFetcher.JSON_URL);

        mDataBaseManager.openPresent();
        Cursor cursor = mDataBaseManager.getMd5KeyRecord();
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
            mGuiContainer.setArtistFetchingServiceFlag(false);
            mGuiContainer.setAlbumsFetchingServiceFlag(false);
            mToastRunnable.setToastText("Couldn't get server. Check your internet connection!!!");
            runOnUiThread(mToastRunnable);
        }
        return updatesAvailability;
    }

    private void runOnUiThread(Runnable r) {
        mExtendedHandler.post(r);
    }
}

