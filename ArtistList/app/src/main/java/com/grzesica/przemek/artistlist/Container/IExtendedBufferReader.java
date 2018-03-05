package com.grzesica.przemek.artistlist.Container;

import java.io.BufferedReader;
import java.io.InputStream;

/**
 * Created by przemek on 05.03.18.
 */

public interface IExtendedBufferReader extends Readable{
    BufferedReader setInputStream(InputStream inputStream);
}
