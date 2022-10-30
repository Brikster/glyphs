package ru.brikster.glyphs.compile;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.file.FileResource;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;

import java.util.*;
import java.util.stream.Collectors;

public class DefaultGlyphCompiler implements GlyphCompiler {

    @Override
    public @NotNull Collection<@NotNull FileResource> compile(@NotNull Collection<@NotNull ResourceProducer> producers) {
        Set<FileResource> fileResources = new HashSet<>();

        Set<Key> fontKeys = producers
                .stream()
                .map(ResourceProducer::fontKey)
                .collect(Collectors.toUnmodifiableSet());

        fontKeys.forEach(key -> {
            List<FontProvider> fontProviders = new ArrayList<>();
            ArbitraryCharacterFactory characterFactory = new DefaultArbitraryCharacterFactory();
            producers.stream()
                    .filter(fontProviderProducer -> fontProviderProducer.fontKey().equals(key))
                    .toList()
                    .forEach(producer -> {
                        producer.produceResources(characterFactory);
                        // Add font providers for current font key
                        fontProviders.addAll(producer.fontProviders());
                        // Add textures to common set with resources
                        fileResources.addAll(producer.textures());
                    });
            fileResources.add(Font.of(key, fontProviders));
        });

        return fileResources;
    }

}
