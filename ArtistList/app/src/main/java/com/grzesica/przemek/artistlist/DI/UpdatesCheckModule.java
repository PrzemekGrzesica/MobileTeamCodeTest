package com.grzesica.przemek.artistlist.DI;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;

import com.grzesica.przemek.artistlist.Container.IExtendedHandler;
import com.grzesica.przemek.artistlist.Model.IDataBaseManager;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;
import com.grzesica.przemek.artistlist.Model.IMD5checkSum;
import com.grzesica.przemek.artistlist.Model.UpdatesCheck;
import com.grzesica.przemek.artistlist.Model.Utilities.IToastRunnable;
import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class UpdatesCheckModule{

    @Provides
    public AsyncTask provideUpdatesCheck(IMD5checkSum mD5checkSum, Context context,
                                         IDataBaseManager dataBaseManager,
                                         IExtendedHandler extendedHandler, IHttpHandler httpHandler,
                                         @Named("settingsActivity") Parcelable intentSettingsActivity,
                                         IGuiContainer guiContainer, IToastRunnable toastRunnable){
        return new UpdatesCheck(mD5checkSum, context, dataBaseManager, extendedHandler, httpHandler,
                                intentSettingsActivity, guiContainer, toastRunnable);
    }
}
