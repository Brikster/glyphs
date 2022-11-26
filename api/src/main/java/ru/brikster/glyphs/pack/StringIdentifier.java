package ru.brikster.glyphs.pack;

import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.ResourceProducer;
import ru.brikster.glyphs.glyph.image.ImageGlyph;

public class StringIdentifier<T extends @NotNull ResourceProducer> implements ResourceIdentifier<T> {

    private final @NotNull String id;

    private final @NotNull Class<T> type;

    protected StringIdentifier(@NotNull String id, @NotNull Class<T> type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public @NotNull Class<T> getType() {
        return type;
    }

    @Override
    public @NotNull String key() {
        return id;
    }

    public static <T extends @NotNull ResourceProducer> @NotNull StringIdentifier<@NotNull T> of(
            @NotNull String id,
            @NotNull Class<T> type) {
        return new StringIdentifier<>(id, type);
    }

    public static @NotNull StringIdentifier<@NotNull ImageGlyph> image(@NotNull String id) {
        return new StringIdentifier<>(id, ImageGlyph.class);
    }

}
