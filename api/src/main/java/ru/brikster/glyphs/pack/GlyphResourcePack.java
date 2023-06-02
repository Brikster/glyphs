package ru.brikster.glyphs.pack;

import java.util.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.compile.resource.FileResource;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;
import ru.brikster.glyphs.glyph.space.mojang.MojangSpacesGlyph;
import team.unnamed.creative.ResourcePack;

public interface GlyphResourcePack {

    @NotNull Collection<@NotNull FileResource> all();

    void compileAll();

    @Contract("_, _ -> this")
    <T extends ResourceProducer> @NotNull GlyphResourcePack with(
            @NotNull ResourceIdentifier<@NotNull T> id, @NotNull T producer);

    @Contract("_ -> this")
    @NotNull GlyphResourcePack with(@NotNull FileResource resource);

    @Contract("_ -> this")
    default @NotNull GlyphResourcePack with(@NotNull FileResource... resources) {
        for (FileResource resource : resources) {
            with(resource);
        }
        return this;
    }

    @Contract("_ -> this")
    default @NotNull GlyphResourcePack with(@NotNull Collection<@NotNull FileResource> resources) {
        resources.forEach(this::with);
        return this;
    }

    @Contract("-> this")
    default @NotNull GlyphResourcePack withMojangSpaces() {
        with(ResourceIdentifier.SPACES, MojangSpacesGlyph.create());
        return this;
    }

    <T extends ResourceProducer> @NotNull T get(@NotNull ResourceIdentifier<@NotNull T> id)
            throws IllegalArgumentException;

    default @NotNull SpacesGlyphResourceProducer spaces() {
        return get(ResourceIdentifier.SPACES);
    }

    default void writeAll(@NotNull ResourcePack resourcePack) {
        all().forEach(resource -> resource.write(resourcePack));
    }

    static @NotNull GlyphResourcePack create() {
        return new DefaultGlyphResourcePack();
    }
}
