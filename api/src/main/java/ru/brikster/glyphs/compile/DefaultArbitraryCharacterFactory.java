package ru.brikster.glyphs.compile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class DefaultArbitraryCharacterFactory implements ArbitraryCharacterFactory {

    private static final Set<Character> reservedCharacters = new HashSet<>();

    static {
        for (char c = 'a'; c <= 'z'; c++) reservedCharacters.add(c);
        for (char c = 'A'; c <= 'Z'; c++) reservedCharacters.add(c);
        for (char c = 'а'; c <= 'я'; c++) reservedCharacters.add(c);
        for (char c = 'А'; c <= 'Я'; c++) reservedCharacters.add(c);
        for (char c = '0'; c <= '9'; c++) reservedCharacters.add(c);

        reservedCharacters.addAll(Arrays.asList(
                '!', '?', ':', '$', ';', '#', '@', '%', '^', '&', '*', '(', ')', '_', '-', '+', '/', '\\', '№', '"',
                '\'', '{', '}', '[', ']', '~', '`', '<', '>', ',', '.', '|', '\n', '\r', '\b', '\f', '\t', ' ', 'ё',
                'Ё', '='));
    }

    @SuppressWarnings("UnnecessaryUnicodeEscape")
    private char currentChar = '\uA201';

    @Override
    public @NotNull Character nextCharacter() throws IllegalStateException {
        if (currentChar == Character.MAX_VALUE) {
            throw new IllegalStateException("Characters range exceeded");
        }

        currentChar++;
        while (!isCharacterAllowed(currentChar)) {
            currentChar++;
        }
        return currentChar;
    }

    private boolean isCharacterAllowed(char c) {
        return !reservedCharacters.contains(c);
    }
}
