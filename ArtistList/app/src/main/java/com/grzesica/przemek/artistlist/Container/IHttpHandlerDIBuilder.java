package com.grzesica.przemek.artistlist.Container;

import com.grzesica.przemek.artistlist.Model.HttpHandler;

/**
 * An abstraction for dependency injection through HttpHandler class constructor
 * by means of a builder.
 */
public interface IHttpHandlerDIBuilder {
    HttpHandler build();
}
