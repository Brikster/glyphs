package ru.brikster.glyphs.bukkit;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import java.io.File;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.GlyphComponentBuilder;
import ru.brikster.glyphs.glyph.GlyphComponentBuilder.PositionType;
import ru.brikster.glyphs.glyph.image.ImageGlyph;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import ru.brikster.glyphs.pack.GlyphResourcePack;
import ru.brikster.glyphs.pack.ImageResourceIdentifier;
import ru.brikster.glyphs.pack.ResourceIdentifier;
import ru.brikster.glyphs.resources.GlyphResources;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.metadata.pack.PackMeta;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;
import team.unnamed.creative.texture.Texture;

public final class ExamplePlugin extends JavaPlugin implements Listener {

    private ChestGui gui;
    private Component chatComponent;

    @SneakyThrows
    private void createResourcepack(GlyphResourcePack glyphResourcePack) {
        File file = new File(getDataFolder(), "pack.zip");
        getDataFolder().mkdirs();
        file.createNewFile();

        ResourcePack resourcePack = ResourcePack.create();

        resourcePack.packMeta(PackMeta.of(9, "Example resourcepack"));
        glyphResourcePack.writeAll(resourcePack);

        MinecraftResourcePackWriter.minecraft()
                .writeToZipFile(getDataFolder().toPath().resolve("pack.zip"), resourcePack);
    }

    @Override
    public boolean onCommand(
            @NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            gui.show((HumanEntity) sender);
        } else {
            sender.sendMessage("You're not a player");
        }
        return true;
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        GlyphResourcePack pack = GlyphResourcePack.create()
                .with(
                        ImageId.GUI_BACKGROUND,
                        ImageGlyph.of(
                                Key.key("glyphs", "example_gui"),
                                Texture.of(
                                        Key.key(Glyph.DEFAULT_NAMESPACE, "gui/gui_background"),
                                        GlyphResources.resourceFromJar("gui_background.png")),
                                new TextureProperties(256, 19)))
                .with(
                        ImageId.EXAMPLE_BUTTON,
                        ImageGlyph.of(
                                Key.key("glyphs", "example_gui"),
                                Texture.of(
                                        Key.key(Glyph.DEFAULT_NAMESPACE, "gui/example_button"),
                                        GlyphResources.resourceFromJar("example_button.png")),
                                new TextureProperties(22, -56)))
                .with(
                        ImageId.EXAMPLE_LOGO,
                        ImageGlyph.of(
                                Key.key("glyphs", "example_chat_message"),
                                Texture.of(
                                        Key.key(Glyph.DEFAULT_NAMESPACE, "chat/example_logo"),
                                        GlyphResources.resourceFromJar("example_logo.png")),
                                new TextureProperties(50, 6)))
                .with(
                        ResourceIdentifier.MINECRAFT_FONT,
                        GlyphResources.minecraftFontGlyphCollection(List.of(
                                new TextureProperties(12, -6),
                                new TextureProperties(8, -24),
                                new TextureProperties(8, -36))))
                .withMojangSpaces()
                .with(GlyphResources.blankSlotResources());

        createResourcepack(pack);

        var font = pack.get(ResourceIdentifier.MINECRAFT_FONT);
        var titleComponent = GlyphComponentBuilder.gui(pack.spaces())
                .append(pack.get(ImageId.GUI_BACKGROUND))
                .append(131, pack.get(ImageId.EXAMPLE_BUTTON))
                .append(16, font.translate(12, -6, "Example text"))
                .append(16, font.translate(8, -24, "Hello "))
                .append(PositionType.RELATIVE, font.translate(8, -24, "world..."))
                .append(
                        PositionType.ABSOLUTE,
                        16,
                        font.translate(8, -36, "Hello world...", NamedTextColor.LIGHT_PURPLE))
                .build()
                .append(Component.text("Test menu with glyphs", NamedTextColor.DARK_GRAY, TextDecoration.UNDERLINED));

        this.gui = new ChestGui(4, ComponentHolder.of(titleComponent));
        StaticPane pane = new StaticPane(0, 0, 9, 4);

        ItemStack itemStack = new ItemStack(Material.PAPER);
        itemStack.editMeta(meta -> {
            meta.setCustomModelData(1);
            meta.displayName(Component.text("Example button")
                    .color(NamedTextColor.YELLOW)
                    .decoration(TextDecoration.ITALIC, false));
        });

        GuiItem item = new GuiItem(itemStack);
        item.setAction(event -> {
            event.getClickedInventory().close();
            event.getWhoClicked().playSound(Sound.sound(org.bukkit.Sound.UI_BUTTON_CLICK, Source.MASTER, 1, 1));
        });

        pane.addItem(item, 7, 3);

        gui.addPane(pane);

        gui.setOnTopClick(event -> event.setCancelled(true));
        gui.setOnBottomClick(event -> event.setCancelled(true));
        gui.setOnGlobalClick(event -> event.setCancelled(true));
        gui.setOnOutsideClick(event -> event.setCancelled(true));

        Component logoIndentComponent = Component.text(" ".repeat(14));

        this.chatComponent = GlyphComponentBuilder.universal(pack.spaces())
                .append(pack.get(ImageId.EXAMPLE_LOGO))
                .build()
                .append(Component.newline().append(logoIndentComponent))
                .append(Component.newline()
                        .append(logoIndentComponent)
                        .append(Component.text("Glyphs", TextColor.fromHexString("#d84aff"), TextDecoration.BOLD)))
                .append(Component.newline()
                        .append(logoIndentComponent)
                        .append(Component.text("<--- ", NamedTextColor.GRAY))
                        .append(Component.text("Here you can see Minecraft® logo", NamedTextColor.YELLOW)))
                .append(Component.newline().append(logoIndentComponent))
                .append(Component.newline().append(logoIndentComponent));

        Objects.requireNonNull(getCommand("glyphs")).setExecutor(this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(chatComponent);
    }

    @RequiredArgsConstructor
    public enum ImageId implements ImageResourceIdentifier {
        // CHAT
        EXAMPLE_BUTTON("example_button"),
        EXAMPLE_LOGO("example_logo"),
        // GUI
        GUI_BACKGROUND("gui_background");

        private final @NotNull String key;

        @Override
        public @NotNull String key() {
            return key;
        }
    }
}
