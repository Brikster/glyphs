package ru.brikster.glyphs.glyph.space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.EmptyGlyph;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import ru.brikster.glyphs.util.ArrayUtil;

@RequiredArgsConstructor
public abstract class AbstractSpacesGlyphResourceProducer implements SpacesGlyphResourceProducer {

    private final Key fontKey;

    protected Map<Integer, Character> mapping;

    @Override
    public @NotNull Key fontKey() {
        return fontKey;
    }

    @Override
    public boolean produced() {
        return mapping != null;
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
