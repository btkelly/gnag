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
package com.btkelly.gnag.utils.diffparser;

import com.github.stkent.githubdiffparser.GitHubDiffParser;
import com.github.stkent.githubdiffparser.models.Diff;
import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class DiffParserConverterFactory extends Converter.Factory {

  private final GitHubDiffParser diffParser;

  private DiffParserConverterFactory(@NotNull final GitHubDiffParser diffParser) {
    this.diffParser = diffParser;
  }

  public static DiffParserConverterFactory create() {
    return new DiffParserConverterFactory(new GitHubDiffParser());
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    final Type diffListType = new TypeToken<List<Diff>>() {
    }.getType();

    if (diffListType.equals(type)) {
      return new DiffParserResponseBodyConverter(diffParser);
    }

    return null;
  }

  @Override
  public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    return null;
  }

}
