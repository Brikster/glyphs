package ru.brikster.glyphs.compile.resource;

import lombok.RequiredArgsConstructor;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.model.Model;

@RequiredArgsConstructor
public class ModelFileResource implements FileResource {

    private final Model model;

    @Override
    public void write(ResourcePack resourcePack) {
        resourcePack.model(model);
    }
}
