package ru.brikster.glyphs.compile;

import org.jetbrains.annotations.NotNull;

public interface ArbitraryCharacterFactory {

    @NotNull Character nextCharacter() throws IllegalStateException;

}
