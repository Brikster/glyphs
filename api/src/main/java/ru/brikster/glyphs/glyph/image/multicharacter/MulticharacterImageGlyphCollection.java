package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import team.unnamed.creative.texture.Texture;

import java.util.ArrayList;
import java.util.List;

public interface MulticharacterImageGlyphCollection extends ResourceProducer {

    static @NotNull MulticharacterImageGlyphCollection of(@NotNull Key key,
                                                          @NotNull Texture texture,
                                                          @NotNull TextureProperties properties,
                                                          @NotNull List<String> charactersMapping) {
        return new MulticharacterImageGlyphCollectionImpl(key, texture, properties, charactersMapping);
    }

    static @NotNull MulticharacterImageGlyphCollection of(@NotNull Texture texture,
                                                          @NotNull TextureProperties properties,
                                                          @NotNull List<String> charactersMapping) {
        return of(Glyph.DEFAULT_KEY, texture, properties, charactersMapping);
    }

    @NotNull PreparedImageGlyph translate(@NotNull Character character) throws IllegalArgumentException;

    default @NotNull List<@NotNull PreparedImageGlyph> translate(@NotNull String text) throws IllegalArgumentException {
        List<PreparedImageGlyph> glyphs = new ArrayList<>();
        for (char character : text.toCharArray()) {
            if (character == ' ') {
                glyphs.add(new PreparedImageGlyph(key(), ' ', 5));
            } else {
                glyphs.add(translate(character));
            }
        }
        return glyphs;
    }

}
