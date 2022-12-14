package ru.brikster.glyphs.glyph;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmptyGlyph implements AppendableGlyph {

    public static final EmptyGlyph INSTANCE = new EmptyGlyph();

    @Override
    public @NotNull Component toAdventure() throws ResourceNotProducedException {
        return Component.text("");
    }

    @Override
    public int width() {
        return 0;
    }

}
