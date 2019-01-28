/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.btkelly.gnag.api;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

/**
 * Created by bobbake4 on 12/3/15.
 */
public class AuthInterceptor implements Interceptor {

  @NotNull
  private final String authToken;

  public AuthInterceptor(@NotNull final String authToken) {
    this.authToken = authToken;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    final Request request = chain.request()
                                 .newBuilder()
                                 .addHeader("Authorization", "token " + authToken)
                                 .build();

    return chain.proceed(request);
  }
}
