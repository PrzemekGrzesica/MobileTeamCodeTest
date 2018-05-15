package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.BitmapFactoryOptions;
import com.grzesica.przemek.artistlist.Container.ExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.ExtendedURL;
import com.grzesica.przemek.artistlist.Container.IBitmapFactoryOptions;
import com.grzesica.przemek.artistlist.Container.IExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.IExtendedUrl;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class HttpHandlerModule {
    @Provides
    public IHttpHandler provideHttpHandler(@Named("stringBuilder")Appendable strBuilder, IBitmapFactoryOptions bitmapFactoryOptions, OutputStream outputStream,
                                           IExtendedUrl extendedUrl, IExtendedBufferReader extendedBufferReader) {
        return new HttpHandler(strBuilder, bitmapFactoryOptions, outputStream, extendedUrl, extendedBufferReader);
    }

    @Provides
    @Named("stringBuffer")
    public Appendable provideStringBuffer(){
        return new StringBuffer();
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
    public IBitmapFactoryOptions provideBitmapFactoryOptions(){
        return new BitmapFactoryOptions();
    }

    @Provides
    public IExtendedBufferReader provideExtendedBufferReader(){
        return new ExtendedBufferReader();
    }

    @Provides
    @Named("stringBuilder")
    public Appendable provideStringBuilder(){
        return new StringBuilder();
    }
}
