package com.grzesica.przemek.artistlist.Adapter;

import android.database.Cursor;

public interface ICursorManager {
    Cursor getArtistListCursor(int position);
    Cursor getAlbumsListCursor(String artistId);
}
