package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Viewer.IGuiContainer;
import com.grzesica.przemek.artistlist.Viewer.GuiContainer;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GuiContModule {
    @Provides
    @Singleton
    public IGuiContainer provideSingletonGuiCont(){
        return new GuiContainer();
    }
}
