package ru.brikster.glyphs.util;

import java.util.List;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ArrayUtil {

    public char[] toCharArray(@NotNull List<Character> list) {
        char[] arr = new char[list.size()];
        for (int i = 0; i < list.size(); i++) {
            arr[i] = list.get(i);
        }
        return arr;
    }
}
