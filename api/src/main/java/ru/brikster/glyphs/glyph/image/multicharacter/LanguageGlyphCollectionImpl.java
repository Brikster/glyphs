package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import java.util.*;

public class LanguageGlyphCollectionImpl implements LanguageGlyphCollection {

    private final Key fontKey;
    private final Texture texture;

    private final Map<TextureProperties, MulticharacterImageGlyphCollection> propertiesToMulticharacterMap = new HashMap<>();

    private Set<FontProvider> fontProviders;

    LanguageGlyphCollectionImpl(
            Key fontKey,
            Texture texture,
            List<TextureProperties> propertiesList,
            List<String> charactersMapping
    ) {
        this.fontKey = fontKey;
        this.texture = texture;

        for (TextureProperties properties : propertiesList) {
            propertiesToMulticharacterMap.put(properties, MulticharacterImageGlyphCollection.of(fontKey, texture, properties, charactersMapping));
        }
    }


    @Override
    public @NotNull Key fontKey() {
        return fontKey;
    }

    @Override
    public boolean produced() {
        return fontProviders != null;
    }

    @Override
    public void produceResources(ArbitraryCharacterFactory characterFactory) throws ResourceAlreadyProducedException {
        if (fontProviders != null) {
            throw new ResourceAlreadyProducedException();
        }

        fontProviders = new HashSet<>();

        for (var entry : propertiesToMulticharacterMap.entrySet()) {
            entry.getValue().produceResources(characterFactory);
            fontProviders.addAll(entry.getValue().fontProviders());
        }
    }

    @Override
    public @NotNull Collection<@NotNull FontProvider> fontProviders() throws ResourceNotProducedException {
        return fontProviders;
    }

    @Override
    public @NotNull Collection<@NotNull Texture> textures() throws ResourceNotProducedException {
        return Collections.singleton(texture);
    }

    @Override
    public @NotNull AppendableGlyph translate(int height, int ascent, @NotNull Character character, @Nullable TextColor color) throws IllegalArgumentException {
        TextureProperties properties = new TextureProperties(height, ascent);
        if (!propertiesToMulticharacterMap.containsKey(properties)) {
            throw new IllegalArgumentException();
        }
        return propertiesToMulticharacterMap.get(properties).translate(character, color);
    }

    @Override
    public @NotNull List<@NotNull AppendableGlyph> translate(int height, int ascent, @NotNull String text, @Nullable TextColor color) throws IllegalArgumentException {
        TextureProperties properties = new TextureProperties(height, ascent);
        if (!propertiesToMulticharacterMap.containsKey(properties)) {
            throw new IllegalArgumentException();
        }
        return propertiesToMulticharacterMap.get(properties).translate(text, color);
    }

}
