package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Container.IBitmapFactoryOptions;
import com.grzesica.przemek.artistlist.Container.IExtendedBufferReader;
import com.grzesica.przemek.artistlist.Container.IExtendedUrl;
import com.grzesica.przemek.artistlist.Model.HttpHandler;
import com.grzesica.przemek.artistlist.Model.IHttpHandler;

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
}
