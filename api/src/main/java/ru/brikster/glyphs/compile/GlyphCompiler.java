package ru.brikster.glyphs.compile;

import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.file.FileResource;

import java.util.Arrays;
import java.util.Collection;

public interface GlyphCompiler {

    @NotNull Collection<@NotNull FileResource> compile(@NotNull Collection<@NotNull ResourceProducer> resourceProducerCollection);

    default @NotNull Collection<@NotNull FileResource> compile(@NotNull ResourceProducer... resourceProducer) {
        return compile(Arrays.asList(resourceProducer));
    }

    static GlyphCompiler instance() {
        return new DefaultGlyphCompiler();
    }

}
