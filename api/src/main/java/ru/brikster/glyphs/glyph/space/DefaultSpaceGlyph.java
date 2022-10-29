package ru.brikster.glyphs.glyph.space;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.exception.ResourceNotProducedException;
import team.unnamed.creative.font.Font;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultSpaceGlyph implements AppendableGlyph {

    @Override
    public @NotNull Component toAdventure() throws ResourceNotProducedException {
        return Component.text(" ").font(Font.MINECRAFT_DEFAULT);
    }

    @Override
    public int width() {
        return 4;
    }

}
