package ru.brikster.glyphs.compile.resource;

import java.util.Collection;
import java.util.stream.Collectors;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.texture.Texture;

public interface FileResource {

    void write(ResourcePack resourcePack);

    static FileResource fromTexture(Texture texture) {
        return new TextureFileResource(texture);
    }

    static Collection<FileResource> fromTextures(Collection<Texture> textures) {
        return textures.stream().map(FileResource::fromTexture).collect(Collectors.toList());
    }

    static FileResource fromFont(Font font) {
        return new FontFileResource(font);
    }

    static FileResource fromModel(Model model) {
        return new ModelFileResource(model);
    }
}
