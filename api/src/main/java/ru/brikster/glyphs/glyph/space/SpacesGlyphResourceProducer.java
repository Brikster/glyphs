package ru.brikster.glyphs.glyph.space;

import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;

public interface SpacesGlyphResourceProducer extends ResourceProducer {

    Glyph translate(int length) throws ResourceNotProducedException;
}
