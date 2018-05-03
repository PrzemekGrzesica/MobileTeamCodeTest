package com.grzesica.przemek.artistlist.Container;

import android.graphics.BitmapFactory;

/**
 * Created by przemek on 20.04.18.
 */

public class BitmapFactoryOptions implements IBitmapFactoryOptions {

    BitmapFactory.Options mBitmapFactoryOptions;

    @Override
    public BitmapFactory.Options setBitmapFactoryOptions() {
        mBitmapFactoryOptions = new BitmapFactory.Options();
        return mBitmapFactoryOptions;
    }
}
