package com.grzesica.przemek.artistlist.Container;

import com.grzesica.przemek.artistlist.Model.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by przemek on 04.03.18.
 */

public class HttpHandlerDIBuilder implements IHttpHandlerDIBuilder {
    public Appendable mStrBuilder;
    public OutputStream mByteArrayOutputStream;
    public IExtendedUrl mExtendedUrl;
    public IExtendedBufferReader mExtendedBufferedReader;

    public HttpHandlerDIBuilder strBuilder() {
        mStrBuilder = new StringBuilder();
        return this;
    }

    public HttpHandlerDIBuilder byteArrayOutputStream() {
        mByteArrayOutputStream = new ByteArrayOutputStream();
        return this;
    }

    public HttpHandlerDIBuilder extendedUrl() {
        mExtendedUrl = new ExtendedURL();
        return this;
    }

    public HttpHandlerDIBuilder extendedBufferedReader() {
        mExtendedBufferedReader = new ExtendedBufferReader();
        return this;
    }

    @Override
    public HttpHandler build() {
        return new HttpHandler(this);
    }
}

