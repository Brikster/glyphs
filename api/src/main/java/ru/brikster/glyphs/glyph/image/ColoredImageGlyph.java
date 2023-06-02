package ru.brikster.glyphs.glyph.image;

import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.image.multicharacter.ColorableGlyph;

public interface ColoredImageGlyph extends AppendableGlyph, ColorableGlyph {

    ImageGlyph original();
}
