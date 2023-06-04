package ru.brikster.glyphs.resources;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.compile.resource.FileResource;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.image.ImageGlyph;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import ru.brikster.glyphs.glyph.image.multicharacter.LanguageGlyphCollection;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.model.*;
import team.unnamed.creative.texture.Texture;

public final class GlyphResources {

    private static final String GLYPHS_RESOURCES_FOLDER = "glyphs-resources";
    public static final Writable SPACE_IMAGE_WRITABLE = resourceFromJar(GLYPHS_RESOURCES_FOLDER + '/' + "space.png");
    public static final Writable BLANK_SLOT_IMAGE_WRITABLE =
            resourceFromJar(GLYPHS_RESOURCES_FOLDER + '/' + "blank_slot.png");
    public static final Writable MINECRAFT_FONT_IMAGE_WRITABLE =
            resourceFromJar(GLYPHS_RESOURCES_FOLDER + '/' + "minecraft_font.png");
    public static final Writable BACKGROUND_WRITABLE =
            resourceFromJar(GLYPHS_RESOURCES_FOLDER + '/' + "fullscreen_background.png");
    private static final Key PAPER_ITEM_KEY = Key.key("item/paper");

    private static final Key MINECRAFT_FONT_KEY = Key.key(Glyph.DEFAULT_NAMESPACE, "minecraft_font");
    private static final Key FULLSCREEN_BACKGROUND_KEY = Key.key(Glyph.DEFAULT_NAMESPACE, "fullscreen_background");

    public static @NotNull Writable resourceFromJar(@NotNull String fileName) {
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

    public static @NotNull LanguageGlyphCollection minecraftFontGlyphCollection(
            @NotNull Key fontKey, @NotNull Key textureKey, @NotNull List<@NotNull TextureProperties> propertiesList) {
        return LanguageGlyphCollection.of(
                fontKey,
                Texture.of(textureKey, MINECRAFT_FONT_IMAGE_WRITABLE),
                propertiesList,
                List.of(
                        "      АБВГДЕЖЗИК",
                        "ЛМНОПРСТУФХЦЧШЩЪ",
                        "ЫЬЭЮЯабвгдежзикл",
                        "мнопрстуфхцчшщъы",
                        "ьэюяйЙёЁ        ",
                        "₽!\"#$%&'()*+,-./",
                        "0123456789: <=>?",
                        "@ABCDEFGHIJKLMNO",
                        "PQRSTUVWXYZ[\\]^_",
                        "`abcdefghijklmno",
                        "pqrstuvwxyz{|}  "));
    }

    public static @NotNull LanguageGlyphCollection minecraftFontGlyphCollection(
            @NotNull List<@NotNull TextureProperties> propertiesList) {
        return minecraftFontGlyphCollection(
                MINECRAFT_FONT_KEY, keyWithPngExtension(MINECRAFT_FONT_KEY), propertiesList);
    }

    public static @NotNull Collection<@NotNull FileResource> blankSlotResources(
            @NotNull Key modelKey, @NotNull Key itemKey, int customModelData) {
        Model blankSlotModel = Model.builder()
                .key(modelKey)
                .parent(Model.ITEM_GENERATED)
                .textures(ModelTextures.builder()
                        .layers(ModelTexture.ofKey(modelKey))
                        .build())
                .build();

        Model paperItemModel = Model.builder()
                .key(itemKey)
                .parent(Model.ITEM_GENERATED)
                .textures(ModelTextures.builder()
                        .layers(ModelTexture.ofKey(itemKey))
                        .build())
                .overrides(ItemOverride.of(modelKey, ItemPredicate.customModelData(customModelData)))
                .build();

        Texture texture = Texture.of(keyWithPngExtension(modelKey), BLANK_SLOT_IMAGE_WRITABLE);
        return Arrays.asList(
                FileResource.fromModel(blankSlotModel),
                FileResource.fromModel(paperItemModel),
                FileResource.fromTexture(texture));
    }

    public static @NotNull Collection<@NotNull FileResource> blankSlotResources() {
        return blankSlotResources(Key.key(Glyph.DEFAULT_NAMESPACE, "blank_slot"), PAPER_ITEM_KEY, 1);
    }

    public static @NotNull ImageGlyph fullscreenBackgroundGlyph(@NotNull Key fontKey, @NotNull Key textureKey) {
        return ImageGlyph.of(fontKey, Texture.of(textureKey, BACKGROUND_WRITABLE), new TextureProperties(2500, 256));
    }

    public static @NotNull ImageGlyph fullscreenBackgroundGlyph() {
        return fullscreenBackgroundGlyph(FULLSCREEN_BACKGROUND_KEY, keyWithPngExtension(FULLSCREEN_BACKGROUND_KEY));
    }

    public static Key keyWithPngExtension(@NotNull Key key) {
        //noinspection PatternValidation
        return Key.key(key.namespace(), key.value().concat(".png"));
    }
}
