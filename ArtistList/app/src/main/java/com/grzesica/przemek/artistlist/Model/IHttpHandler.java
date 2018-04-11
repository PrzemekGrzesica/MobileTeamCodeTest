package com.grzesica.przemek.artistlist.Model;

import android.graphics.Bitmap;

/**
 * Created by przemek on 03.03.18.
 */

public interface IHttpHandler {
    String jsonServiceCall(String requestUrl);
    byte[] getBlob(Bitmap bitmap);
    Bitmap downloadImage(String url);
}
