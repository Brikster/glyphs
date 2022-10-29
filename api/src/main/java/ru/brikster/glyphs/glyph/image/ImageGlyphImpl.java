package ru.brikster.glyphs.glyph.image;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import ru.brikster.glyphs.util.ImageUtil;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ImageGlyphImpl implements ImageGlyph {

    private final Key key;
    private final Texture texture;
    private final TextureProperties properties;

    private Character character;
    private Set<FontProvider> fontProviders;

    private int width = -1;

    @Override
    public @NotNull Key key() {
        return key;
    }

    @Override
    public void produceResources(ArbitraryCharacterFactory characterFactory) throws ResourceAlreadyProducedException {
        if (fontProviders != null) {
            throw new ResourceAlreadyProducedException();
        }

        var fontProviderBuilder = FontProvider.bitMap();

        this.character = characterFactory.nextCharacter();

        fontProviderBuilder.characters(String.valueOf(character));
        fontProviderBuilder.file(texture.key());
        fontProviderBuilder.ascent(properties.ascent());
        fontProviderBuilder.height(properties.height());

        this.fontProviders = Collections.singleton(fontProviderBuilder.build());
    }

    @Override
    public @NotNull Collection<@NotNull FontProvider> fontProviders() throws ResourceNotProducedException {
        if (fontProviders == null) {
            throw new ResourceNotProducedException();
        }
        return fontProviders;
    }

    @Override
    public @NotNull Collection<@NotNull Texture> textures() throws ResourceNotProducedException {
        return Collections.singleton(texture);
    }

    @Override
    public int width() {
        if (width == -1) {
            try {
                width = ImageUtil.calculateWidth(ImageIO.read(new ByteArrayInputStream(texture.data().toByteArray()))) + 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return width;
    }

    @Override
    public @NotNull Character character() throws ResourceNotProducedException {
        return character;
    }

    @Override
    public @NotNull Texture texture() {
        return texture;
    }

}
