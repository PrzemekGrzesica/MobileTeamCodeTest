package com.grzesica.przemek.artistlist.Container;

import android.content.Context;

import com.grzesica.przemek.artistlist.Model.UpdatesCheck;

/**
 * An abstraction for dependency injection through UpdatesCheck class constructor
 * by means of a builder.
 */

public interface IUpdatesCheckDIBuilder {
    UpdatesCheck build(Context context);
}
