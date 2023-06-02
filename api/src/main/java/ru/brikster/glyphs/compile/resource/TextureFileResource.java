package ru.brikster.glyphs.compile.resource;

import lombok.RequiredArgsConstructor;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.texture.Texture;

@RequiredArgsConstructor
public final class TextureFileResource implements FileResource {

    private final Texture texture;

    @Override
    public void write(ResourcePack resourcePack) {
        resourcePack.texture(texture);
    }
}
