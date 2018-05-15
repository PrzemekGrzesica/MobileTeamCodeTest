package com.grzesica.przemek.artistlist.Adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Service.DataFetchingService;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import javax.inject.Inject;
import javax.inject.Named;

public class CursorManager implements ICursorManager{

    private Context mContext;
    private DataBaseManager mDataBaseManager;
    private GuiContainer mGuiContainer;
    private Intent mIntent;
    private int mCursorPosition;
    private String mArtistId;

    @Inject
    public CursorManager(IDataBaseManager dataBaseManager, Context context, @Named("dataFetchingService") Parcelable intent, IGuiContainer guiContainer){
        this.mDataBaseManager = (DataBaseManager) dataBaseManager;
        this.mGuiContainer = (GuiContainer) guiContainer;
        this.mContext = context;
        this.mIntent = (Intent) intent;
    }

    @Override
    public Cursor getArtistListCursor(int cursorPosition){
        setCursorPosition(cursorPosition);
        mDataBaseManager.openPresent();
        if (mDataBaseManager==null){
            Intent intent = mIntent;
            mIntent.putExtra(DataFetchingService.STR_MESSAGE, "Please, wait for? database refreshing...");
            mGuiContainer.setServiceFlag(true);
            mContext.startService(intent);
        }
        try {
            Cursor cursor = mDataBaseManager.getArtistListRecords();
            cursor.moveToPosition(--cursorPosition);
            return cursor;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public Cursor getAlbumsListCursor(String artistId) {
        setArtistId(artistId);
        DataBaseManager dataBaseManager = mDataBaseManager;
        dataBaseManager.openPresent();
        try {
            Cursor cursor = dataBaseManager.getAlbumsListRecords(artistId);
            cursor.moveToFirst();
            return cursor;
        }catch (Exception e){
            return null;
        }
    }

    public void setCursorPosition(int cursorPosition){
        this.mCursorPosition = cursorPosition;
    }

    public int getCursorPosition(){
        return mCursorPosition;
    }

    public void setArtistId(String artistId){
        this.mArtistId = artistId;
    }

    public String getArtistId() {
        return mArtistId;
    }
}
