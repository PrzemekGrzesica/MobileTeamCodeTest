package com.grzesica.przemek.artistlist;

import android.graphics.Bitmap;

/**
 * Created by przemek on 03.03.18.
 */

public interface IhttpHandler {
    String jsonServiceCall(String requestUrl);
    byte[] getBlob(Bitmap bitmap);
    Bitmap downloadImage(String url);
}
