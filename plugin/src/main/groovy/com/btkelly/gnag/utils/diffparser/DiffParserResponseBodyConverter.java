package com.btkelly.gnag.utils.diffparser;

import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.wickedsource.diffparser.api.DiffParser;
import org.wickedsource.diffparser.api.model.Diff;
import retrofit2.Converter;

import java.io.IOException;
import java.util.List;

public class DiffParserResponseBodyConverter implements Converter<ResponseBody, List<Diff>> {

    @NotNull
    private final DiffParser diffParser;

    protected DiffParserResponseBodyConverter(@NotNull final DiffParser diffParser) {
        this.diffParser = diffParser;
    }

    @Override
    public List<Diff> convert(final ResponseBody value) throws IOException {
        try {
            return diffParser.parse(value.byteStream());
        } catch (final IllegalStateException e) {
            return null;
        } finally {
            value.close();
        }
    }

}
