package ru.brikster.glyphs.pack;

import java.util.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.GlyphCompiler;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.compile.resource.FileResource;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DefaultGlyphResourcePack implements GlyphResourcePack {

    private final Map<String, ResourceProducer> raw = new HashMap<>();
    private final Map<String, ResourceProducer> compiled = new HashMap<>();

    private final Set<FileResource> resources = new HashSet<>();

    @Override
    public @NotNull Collection<@NotNull FileResource> all() {
        if (!raw.isEmpty()) {
            compileAll();
        }
        return resources;
    }

    @Override
    public void compileAll() {
        var resources = GlyphCompiler.instance().compile(raw.values());
        this.resources.addAll(resources);
        compiled.putAll(raw);
        raw.clear();
    }

    @Override
    public @NotNull <T extends ResourceProducer> GlyphResourcePack with(
            @NotNull ResourceIdentifier<@NotNull T> id, @NotNull T producer) {
        if (raw.containsKey(id.key()) || compiled.containsKey(id.key())) {
            throw new IllegalArgumentException("Producer with " + id.key() + " identifier already registered");
        }
        raw.put(id.key(), producer);
        return this;
    }

    @Override
    public @NotNull GlyphResourcePack with(@NotNull FileResource resource) {
        resources.add(resource);
        return this;
    }

    @Override
    public <T extends ResourceProducer> @NotNull T get(@NotNull ResourceIdentifier<@NotNull T> id)
            throws IllegalArgumentException {
        if (!compiled.containsKey(id.key())) {
            throw new IllegalArgumentException("Producer with " + id.key() + " identifier is not compiled");
        }
        ResourceProducer producer = compiled.get(id.key());
        if (!id.getType().isAssignableFrom(producer.getClass())) {
            throw new IllegalArgumentException("Wrong producer type");
        }
        return (T) producer;
    }
}
