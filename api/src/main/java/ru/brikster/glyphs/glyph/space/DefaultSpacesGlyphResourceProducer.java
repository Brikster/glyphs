package ru.brikster.glyphs.glyph.space;

import java.util.*;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

public class DefaultSpacesGlyphResourceProducer extends AbstractSpacesGlyphResourceProducer {

    private final Key textureKey;
    private final Writable writable;

    private Set<Texture> textures;
    private Set<FontProvider> fontProviders;

    public DefaultSpacesGlyphResourceProducer(Key fontKey, Key textureKey, Writable writable) {
        super(fontKey);
        this.textureKey = textureKey;
        this.writable = writable;
    }

    @Override
    public boolean produced() {
        return textures != null;
    }

    @Override
    public void produceResources(ArbitraryCharacterFactory characterFactory) {
        if (textures != null) {
            throw new ResourceAlreadyProducedException();
        }

        this.mapping = new HashMap<>();

        Set<FontProvider> fontProviders = new HashSet<>();

        for (int length = 1; length <= 2048; length *= 2) {
            fontProviders.add(prepareBuilder(characterFactory, length).build());
            fontProviders.add(prepareBuilder(characterFactory, length * (-1)).build());
        }

        this.textures = Collections.singleton(Texture.of(textureKey, writable));
        this.fontProviders = fontProviders;
    }

    @Override
    public @NotNull Collection<@NotNull FontProvider> fontProviders() throws ResourceNotProducedException {
        if (fontProviders == null) {
            throw new ResourceNotProducedException();
        }
        return fontProviders;
    }

    @Override
    public @NotNull Collection<@NotNull Texture> textures() throws ResourceNotProducedException {
        if (textures == null) {
            throw new ResourceNotProducedException();
        }
        return textures;
    }

    @NotNull private BitMapFontProvider.Builder prepareBuilder(ArbitraryCharacterFactory characterFactory, int length) {
        var fontProviderBuilder = FontProvider.bitMap();

        char character = characterFactory.nextCharacter();

        fontProviderBuilder.characters(String.valueOf(character));
        fontProviderBuilder.file(textureKey);

        if (length > 0) {
            fontProviderBuilder.height(length - 1);
            fontProviderBuilder.ascent(0);
        } else {
            fontProviderBuilder.height(length - 2);
            fontProviderBuilder.ascent(-32768);
        }

        mapping.put(length, character);

        return fontProviderBuilder;
    }
}
