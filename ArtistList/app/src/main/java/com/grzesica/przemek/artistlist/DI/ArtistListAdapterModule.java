package com.grzesica.przemek.artistlist.DI;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcelable;
import android.widget.CursorAdapter;

import com.grzesica.przemek.artistlist.Adapter.ArtistListAdapter;
import com.grzesica.przemek.artistlist.Adapter.CursorManager;
import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Model.DataBaseManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class ArtistListAdapterModule {

    @Provides
    @Named("ArtistAdapter")
    public CursorAdapter provideArtistListAdapter(Context context, @Named("ArtistListCursor")Cursor cursor){
        if (cursor!=null) {
            return new ArtistListAdapter(context, cursor);
        }else{
            return null;
        }
    }

    @Provides
    @Named("ArtistListCursor")
    public Cursor provideArtistListCursor(IDataBaseManager dataBaseManager, Context context, @Named("dataFetchingService") Parcelable intent,
                                          ICursorManager cursorManager){
        CursorManager cM = (CursorManager)cursorManager;
        return cM.getArtistListCursor(cM.getCursorPosition());
    }
}
