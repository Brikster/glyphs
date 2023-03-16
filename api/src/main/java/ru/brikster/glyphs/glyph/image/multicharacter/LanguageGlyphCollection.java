package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import team.unnamed.creative.texture.Texture;

import java.util.List;

public interface LanguageGlyphCollection extends ResourceProducer {

    static @NotNull LanguageGlyphCollection of(@NotNull Key fontKey,
                                               @NotNull Texture texture,
                                               @NotNull List<@NotNull TextureProperties> propertiesList,
                                               @NotNull List<@NotNull String> charactersMapping) {
        return new LanguageGlyphCollectionImpl(fontKey, texture, propertiesList, charactersMapping);
    }

    @Deprecated(forRemoval = true)
    static @NotNull LanguageGlyphCollection of(@NotNull Texture texture,
                                               @NotNull List<@NotNull TextureProperties> propertiesList,
                                               @NotNull List<@NotNull String> charactersMapping) {
        return of(Glyph.DEFAULT_FONT_KEY, texture, propertiesList, charactersMapping);
    }

    @NotNull AppendableGlyph translate(int height, int ascent, @NotNull Character character, @Nullable TextColor color) throws IllegalArgumentException;

    @NotNull List<@NotNull AppendableGlyph> translate(int height, int ascent, @NotNull String text, @Nullable TextColor color) throws IllegalArgumentException;

    @NotNull List<@NotNull AppendableGlyph> translate(int height, int ascent, @NotNull Component component) throws IllegalArgumentException;

    default @NotNull AppendableGlyph translate(int height, int ascent, @NotNull Character character) throws IllegalArgumentException {
        return translate(height, ascent, character, null);
    }

    default @NotNull List<@NotNull AppendableGlyph> translate(int height, int ascent, @NotNull String text) throws IllegalArgumentException {
        return translate(height, ascent, text, null);
    }

}
