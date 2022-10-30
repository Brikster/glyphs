package ru.brikster.glyphs.glyph.image.multicharacter;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brikster.glyphs.glyph.AppendableGlyph;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class PreparedImageGlyph implements AppendableGlyph, ColorableGlyph {

    private final Key key;
    private final Character character;
    private final int width;
    private @Nullable TextColor color;

    @Override
    public @NotNull Component toAdventure() {
        return Component.text(character)
                .font(key)
                .color(color == null ? NamedTextColor.BLACK : color);
    }

    @Override
    public int width() {
        return width;
    }

    @Override
    public @Nullable TextColor color() {
        return color;
    }

    @Override
    public void updateColor(@Nullable TextColor color) {
        this.color = color;
    }

}
