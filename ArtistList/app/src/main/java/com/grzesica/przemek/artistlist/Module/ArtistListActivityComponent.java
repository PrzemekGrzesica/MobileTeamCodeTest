package com.grzesica.przemek.artistlist.Module;

import com.grzesica.przemek.artistlist.Viewer.ArtistListActivity;

import dagger.Component;

/**
 * Created by przemek on 28.04.18.
 */
@Component(modules = {ArtistListActivityModule.class})
public interface ArtistListActivityComponent {
    void inject(ArtistListActivity artistListActivity);
}
