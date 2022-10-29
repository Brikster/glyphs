package ru.brikster.glyphs.glyph.space;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import java.util.*;

public class DefaultSpacesGlyphResourceProducer extends AbstractSpacesGlyphResourceProducer {

    private final Writable writable;

    private Set<Texture> textures;
    private Set<FontProvider> fontProviders;

    public DefaultSpacesGlyphResourceProducer(Key key, Writable writable) {
        super(key);
        this.writable = writable;
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

        this.textures = Collections.singleton(Texture.of(key(), writable));
        this.fontProviders = fontProviders;
    }

    @NotNull
    private BitMapFontProvider.Builder prepareBuilder(ArbitraryCharacterFactory characterFactory, int length) {
        var fontProviderBuilder = FontProvider.bitMap();

        char character = characterFactory.nextCharacter();

        fontProviderBuilder.characters(String.valueOf(character));
        fontProviderBuilder.file(key());
        fontProviderBuilder.ascent(-32768);

        if (length > 0) {
            fontProviderBuilder.height(length - 1);
        } else {
            fontProviderBuilder.ascent((length + 2) * (-1));
        }

        mapping.put(length, character);

        return fontProviderBuilder;
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

}
