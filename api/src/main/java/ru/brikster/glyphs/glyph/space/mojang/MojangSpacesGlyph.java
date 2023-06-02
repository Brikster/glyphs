package ru.brikster.glyphs.glyph.space.mojang;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;

public interface MojangSpacesGlyph extends Glyph, ResourceProducer {

    static @NotNull SpacesGlyphResourceProducer create(@NotNull Key key) {
        return new MojangSpacesGlyphResourceProducer(key);
    }

    static SpacesGlyphResourceProducer create() {
        return create(Glyph.DEFAULT_SPACES_FONT_KEY);
    }
}
