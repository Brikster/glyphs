package ru.brikster.glyphs.pack;

import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.image.multicharacter.LanguageGlyphCollection;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;

public interface ResourceIdentifier<T extends @NotNull ResourceProducer> {

    @NotNull StringIdentifier<@NotNull SpacesGlyphResourceProducer> SPACES =
            StringIdentifier.of("spaces", SpacesGlyphResourceProducer.class);

    @NotNull StringIdentifier<@NotNull LanguageGlyphCollection> MINECRAFT_FONT =
            StringIdentifier.of("minecraft_font", LanguageGlyphCollection.class);

    String STRING_IDENTIFIER_NAMESPACE = "glyphs";

    @NotNull String key();

    @NotNull Class<T> getType();
}
