package ru.brikster.glyphs.util.kyori;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.flattener.ComponentFlattener;

import java.util.List;

public class KyoriUtils {
    public static List<TextAndColor> groupByColor(Component component) {
        ColorGroupFlattenerListener flattenerListener = new ColorGroupFlattenerListener();
        ComponentFlattener.basic().flatten(component, flattenerListener);

        return flattenerListener.getResult();
    }
}
