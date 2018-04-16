package com.grzesica.przemek.artistlist.Module;

import com.grzesica.przemek.artistlist.Container.ExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.ExtendedURL;
import com.grzesica.przemek.artistlist.Container.IExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.IExtendedUrl;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import dagger.Module;
import dagger.Provides;

/**
 * Created by przemek on 16.04.18.
 */

@Module
public class HttpHandlerModule {
    @Provides
    public Appendable provideStringBuilder(){
        return new StringBuilder();
    }
    @Provides
    public OutputStream provideOutputStream(){
        return new ByteArrayOutputStream();
    }
    @Provides
    public IExtendedUrl provideExtendedUrl(){
        return new ExtendedURL();
    }
    @Provides
    public IExtendedBufferReader provideExtendedBufferReader(){
        return new ExtendedBufferReader();
    }
}
