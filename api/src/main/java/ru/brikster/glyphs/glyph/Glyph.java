package ru.brikster.glyphs.glyph;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;

public interface Glyph {

    String DEFAULT_NAMESPACE = "glyphs";

    @Deprecated(forRemoval = true)
    Key DEFAULT_FONT_KEY = Key.key(DEFAULT_NAMESPACE, "default");

    Key DEFAULT_SPACES_FONT_KEY = Key.key(DEFAULT_NAMESPACE, "spaces");

    int SEPARATOR_WIDTH = 1;

    @NotNull Component toAdventure() throws ResourceNotProducedException;

    int width();

}
