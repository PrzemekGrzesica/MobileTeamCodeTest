package com.grzesica.przemek.artistlist.Module;

import com.grzesica.przemek.artistlist.Model.HttpHandler;

import dagger.Component;

/**
 * Created by przemek on 16.04.18.
 */
@Component(modules = {HttpHandlerModule.class})
public interface HttpHandlerComponent {
    void inject(HttpHandler httpHandler);
}
