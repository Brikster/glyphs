package ru.brikster.glyphs.glyph.image.multicharacter;

import java.util.*;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import ru.brikster.glyphs.util.kyori.KyoriUtil;
import ru.brikster.glyphs.util.kyori.KyoriUtil.ColoredPartsFlattenerListener.ColoredComponentTextPart;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

public class LanguageGlyphCollectionImpl implements LanguageGlyphCollection {

    private final Key fontKey;
    private final Texture texture;

    private final Map<TextureProperties, MulticharacterImageGlyphCollection> propertiesToMulticharacterMap =
            new HashMap<>();

    private Set<FontProvider> fontProviders;

    LanguageGlyphCollectionImpl(
            Key fontKey, Texture texture, List<TextureProperties> propertiesList, List<String> charactersMapping) {
        this.fontKey = fontKey;
        this.texture = texture;

        for (TextureProperties properties : propertiesList) {
            propertiesToMulticharacterMap.put(
                    properties, MulticharacterImageGlyphCollection.of(fontKey, texture, properties, charactersMapping));
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

    @NotNull private MulticharacterImageGlyphCollection getGlyphCollection(int height, int ascent) {
        TextureProperties properties = new TextureProperties(height, ascent);
        MulticharacterImageGlyphCollection glyphCollection = propertiesToMulticharacterMap.get(properties);
        if (glyphCollection == null) {
            throw new IllegalArgumentException("Font with " + properties + " not found");
        }
        return glyphCollection;
    }

    @Override
    public @NotNull AppendableGlyph translate(
            int height, int ascent, @NotNull Character character, @Nullable TextColor color)
            throws IllegalArgumentException {
        MulticharacterImageGlyphCollection glyphCollection = getGlyphCollection(height, ascent);
        return glyphCollection.translate(character, color);
    }

    @Override
    public @NotNull List<@NotNull AppendableGlyph> translate(
            int height, int ascent, @NotNull String text, @Nullable TextColor color) throws IllegalArgumentException {
        MulticharacterImageGlyphCollection glyphCollection = getGlyphCollection(height, ascent);
        return Collections.unmodifiableList(glyphCollection.translate(text, color));
    }

    @Override
    public @NotNull List<@NotNull AppendableGlyph> translate(int height, int ascent, @NotNull Component component)
            throws IllegalArgumentException {
        MulticharacterImageGlyphCollection glyphCollection = getGlyphCollection(height, ascent);
        List<AppendableGlyph> result = new ArrayList<>();
        List<? extends ColoredComponentTextPart> textAndColors = KyoriUtil.toColoredParts(component);

        for (ColoredComponentTextPart parts : textAndColors) {
            List<AppendableGlyph> glyphList = glyphCollection.translate(parts.text(), parts.color());
            result.addAll(glyphList);
        }

        return Collections.unmodifiableList(result);
    }
}
