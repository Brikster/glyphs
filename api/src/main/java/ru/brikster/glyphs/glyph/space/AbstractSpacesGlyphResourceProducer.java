package ru.brikster.glyphs.glyph.space;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.EmptyGlyph;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public abstract class AbstractSpacesGlyphResourceProducer implements SpacesGlyphResourceProducer {

    private final Key key;

    protected Map<Integer, Character> mapping;

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public Glyph translate(int length) throws ResourceNotProducedException {
        if (mapping == null) {
            throw new ResourceNotProducedException();
        }

        if (length == 0) {
            return EmptyGlyph.INSTANCE;
        }

        if (!mapping.containsKey(length)) {
            throw new IllegalArgumentException();
        }

        return new SpacesGlyph(key, mapping.get(length), length);
    }

}
