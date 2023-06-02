package ru.brikster.glyphs.pack;

import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.image.ImageGlyph;

public interface ImageResourceIdentifier extends ResourceIdentifier<@NotNull ImageGlyph> {

    @Override
    default @NotNull Class<@NotNull ImageGlyph> getType() {
        return ImageGlyph.class;
    }
}
