package com.grzesica.przemek.artistlist.Viewer;

public interface IGuiContainer {
    void setImageByteArray(byte[] imageByteArray);
    byte[] getImageByteArray();
    boolean getFetchingServiceFlag();
    void setArtistFetchingServiceFlag(boolean serviceFlag);
    void setAlbumsFetchingServiceFlag(boolean serviceFlag);
}
