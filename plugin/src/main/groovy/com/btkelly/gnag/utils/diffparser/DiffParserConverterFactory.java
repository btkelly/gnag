package com.btkelly.gnag.utils.diffparser;

import com.google.common.reflect.TypeToken;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.wickedsource.diffparser.api.DiffParser;
import org.wickedsource.diffparser.api.UnifiedDiffParser;
import org.wickedsource.diffparser.api.model.Diff;
import retrofit2.Converter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

public class DiffParserConverterFactory extends Converter.Factory {

    public static DiffParserConverterFactory create() {
        return new DiffParserConverterFactory(new UnifiedDiffParser());
    }

    private final DiffParser diffParser;

    private DiffParserConverterFactory(@NotNull final DiffParser diffParser) {
        this.diffParser = diffParser;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        final Type diffListType = new TypeToken<List<Diff>>() {}.getType();

        if (diffListType.equals(type)) {
            return new DiffParserResponseBodyConverter(diffParser);
        }

        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return null;
    }

}
