package ru.brikster.glyphs.compile;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import java.util.Collection;
import java.util.Collections;

public interface ResourceProducer {

    @NotNull Key fontKey();

    boolean produced();

    void produceResources(ArbitraryCharacterFactory characterFactory) throws ResourceAlreadyProducedException;

    @NotNull Collection<@NotNull FontProvider> fontProviders() throws ResourceNotProducedException;

    default @NotNull Collection<@NotNull Texture> textures() throws ResourceNotProducedException {
        return Collections.emptyList();
    }

}
