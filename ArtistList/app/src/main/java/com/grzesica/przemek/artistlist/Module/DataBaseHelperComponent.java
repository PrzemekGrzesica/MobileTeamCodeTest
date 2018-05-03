package com.grzesica.przemek.artistlist.Module;

import android.content.Context;

import com.grzesica.przemek.artistlist.Model.DataBaseHelper;
import com.grzesica.przemek.artistlist.Module.Annotations.ApplicationContext;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by przemek on 28.04.18.
 */
@Singleton
@Component(modules = {DataBaseHelperModule.class})
public interface DataBaseHelperComponent {
    void inject(DataBaseHelper dataBaseAdapter);

    @ApplicationContext
    Context getContext();


}
