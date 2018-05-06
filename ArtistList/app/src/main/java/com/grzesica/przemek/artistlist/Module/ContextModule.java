package com.grzesica.przemek.artistlist.Module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private final Context mContext;

    public ContextModule(Context context) {
        this.mContext = context;
    }

    @Provides
    public Context provideContext(){
        return mContext;
    }
}
