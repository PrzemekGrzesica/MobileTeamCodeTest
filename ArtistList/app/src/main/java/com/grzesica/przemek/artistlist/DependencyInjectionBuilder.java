package com.grzesica.przemek.artistlist;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Created by przemek on 04.03.18.
 */

public class DependencyInjectionBuilder {
    public Appendable mStrBuilder;
    public OutputStream mByteArrayOutputStream;

    DependencyInjectionBuilder strBuilder(){
        mStrBuilder = new StringBuilder();
        return this;
    }
    DependencyInjectionBuilder byteArrayOutputStream(){
        mByteArrayOutputStream = new ByteArrayOutputStream();
        return this;
    }
    public HttpHandler build(){
        return new HttpHandler(this);
    }
}
