package com.grzesica.przemek.artistlist.Viewer;

import javax.inject.Singleton;

@Singleton
public class GuiContainer implements IGuiContainer {

    private byte[] mImageByteArray;
    private boolean mActivityServiceFlag = false;
    private boolean mServiceFlag = false;

    @Override
    public void setImageByteArray(byte[] imageByteArray) {
        mImageByteArray = imageByteArray;
    }

    @Override
    public byte[] getImageByteArray() {
        return mImageByteArray;
    }

    public void setServiceFlag(boolean serviceFlag){
        this.mServiceFlag = serviceFlag;
    }

    public boolean getServiceFlag(){
        return mServiceFlag;
    }

    public void setActivityServiceFlag(boolean activityServiceFlag){
        this.mActivityServiceFlag = activityServiceFlag;
    }

    public boolean getActivityServiceFlag(){
        return mActivityServiceFlag;
    }
}
