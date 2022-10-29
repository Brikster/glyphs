package ru.brikster.glyphs.resources;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import ru.brikster.glyphs.glyph.image.multicharacter.LanguageGlyphCollection;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.file.FileResource;
import team.unnamed.creative.model.ItemOverride;
import team.unnamed.creative.model.ItemPredicate;
import team.unnamed.creative.model.Model;
import team.unnamed.creative.model.ModelTexture;
import team.unnamed.creative.texture.Texture;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class GlyphResources {

    private static final String GLYPHS_RESOURCES_FOLDER = "glyphs-resources";
    public static final Writable SPACE_IMAGE_WRITABLE = resourceFromJar(GLYPHS_RESOURCES_FOLDER + '/' + "space.png");
    public static final Writable BLANK_SLOT_IMAGE_WRITABLE = resourceFromJar(GLYPHS_RESOURCES_FOLDER + '/' + "blank_slot.png");
    public static final Writable MINECRAFT_FONT_IMAGE_WRITABLE = resourceFromJar(GLYPHS_RESOURCES_FOLDER + '/' + "minecraft_font.png");
    private static final Key PAPER_ITEM_KEY = Key.key("item/paper");

    public static Writable resourceFromJar(String fileName) {
        return Writable.inputStream(() -> {
            try {
                URL url = GlyphResources.class.getClassLoader().getResource(fileName);

                if (url == null) {
                    return null;
                }

                URLConnection connection = url.openConnection();
                connection.setUseCaches(false);
                return connection.getInputStream();
            } catch (IOException ex) {
                return null;
            }
        });
    }

    public static LanguageGlyphCollection minecraftFontGlyphCollection(@NotNull Key fontKey,
                                                                       @NotNull Key textureKey,
                                                                       @NotNull List<@NotNull TextureProperties> propertiesList) {
        return LanguageGlyphCollection.of(fontKey,
                Texture.of(textureKey, MINECRAFT_FONT_IMAGE_WRITABLE),
                propertiesList,
                List.of("      АБВГДЕЖЗИК",
                        "ЛМНОПРСТУФХЦЧШЩЪ",
                        "ЫЬЭЮЯабвгдежзикл",
                        "мнопрстуфхцчшщъы",
                        "ьэюяйЙёЁ        ",
                        "₽!\"#$%&'()*+,-./",
                        "0123456789: <=>?",
                        "@ABCDEFGHIJKLMNO",
                        "PQRSTUVWXYZ[\\]^_",
                        "`abcdefghijklmno",
                        "pqrstuvwxyz{|}  ")
        );
    }

    public static LanguageGlyphCollection minecraftFontGlyphCollection(@NotNull List<@NotNull TextureProperties> propertiesList) {
        return LanguageGlyphCollection.of(Glyph.DEFAULT_FONT_KEY,
                Texture.of(Key.key(Glyph.DEFAULT_NAMESPACE, "minecraft_font"), MINECRAFT_FONT_IMAGE_WRITABLE),
                propertiesList,
                List.of("      АБВГДЕЖЗИК",
                        "ЛМНОПРСТУФХЦЧШЩЪ",
                        "ЫЬЭЮЯабвгдежзикл",
                        "мнопрстуфхцчшщъы",
                        "ьэюяйЙёЁ        ",
                        "₽!\"#$%&'()*+,-./",
                        "0123456789: <=>?",
                        "@ABCDEFGHIJKLMNO",
                        "PQRSTUVWXYZ[\\]^_",
                        "`abcdefghijklmno",
                        "pqrstuvwxyz{|}  ")
        );
    }

    public static Collection<FileResource> blankSlotResources(@NotNull Key modelKey, @NotNull Key itemKey) {
        Model blankSlotModel = Model.builder()
                .key(modelKey)
                .parent(Model.ITEM_GENERATED)
                .textures(ModelTexture.builder()
                        .layers(modelKey)
                        .build())
                .build();

        Model paperItemModel = Model.builder()
                .key(itemKey)
                .textures(ModelTexture.builder()
                        .layers(Collections.singletonList(itemKey))
                        .build())
                .overrides(ItemOverride.of(modelKey, ItemPredicate.customModelData(2)))
                .build();

        Texture texture = Texture.of(modelKey, BLANK_SLOT_IMAGE_WRITABLE);
        return Arrays.asList(blankSlotModel, paperItemModel, texture);
    }

    public static Collection<FileResource> blankSlotResources() {
        return blankSlotResources(Key.key(Glyph.DEFAULT_NAMESPACE, "blank_slot"), PAPER_ITEM_KEY);
    }

}
