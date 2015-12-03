package com.btkelly.gnag.api;

import com.btkelly.gnag.GnagPluginExtension;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by bobbake4 on 12/3/15.
 */
public class AuthInterceptor implements Interceptor {

    private final GnagPluginExtension gnagPluginExtension;

    public AuthInterceptor(GnagPluginExtension gnagPluginExtension) {
        this.gnagPluginExtension = gnagPluginExtension;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "token " + gnagPluginExtension.getGitHubAuthToken())
                .build();

        return chain.proceed(request);
    }
}
