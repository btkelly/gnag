package com.btkelly.gnag.api;

import com.btkelly.gnag.utils.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;
import com.squareup.okhttp.logging.HttpLoggingInterceptor.Level;

import java.io.IOException;

import static com.squareup.okhttp.logging.HttpLoggingInterceptor.Level.BODY;
import static com.squareup.okhttp.logging.HttpLoggingInterceptor.Level.NONE;

/**
 * Created by bobbake4 on 12/3/15.
 */
public class LoggingInterceptor implements Interceptor {

    private final HttpLoggingInterceptor httpLoggingInterceptor;

    public LoggingInterceptor() {

        Level level;

        if (Logger.isDebugLogEnabled()) {
            level = BODY;
        } else {
            level = NONE;
        }

        this.httpLoggingInterceptor = new HttpLoggingInterceptor();
        this.httpLoggingInterceptor.setLevel(level);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return httpLoggingInterceptor.intercept(chain);
    }
}
