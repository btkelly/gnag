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

import com.btkelly.gnag.utils.Logger;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;

import static com.squareup.okhttp.logging.HttpLoggingInterceptor.Level.*;

/**
 * Created by bobbake4 on 12/3/15.
 */
public class LoggingInterceptor implements Interceptor {

    private final HttpLoggingInterceptor httpLoggingInterceptor;

    public LoggingInterceptor() {
        HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (Logger.isDebugLoggingEnabled()) {
                    Logger.logDebug(message);
                } else if (Logger.isInfoLoggingEnabled()){
                    Logger.logInfo(message);
                }
            }
        };
        this.httpLoggingInterceptor = new HttpLoggingInterceptor(logger);
        this.httpLoggingInterceptor.setLevel(Logger.isDebugLoggingEnabled() ? BODY : Logger.isInfoLoggingEnabled() ? BASIC : NONE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return httpLoggingInterceptor.intercept(chain);
    }

}
