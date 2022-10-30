package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Nullable;

public interface ColorableGlyph {

    @Nullable TextColor color();

    void updateColor(@Nullable TextColor color);

}
