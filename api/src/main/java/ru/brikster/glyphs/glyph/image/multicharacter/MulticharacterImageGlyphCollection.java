package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import ru.brikster.glyphs.glyph.space.SpacesGlyph;
import team.unnamed.creative.texture.Texture;

import java.util.ArrayList;
import java.util.List;

public interface MulticharacterImageGlyphCollection extends ResourceProducer {

    static @NotNull MulticharacterImageGlyphCollection of(@NotNull Key fontKey,
                                                          @NotNull Texture texture,
                                                          @NotNull TextureProperties properties,
                                                          @NotNull List<@NotNull String> charactersMapping) {
        return new MulticharacterImageGlyphCollectionImpl(fontKey, texture, properties, charactersMapping);
    }

    static @NotNull MulticharacterImageGlyphCollection of(@NotNull Texture texture,
                                                          @NotNull TextureProperties properties,
                                                          @NotNull List<@NotNull String> charactersMapping) {
        return of(Glyph.DEFAULT_FONT_KEY, texture, properties, charactersMapping);
    }

    @NotNull AppendableGlyph translate(@NotNull Character character, @Nullable TextColor color) throws IllegalArgumentException;

    default @NotNull List<@NotNull AppendableGlyph> translate(@NotNull String text, @Nullable TextColor color) throws IllegalArgumentException {
        List<AppendableGlyph> glyphs = new ArrayList<>();
        for (char character : text.toCharArray()) {
            if (character == ' ') {
                glyphs.add(SpacesGlyph.DEFAULT_SPACE_GLYPH);
            } else {
                glyphs.add(translate(character, color));
            }
        }
        return glyphs;
    }

    default @NotNull AppendableGlyph translate(@NotNull Character character) throws IllegalArgumentException {
        return translate(character, null);
    }

    default @NotNull List<@NotNull AppendableGlyph> translate(@NotNull String text) throws IllegalArgumentException {
        return translate(text, null);
    }

}
