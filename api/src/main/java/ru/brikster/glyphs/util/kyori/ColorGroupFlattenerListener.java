package ru.brikster.glyphs.util.kyori;

import lombok.Getter;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColorGroupFlattenerListener implements FlattenerListener {
    private final ArrayDeque<TextColor> colors = new ArrayDeque<>();
    @Getter
    private final List<TextAndColor> result = new ArrayList<>();

    @Override
    public void component(@NotNull String text) {
        result.add(new TextAndColor(
                text,
                current()
        ));
    }

    @Override
    public void pushStyle(@NotNull Style style) {
        TextColor color = style.color();
        if (color != null)
            colors.add(color);
    }

    @Override
    public void popStyle(@NotNull Style style) {
        TextColor color = style.color();
        if (color != null)
            colors.removeFirst();
    }

    private TextColor current() {
        TextColor color = colors.peek();

        return Objects.requireNonNullElse(color, NamedTextColor.WHITE);

    }
}
