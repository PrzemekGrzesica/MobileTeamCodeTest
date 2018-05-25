package com.grzesica.przemek.artistlist.Container;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by przemek on 05.03.18.
 */

public class ExtendedBufferReader implements IExtendedBufferReader {

    BufferedReader mBufferReader;

    @Override
    public BufferedReader setInputStream(InputStream inputStream) {
        mBufferReader = new BufferedReader(new InputStreamReader(inputStream));
        return mBufferReader;
    }
}
