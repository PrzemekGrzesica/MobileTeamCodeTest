package com.grzesica.przemek.artistlist.Model.Utilities;

import android.content.Context;
import android.widget.Toast;

import javax.inject.Inject;

public class ToastRunnable implements IToastRunnable {

    private String mToastText;
    private Context mContext;

    @Inject
    public ToastRunnable(Context context){
        this.mContext = context;
    }

    @Override
    public void run() {
        Toast.makeText(mContext, mToastText, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setToastText(String toastText) {
        this.mToastText = toastText;
    }
}
