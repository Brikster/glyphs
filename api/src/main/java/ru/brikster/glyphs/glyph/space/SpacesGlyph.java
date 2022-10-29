package ru.brikster.glyphs.glyph.space;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.Glyph;
import team.unnamed.creative.base.Writable;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SpacesGlyph implements Glyph {

    private final Key key;
    private final Character character;
    private final int length;

    public static @NotNull SpacesGlyphResourceProducer create(@NotNull Key key, @NotNull Writable spacesWritable) {
        return new DefaultSpacesGlyphResourceProducer(key, spacesWritable);
    }

    public static SpacesGlyphResourceProducer create(@NotNull Writable spacesWritable) {
        return create(Glyph.DEFAULT_KEY, spacesWritable);
    }

    @Override
    public @NotNull Component toAdventure() {
        return Component.text(character).font(key);
    }

    @Override
    public int width() {
        return length;
    }

}
