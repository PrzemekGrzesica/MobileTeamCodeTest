package com.grzesica.przemek.artistlist.DI;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.widget.CursorAdapter;

import com.grzesica.przemek.artistlist.Adapter.AlbumsListAdapter;
import com.grzesica.przemek.artistlist.Adapter.CursorManager;
import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class AlbumsListAdapterModule {

    @Provides
    @Named("AlbumsAdapter")
    public CursorAdapter provideAlbumsListAdapter(Context context, @Named("AlbumsListCursor") Cursor cursor){
        if (cursor!=null) {
            return new AlbumsListAdapter(context, cursor);
        }else{
            return null;
        }
    }

    @Provides
    @Named("AlbumsListCursor")
    public Cursor provideAlbumsListCursor(IDataBaseManager dataBaseManager, Context context, @Named("dataFetchingService") Parcelable intent,
                                          ICursorManager cursorManager) {
        CursorManager cM = (CursorManager)cursorManager;
        return cM.getAlbumsListCursor(cM.getArtistId());
    }
}
