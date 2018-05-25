package com.grzesica.przemek.artistlist.Container;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by przemek on 04.03.18.
 */

public class ExtendedURL implements IExtendedUrl {

    URL mUrl;

    public ExtendedURL() {
        this.mUrl = null;
    }

    public URL getUrlConn(String strUrl) {
        try {
            mUrl = new URL(strUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return mUrl;
    }
}
