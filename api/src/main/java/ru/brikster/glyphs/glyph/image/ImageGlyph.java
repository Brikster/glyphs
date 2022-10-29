package ru.brikster.glyphs.glyph.image;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import team.unnamed.creative.texture.Texture;

public interface ImageGlyph extends AppendableGlyph, ResourceProducer {

    static @NotNull ImageGlyph of(@NotNull Key key,
                                  @NotNull Texture texture,
                                  @NotNull TextureProperties properties) {
        return new ImageGlyphImpl(key, texture, properties);
    }

    static @NotNull ImageGlyph of(@NotNull Texture texture,
                                  @NotNull TextureProperties properties) {
        return of(Glyph.DEFAULT_FONT_KEY, texture, properties);
    }

    @NotNull Character character() throws ResourceNotProducedException;

    @NotNull Texture texture();

    default @NotNull Component toAdventure() throws ResourceNotProducedException {
        return Component.text(character()).font(fontKey());
    }

}
