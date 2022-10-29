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

        Set<Key> usedKeys = producers
                .stream()
                .map(ResourceProducer::fontKey)
                .collect(Collectors.toUnmodifiableSet());

        for (Key key : usedKeys) {
            List<FontProvider> fontProviders = new ArrayList<>();

            ArbitraryCharacterFactory characterFactory = new DefaultArbitraryCharacterFactory();

            for (ResourceProducer producer : producers
                    .stream()
                    .filter(fontProviderProducer -> fontProviderProducer.fontKey().equals(key))
                    .toList()) {
                producer.produceResources(characterFactory);
                fontProviders.addAll(producer.fontProviders());

                fileResources.addAll(producer.textures());
            }

            fileResources.add(Font.of(key, fontProviders));
        }

        return fileResources;
    }

}
