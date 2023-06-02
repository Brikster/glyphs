package ru.brikster.glyphs.glyph.space;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.Glyph;
import team.unnamed.creative.base.Writable;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SpacesGlyph implements Glyph {

    private static final Key DEFAULT_SPACE_TEXTURE_KEY = Key.key(Glyph.DEFAULT_NAMESPACE, "space");
    public static final @NotNull DefaultSpaceGlyph DEFAULT_SPACE_GLYPH = new DefaultSpaceGlyph();
    private final Key key;
    private final char[] characters;
    private final int length;

    public static @NotNull SpacesGlyphResourceProducer create(
            @NotNull Key fontKey, @NotNull Key textureKey, @NotNull Writable spacesWritable) {
        return new DefaultSpacesGlyphResourceProducer(fontKey, textureKey, spacesWritable);
    }

    public static SpacesGlyphResourceProducer create(@NotNull Writable spacesWritable) {
        return create(Glyph.DEFAULT_SPACES_FONT_KEY, DEFAULT_SPACE_TEXTURE_KEY, spacesWritable);
    }

    @Override
    public @NotNull Component toAdventure() {
        return Component.text(new String(characters)).font(key);
    }

    @Override
    public int width() {
        return length;
    }
}
