package com.grzesica.przemek.artistlist.Model;

/**
 * Created by przemek on 05.03.18.
 */

public interface IDataBaseSingleton {
    DataBaseSingleton open(int dbVersionFlag);
    DataBaseSingleton close();
}
