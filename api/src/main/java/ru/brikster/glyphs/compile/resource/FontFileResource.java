package ru.brikster.glyphs.compile.resource;

import lombok.RequiredArgsConstructor;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.font.Font;

@RequiredArgsConstructor
public final class FontFileResource implements FileResource {

    private final Font font;

    @Override
    public void write(ResourcePack resourcePack) {
        resourcePack.font(font);
    }
}
