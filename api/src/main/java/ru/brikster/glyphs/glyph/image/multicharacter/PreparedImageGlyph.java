package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.Glyph;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PreparedImageGlyph implements Glyph {

    private final Key key;
    private final Character character;
    private final int width;

    @Override
    public @NotNull Component toAdventure() {
        return Component.text(character).font(key);
    }

    @Override
    public int width() {
        return width;
    }

}
