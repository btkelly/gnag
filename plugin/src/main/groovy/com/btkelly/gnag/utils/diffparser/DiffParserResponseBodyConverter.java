/**
 * Copyright 2016 Bryan Kelly
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.btkelly.gnag.utils.diffparser;

import com.github.stkent.githubdiffparser.GitHubDiffParser;
import com.github.stkent.githubdiffparser.models.Diff;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Converter;

public class DiffParserResponseBodyConverter implements Converter<ResponseBody, List<Diff>> {

  @NotNull
  private final GitHubDiffParser diffParser;

  protected DiffParserResponseBodyConverter(final @NotNull GitHubDiffParser diffParser) {
    this.diffParser = diffParser;
  }

  @Override
  public List<Diff> convert(final ResponseBody value) throws IOException {
    try {
      final String rawResponse = value.string();
      return diffParser.parse(rawResponse.getBytes());
    } catch (final IllegalStateException e) {
      return new ArrayList<>();
    } finally {
      value.close();
    }
  }

}
