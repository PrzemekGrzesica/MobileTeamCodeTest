package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.ExtendedHandler;
import com.grzesica.przemek.artistlist.Container.IExtendedHandler;

import dagger.Module;
import dagger.Provides;

@Module
public class ExtendedHandlerModule {
    @Provides
    public IExtendedHandler provideExtendedHandler(){
        return new ExtendedHandler();
    }
}
