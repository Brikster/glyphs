package ru.brikster.glyphs.compile;

import java.util.Arrays;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.resource.FileResource;

public interface GlyphCompiler {

    static GlyphCompiler instance() {
        return new DefaultGlyphCompiler();
    }

    @NotNull Collection<@NotNull FileResource> compile(
            @NotNull Collection<@NotNull ResourceProducer> resourceProducerCollection);

    default @NotNull Collection<@NotNull FileResource> compile(@NotNull ResourceProducer... resourceProducer) {
        return compile(Arrays.asList(resourceProducer));
    }
}
