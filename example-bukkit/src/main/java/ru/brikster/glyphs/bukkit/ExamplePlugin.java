package ru.brikster.glyphs.bukkit;

import com.github.stefvanschie.inventoryframework.adventuresupport.ComponentHolder;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
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
import ru.brikster.glyphs.compile.GlyphCompiler;
import ru.brikster.glyphs.glyph.Glyph;
import ru.brikster.glyphs.glyph.GlyphComponentBuilder;
import ru.brikster.glyphs.glyph.GlyphComponentBuilder.PositionType;
import ru.brikster.glyphs.glyph.image.ImageGlyph;
import ru.brikster.glyphs.glyph.image.TextureProperties;
import ru.brikster.glyphs.glyph.space.mojang.MojangSpacesGlyph;
import ru.brikster.glyphs.resources.GlyphResources;
import team.unnamed.creative.file.FileResource;
import team.unnamed.creative.file.FileTree;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.PackMeta;
import team.unnamed.creative.texture.Texture;

import lombok.SneakyThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipOutputStream;

public final class ExamplePlugin extends JavaPlugin implements Listener {

    private static final GlyphCompiler COMPILER = GlyphCompiler.instance();

    private ChestGui gui;
    private Component chatComponent;

    @SneakyThrows
    private void createResourcepack(Collection<FileResource> resources) {
        File file = new File(getDataFolder(), "pack.zip");
        getDataFolder().mkdirs();
        file.createNewFile();
        try (FileTree tree = FileTree.zip(new ZipOutputStream(new FileOutputStream(file)))) {
            tree.write(Metadata.builder()
                    .add(PackMeta.of(9, "Example resourcepack"))
                    .build());

            resources.forEach(tree::write);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             @NotNull String[] args) {
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

        var spaces = MojangSpacesGlyph.create();

        var guiBackground = ImageGlyph.of(Texture.of(
                        Key.key(Glyph.DEFAULT_NAMESPACE, "gui/gui_background"),
                        GlyphResources.resourceFromJar("gui_background.png")),
                new TextureProperties(256, 19));

        var exampleButton = ImageGlyph.of(Texture.of(
                        Key.key(Glyph.DEFAULT_NAMESPACE, "gui/example_button"),
                        GlyphResources.resourceFromJar("example_button.png")),
                new TextureProperties(22, -56));

        var font = GlyphResources.minecraftFontGlyphCollection(
                List.of(new TextureProperties(12, -6),
                        new TextureProperties(8, -24),
                        new TextureProperties(8, -36)));

        var exampleLogo = ImageGlyph.of(Texture.of(
                        Key.key(Glyph.DEFAULT_NAMESPACE, "chat/example_logo"),
                        GlyphResources.resourceFromJar("example_logo.png")),
                new TextureProperties(50, 6));

        var resources = COMPILER.compile(spaces, guiBackground, exampleButton, font, exampleLogo);
        resources.addAll(GlyphResources.blankSlotResources());
        createResourcepack(resources);

        var titleComponent = GlyphComponentBuilder.gui(spaces)
                .append(guiBackground)
                .append(131, exampleButton)
                .append(16, font.translate(12, -6, "Example text"))
                .append(16, font.translate(8, -24, "Hello "))
                .append(PositionType.RELATIVE, font.translate(8, -24, "world..."))
                .append(PositionType.ABSOLUTE, 16, font.translate(8, -36, "Hello world...", NamedTextColor.LIGHT_PURPLE))
                .build()
                .append(Component.text("Test menu with glyphs", NamedTextColor.DARK_GRAY, TextDecoration.UNDERLINED));

        this.gui = new ChestGui(4, ComponentHolder.of(titleComponent));
        StaticPane pane = new StaticPane(0, 0, 9, 4);

        ItemStack itemStack = new ItemStack(Material.PAPER);
        var meta = itemStack.getItemMeta();
        meta.setCustomModelData(1);
        meta.displayName(Component.text("Example button")
                .color(NamedTextColor.YELLOW)
                .decoration(TextDecoration.ITALIC, false));
        itemStack.setItemMeta(meta);

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

        this.chatComponent = GlyphComponentBuilder.universal(spaces)
                .append(exampleLogo)
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

}
