package ru.brikster.glyphs.glyph.image;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ColoredImageGlyphImpl implements ColoredImageGlyph {
    private final ImageGlyph original;
    private TextColor color;

    @Override
    public @NotNull Component toAdventure() throws ResourceNotProducedException {
        Component component = this.original.toAdventure();
        if (color != null)
            component = component.color(color);

        return component;
    }

    @Override
    public int width() {
        return original.width();
    }

    @Override
    public @Nullable TextColor color() {
        return color;
    }

    @Override
    public void updateColor(@Nullable TextColor color) {
        this.color = color;
    }

    @Override
    public ImageGlyph original() {
        return original;
    }
}
