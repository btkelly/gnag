package com.btkelly.gnag.api;

import com.btkelly.gnag.utils.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

/**
 * Created by bobbake4 on 12/3/15.
 */
public class LoggingInterceptor implements Interceptor {

    private final HttpLoggingInterceptor httpLoggingInterceptor;

    public LoggingInterceptor() {
        this.httpLoggingInterceptor = new HttpLoggingInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (Logger.isDebugLogEnabled()) {
            return httpLoggingInterceptor.intercept(chain);
        } else {
            return chain.proceed(chain.request());
        }
    }
}
