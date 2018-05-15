package com.grzesica.przemek.artistlist.Model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by przemek on 05.03.18.
 */

public interface IDataBaseManager {
    SQLiteDatabase openPresent();
    void openNew();
    DataBaseManager close();
}
