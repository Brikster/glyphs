package ru.brikster.glyphs.glyph;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;

public class EmptyGlyph implements Glyph {

    public static final Glyph INSTANCE = new EmptyGlyph();

    @Override
    public @NotNull Component toAdventure() throws ResourceNotProducedException {
        return Component.text("");
    }

    @Override
    public int width() {
        return 0;
    }

}
