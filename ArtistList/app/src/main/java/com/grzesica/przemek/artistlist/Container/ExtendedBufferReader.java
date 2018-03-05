package com.grzesica.przemek.artistlist.Container;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.CharBuffer;


/**
 * Created by przemek on 05.03.18.
 */

public class ExtendedBufferReader implements IExtendedBufferReader {

    BufferedReader mBufferReader;

    public ExtendedBufferReader(){
        this.mBufferReader = null;
    }

    @Override
    public BufferedReader setInputStream(InputStream inputStream) {
        mBufferReader = new BufferedReader(new InputStreamReader(inputStream));
        return mBufferReader;
    }

    @Override
    public int read(@NonNull CharBuffer cb) throws IOException {
        return 0;
    }
}