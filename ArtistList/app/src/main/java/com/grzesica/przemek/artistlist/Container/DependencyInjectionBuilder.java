package com.grzesica.przemek.artistlist.Container;

import com.grzesica.przemek.artistlist.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by przemek on 04.03.18.
 */

public class DependencyInjectionBuilder implements IDependencyInjectionBuilder {
    public Appendable mStrBuilder;
    public OutputStream mByteArrayOutputStream;
    public IExtendedUrl mExtendedUrl;
    public IExtendedBufferReader mExtendedBufferedReader;

    public DependencyInjectionBuilder strBuilder() {
        mStrBuilder = new StringBuilder();
        return this;
    }

    public DependencyInjectionBuilder byteArrayOutputStream() {
        mByteArrayOutputStream = new ByteArrayOutputStream();
        return this;
    }

    public DependencyInjectionBuilder extendedUrl() {
        mExtendedUrl = new ExtendedURL();
        return this;
    }

    public DependencyInjectionBuilder extendedBufferedReader() {
        mExtendedBufferedReader = new ExtendedBufferReader();
        return this;
    }

    @Override
    public HttpHandler build() {
        return new HttpHandler(this);
    }
}

