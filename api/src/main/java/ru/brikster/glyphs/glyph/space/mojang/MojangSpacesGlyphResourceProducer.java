package ru.brikster.glyphs.glyph.space.mojang;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import ru.brikster.glyphs.glyph.space.AbstractSpacesGlyphResourceProducer;
import team.unnamed.creative.font.FontProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class MojangSpacesGlyphResourceProducer extends AbstractSpacesGlyphResourceProducer {

    private Set<FontProvider> fontProviders;

    public MojangSpacesGlyphResourceProducer(Key key) {
        super(key);
    }

    @Override
    public void produceResources(ArbitraryCharacterFactory characterFactory) {
        if (fontProviders != null) {
            throw new ResourceAlreadyProducedException();
        }

        mapping = new HashMap<>();

        var fontProviderBuilder = FontProvider.space();

        for (int length = 1; length <= 2048; length++) {
            fontProviderBuilder.advance(retrieveCharacter(characterFactory, length), length);
            fontProviderBuilder.advance(retrieveCharacter(characterFactory, length * (-1)), length * (-1));
        }

        this.fontProviders = Collections.singleton(fontProviderBuilder.build());
    }

    @Override
    public @NotNull Collection<@NotNull FontProvider> fontProviders() throws ResourceNotProducedException {
        if (fontProviders == null) {
            throw new ResourceNotProducedException();
        }
        return fontProviders;
    }

    private Character retrieveCharacter(ArbitraryCharacterFactory characterFactory, int length) {
        char character = characterFactory.nextCharacter();
        mapping.put(length, character);
        return character;
    }

}
