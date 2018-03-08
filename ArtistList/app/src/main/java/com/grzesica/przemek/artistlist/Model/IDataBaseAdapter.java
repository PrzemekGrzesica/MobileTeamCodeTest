package com.grzesica.przemek.artistlist.Model;

/**
 * Created by przemek on 05.03.18.
 */

public interface IDataBaseAdapter {
    DataBaseAdapter open(int dbVersionFlag);
    DataBaseAdapter close();
}
