package ru.brikster.glyphs.glyph.space;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ArbitraryCharacterFactory;
import ru.brikster.glyphs.glyph.EmptyGlyph;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.exception.ResourceAlreadyProducedException;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import ru.brikster.glyphs.util.ArrayUtil;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.BitMapFontProvider;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

import java.util.*;

public class DefaultSpacesGlyphResourceProducer extends AbstractSpacesGlyphResourceProducer {

    private final Key textureKey;
    private final Writable writable;

    private Set<Texture> textures;
    private Set<FontProvider> fontProviders;

    public DefaultSpacesGlyphResourceProducer(Key fontKey, Key textureKey, Writable writable) {
        super(fontKey);
        this.textureKey = textureKey;
        this.writable = writable;
    }

    @Override
    public void produceResources(ArbitraryCharacterFactory characterFactory) {
        if (textures != null) {
            throw new ResourceAlreadyProducedException();
        }

        this.mapping = new HashMap<>();

        Set<FontProvider> fontProviders = new HashSet<>();

        for (int length = 1; length <= 2048; length *= 2) {
            fontProviders.add(prepareBuilder(characterFactory, length).build());
            fontProviders.add(prepareBuilder(characterFactory, length * (-1)).build());
        }

        this.textures = Collections.singleton(Texture.of(textureKey, writable));
        this.fontProviders = fontProviders;
    }

    @NotNull
    private BitMapFontProvider.Builder prepareBuilder(ArbitraryCharacterFactory characterFactory, int length) {
        var fontProviderBuilder = FontProvider.bitMap();

        char character = characterFactory.nextCharacter();

        fontProviderBuilder.characters(String.valueOf(character));
        fontProviderBuilder.file(textureKey);

        if (length > 0) {
            fontProviderBuilder.height(length - 1);
            fontProviderBuilder.ascent(0);
        } else {
            fontProviderBuilder.height(length - 2);
            fontProviderBuilder.ascent(-32768);
        }

        mapping.put(length, character);

        return fontProviderBuilder;
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
        if (textures == null) {
            throw new ResourceNotProducedException();
        }
        return textures;
    }

    @Override
    public Glyph translate(int length) throws ResourceNotProducedException {
        if (mapping == null) {
            throw new ResourceNotProducedException();
        }

        if (length == 0) {
            return EmptyGlyph.INSTANCE;
        }

        int sign = length > 0 ? 1 : -1;
        String binaryString = Integer.toBinaryString(Math.abs(length));

        List<Character> characters = new ArrayList<>();

        int currentRankLength = 1;
        for (int index = 0; index < binaryString.length(); index++) {
            char digit = binaryString.charAt(binaryString.length() - index - 1);
            if (digit == '1') {
                int partLength = currentRankLength * sign;
                if (!mapping.containsKey(partLength)) {
                    throw new IllegalArgumentException("Too much length");
                }
                characters.add(mapping.get(partLength));
            }
            currentRankLength *= 2;
        }

        return new SpacesGlyph(fontKey(), ArrayUtil.toCharArray(characters), length);
    }

}
