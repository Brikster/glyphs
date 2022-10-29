package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import ru.brikster.glyphs.util.ImageUtil;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MulticharacterImageGlyphCollectionImpl implements MulticharacterImageGlyphCollection {

    private final Key key;
    private final Texture texture;
    private final TextureProperties properties;
    private final List<String> charactersMapping;

    private final Map<Character, Character> originToArbitraryCharacterMap = new HashMap<>();

    private Set<FontProvider> fontProviders;

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
        fontProviderBuilder.file(key);
        fontProviderBuilder.ascent(properties.ascent());
        fontProviderBuilder.height(properties.height());

        List<String> mappingLines = new ArrayList<>();

        for (String mappingLine : charactersMapping) {
            StringBuilder builder = new StringBuilder();
            for (char character : mappingLine.toCharArray()) {
                char arbitraryCharacter = characterFactory.nextCharacter();
                originToArbitraryCharacterMap.put(character, arbitraryCharacter);
                builder.append(arbitraryCharacter);
            }
            mappingLines.add(builder.toString());
        }

        fontProviderBuilder.characters(mappingLines);

        this.fontProviders = Collections.singleton(fontProviderBuilder.build());
    }

    @Override
    public @NotNull Collection<@NotNull FontProvider> fontProviders() throws ResourceNotProducedException {
        return fontProviders;
    }

    @Override
    public @NotNull Collection<@NotNull Texture> textures() throws ResourceNotProducedException {
        return Collections.singleton(texture);
    }

    @Override
    public @NotNull PreparedImageGlyph translate(@NotNull Character character) throws IllegalArgumentException {
        if (!originToArbitraryCharacterMap.containsKey(character)) {
            throw new IllegalArgumentException();
        }

        int width = 0;
        for (int lineIndex = 0; lineIndex < charactersMapping.size(); lineIndex++) {
            String line = charactersMapping.get(lineIndex);
            for (int characterIndex = 0; characterIndex < line.toCharArray().length; characterIndex++) {
                if (line.charAt(characterIndex) == character) {
                    BufferedImage image = null;
                    try {
                        image = ImageIO.read(new ByteArrayInputStream(texture.data().toByteArray()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (image == null) {
                        throw new IllegalArgumentException();
                    }

                    int partWidth = image.getWidth() / charactersMapping.get(0).length();
                    int partHeight = image.getHeight() / charactersMapping.size();

                    width = ImageUtil.calculateWidth(
                            image, partWidth * characterIndex, partHeight * lineIndex,
                            (partWidth + 1) * characterIndex, (partHeight + 1) * lineIndex
                    );
                    break;
                }
            }
        }

        return new PreparedImageGlyph(key, originToArbitraryCharacterMap.get(character), width + 1);
    }

}
