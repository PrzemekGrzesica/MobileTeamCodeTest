package com.grzesica.przemek.artistlist.Module;

import com.grzesica.przemek.artistlist.Model.DataFetcher;

import dagger.Component;

/**
 * Created by przemek on 18.04.18.
 */
@Component(modules = {DataFetcherModule.class})
public interface DataFetcherComponent {
    void inject(DataFetcher dataFetcher);
}
