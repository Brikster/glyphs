package ru.brikster.glyphs.glyph;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;

public interface Glyph {

    Key DEFAULT_KEY = Key.key("glyphs", "default");

    @NotNull Component toAdventure() throws ResourceNotProducedException;

    int width();

}
