package com.grzesica.przemek.artistlist.Viewer;

import javax.inject.Singleton;

@Singleton
public class GuiContainer implements IGuiContainer {

    private byte[] mImageByteArray;
    private boolean mServiceFlag = false;
    private boolean mAlbumsFetchingServiceFlag = false;
    private boolean mArtistFetchingServiceFlag = false;

    @Override
    public void setImageByteArray(byte[] imageByteArray) {
        mImageByteArray = imageByteArray;
    }

    @Override
    public byte[] getImageByteArray() {
        return mImageByteArray;
    }

    @Override
    public void setAlbumsFetchingServiceFlag(boolean serviceFlag) {
        this.mAlbumsFetchingServiceFlag = serviceFlag;
    }

    @Override
    public void setArtistFetchingServiceFlag(boolean serviceFlag) {
        this.mArtistFetchingServiceFlag = serviceFlag;
    }

    @Override
    public boolean getFetchingServiceFlag() {
        mServiceFlag = mArtistFetchingServiceFlag | mAlbumsFetchingServiceFlag;
        return mServiceFlag;
    }
}
