package com.grzesica.przemek.artistlist.DI;

import com.grzesica.przemek.artistlist.Model.IMD5checkSum;
import com.grzesica.przemek.artistlist.Model.MD5checkSum;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public class MD5checkSumModule {
    @Provides
    public IMD5checkSum provideMD5checkSum(@Named("stringBuffer")Appendable stringBuffer){
        return new MD5checkSum(stringBuffer);
    }
}
