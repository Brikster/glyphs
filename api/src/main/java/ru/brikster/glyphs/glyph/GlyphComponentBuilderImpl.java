package ru.brikster.glyphs.glyph;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class GlyphComponentBuilderImpl implements GlyphComponentBuilder {

    private final SpacesGlyphResourceProducer spacesProducer;

    private final List<Glyph> glyphs = new ArrayList<>();

    @Override
    public @NotNull GlyphComponentBuilder append(int position, @NotNull Glyph glyph) {
        glyphs.add(spacesProducer.translate(position));
        glyphs.add(glyph);
//        glyphs.add()
        return this;
    }

    @Override
    public @NotNull Component build() {
        var componentBuilder = Component.text();

        for (Glyph glyph : glyphs) {
            componentBuilder.append(glyph.toAdventure());
        }

        return componentBuilder.build();
    }

}
