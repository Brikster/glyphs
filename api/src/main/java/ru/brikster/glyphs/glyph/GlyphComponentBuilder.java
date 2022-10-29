package ru.brikster.glyphs.glyph;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;

public interface GlyphComponentBuilder {

    static @NotNull GlyphComponentBuilder universal(SpacesGlyphResourceProducer spacesProducer) {
        return new GlyphComponentBuilderImpl(spacesProducer);
    }

    static @NotNull GlyphComponentBuilder gui(SpacesGlyphResourceProducer spacesProducer) {
        return universal(spacesProducer)
                .append(0, spacesProducer.translate(-8));
    }

    @NotNull GlyphComponentBuilder append(int position, @NotNull Glyph glyph);

    default @NotNull GlyphComponentBuilder append(@NotNull Glyph glyph) {
        return append(0, glyph);
    }

    @NotNull Component build();

}
