/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
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
