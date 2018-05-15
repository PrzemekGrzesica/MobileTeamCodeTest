package com.grzesica.przemek.artistlist.DI;

import android.content.Context;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Adapter.CursorManager;
import com.grzesica.przemek.artistlist.Adapter.ICursorManager;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CursorManagerModule {
    @Provides
    @Singleton
    public ICursorManager provideCursorManager(IDataBaseManager dataBaseManager, Context context, @Named("dataFetchingService") Parcelable intent,
                                               IGuiContainer guiContainer){
        return new CursorManager(dataBaseManager, context, intent, guiContainer);
    }
}
