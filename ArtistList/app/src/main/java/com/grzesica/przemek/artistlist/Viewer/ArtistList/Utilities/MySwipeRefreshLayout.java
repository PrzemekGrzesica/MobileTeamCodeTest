package com.grzesica.przemek.artistlist.Viewer.ArtistList.Utilities;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.grzesica.przemek.artistlist.Container.IExtendedHandler;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
public class MySwipeRefreshLayout implements IMySwipeRefreshLayout{

    private Handler mHandler;
    private Runnable mSwipeRefreshRunnable;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject
    public MySwipeRefreshLayout(IExtendedHandler handler, @Named("swipeRefresh") Runnable runnable){
        this.mHandler = (Handler) handler;
        this.mSwipeRefreshRunnable = runnable;
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(mSwipeRefreshRunnable, 500);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.mSwipeRefreshLayout = swipeRefreshLayout;
    }
}
