package ru.brikster.glyphs.glyph;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;

import java.util.List;

public interface GlyphComponentBuilder {

    static @NotNull GlyphComponentBuilder universal(SpacesGlyphResourceProducer spacesProducer) {
        return new GlyphComponentBuilderImpl(spacesProducer, 0, Component.text(""));
    }

    static @NotNull GlyphComponentBuilder gui(SpacesGlyphResourceProducer spacesProducer) {
        return new GlyphComponentBuilderImpl(spacesProducer, -8, Component.text("", NamedTextColor.WHITE));
    }

    @NotNull GlyphComponentBuilder append(PositionType positionType, int position, @NotNull AppendableGlyph glyph);

    default @NotNull GlyphComponentBuilder append(PositionType positionType, @NotNull AppendableGlyph glyph) {
        return append(positionType, 0, glyph);
    }

    @NotNull GlyphComponentBuilder append(PositionType positionType, int position, @NotNull List<? extends @NotNull AppendableGlyph> glyphList);

    default @NotNull GlyphComponentBuilder append(PositionType positionType, @NotNull List<? extends @NotNull AppendableGlyph> glyphList) {
        return append(positionType, 0, glyphList);
    }

    default @NotNull GlyphComponentBuilder append(int position, @NotNull AppendableGlyph glyph) {
        return append(PositionType.ABSOLUTE, position, glyph);
    }

    // Append with default position type

    default @NotNull GlyphComponentBuilder append(@NotNull AppendableGlyph glyph) {
        return append(PositionType.ABSOLUTE, glyph);
    }

    default @NotNull GlyphComponentBuilder append(int position, @NotNull List<? extends @NotNull AppendableGlyph> glyphList) {
        return append(PositionType.ABSOLUTE, position, glyphList);
    }

    default @NotNull GlyphComponentBuilder append(@NotNull List<? extends @NotNull AppendableGlyph> glyphList) {
        return append(PositionType.ABSOLUTE, glyphList);
    }

    @NotNull Component build();

    enum PositionType {
        ABSOLUTE,
        RELATIVE
    }

}
