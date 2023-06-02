package ru.brikster.glyphs.glyph;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GlyphComponentBuilderImpl implements GlyphComponentBuilder {

    private final SpacesGlyphResourceProducer spacesProducer;
    private final int initialPosition;
    private final Component baseComponent;

    private final List<Glyph> glyphs = new ArrayList<>();

    private int previousElementsWidth;

    @Override
    public @NotNull GlyphComponentBuilder append(
            PositionType positionType, int position, @NotNull AppendableGlyph glyph) {
        if (positionType == PositionType.ABSOLUTE && previousElementsWidth != 0) {
            glyphs.add(spacesProducer.translate((-1) * previousElementsWidth));
            previousElementsWidth = 0;
        }

        if (position != 0) {
            glyphs.add(spacesProducer.translate(position));
        }

        glyphs.add(glyph);
        this.previousElementsWidth += position + glyph.width();

        return this;
    }

    @Override
    public @NotNull GlyphComponentBuilder append(
            PositionType positionType, int position, @NotNull List<? extends @NotNull AppendableGlyph> glyphList) {
        if (positionType == PositionType.ABSOLUTE && previousElementsWidth != 0) {
            glyphs.add(spacesProducer.translate((-1) * previousElementsWidth));
            previousElementsWidth = 0;
        }

        if (position != 0) {
            glyphs.add(spacesProducer.translate(position));
        }

        int width = 0;
        for (AppendableGlyph glyph : glyphList) {
            glyphs.add(glyph);
            width += glyph.width();
        }

        this.previousElementsWidth += position + width;

        return this;
    }

    @Override
    public @NotNull Component build(boolean keepInitialPosition) {
        if (keepInitialPosition) {
            previousElementsWidth += initialPosition;

            // Component should have zero width finally
            if (previousElementsWidth != 0) {
                glyphs.add(spacesProducer.translate((-1) * previousElementsWidth));
            }
        }

        var component = baseComponent;

        if (initialPosition != 0) {
            component =
                    component.append(spacesProducer.translate(initialPosition).toAdventure());
        }

        for (Glyph glyph : glyphs) {
            component = component.append(glyph.toAdventure());
        }

        return component;
    }
}
