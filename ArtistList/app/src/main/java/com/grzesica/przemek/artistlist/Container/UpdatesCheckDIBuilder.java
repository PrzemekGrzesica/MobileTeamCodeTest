package com.grzesica.przemek.artistlist.Container;

import android.content.Context;
import android.os.Handler;

import com.grzesica.przemek.artistlist.Model.DataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.IDataBaseAdapter;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;

/**
 * Created by przemek on 27.03.18.
 */

public class UpdatesCheckDIBuilder implements IUpdatesCheckDIBuilder {

    public IDataBaseAdapterDIBuilder mDataBaseAdapterDIBuilder;
    public Handler mHandler;

    public UpdatesCheckDIBuilder dataBaseAdapterDIBUilder(){
        mDataBaseAdapterDIBuilder = new DataBaseAdapterDIBuilder();
        return this;
    }

    public UpdatesCheckDIBuilder handler(){
        mHandler= new Handler();
        return this;
    }

    @Override
    public UpdatesCheck build(Context context) {
        return new UpdatesCheck(this, context);
    }
}
