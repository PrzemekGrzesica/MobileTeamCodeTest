package com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

public interface IMySwipeRefreshLayout extends OnRefreshListener {
    void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout);
}
