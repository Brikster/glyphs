package ru.brikster.glyphs.util.kyori;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.flattener.FlattenerListener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.util.kyori.KyoriUtil.ColoredPartsFlattenerListener.ColoredComponentTextPart;

import lombok.experimental.UtilityClass;

import java.util.*;

@UtilityClass
public class KyoriUtil {

    public @NotNull List<? extends @NotNull ColoredComponentTextPart> toColoredParts(@NotNull Component component) {
        ColoredPartsFlattenerListener flattenerListener = new ColoredPartsFlattenerListener();
        ComponentFlattener.basic().flatten(component, flattenerListener);
        return flattenerListener.result();
    }

    public static class ColoredPartsFlattenerListener implements FlattenerListener {

        private final Deque<TextColor> colors = new LinkedList<>();
        private final List<ColoredComponentTextPart> result = new ArrayList<>();

        @Override
        public void pushStyle(@NotNull Style style) {
            TextColor color = style.color();
            if (color != null) {
                colors.add(color);
            }
        }

        @Override
        public void component(@NotNull String text) {
            result.add(new ColoredComponentTextPart(text, current()));
        }

        @Override
        public void popStyle(@NotNull Style style) {
            TextColor color = style.color();
            if (color != null) {
                colors.removeLast();
            }
        }

        private TextColor current() {
            TextColor color = colors.peekLast();
            return color != null ? color : NamedTextColor.WHITE;
        }

        public @NotNull List<? extends @NotNull ColoredComponentTextPart> result() {
            return result;
        }

        public record ColoredComponentTextPart(String text, TextColor color) {}

    }

}
