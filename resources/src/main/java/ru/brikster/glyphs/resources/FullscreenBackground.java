package ru.brikster.glyphs.resources;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
import org.jetbrains.annotations.NotNull;
import ru.brikster.glyphs.glyph.AppendableGlyph;
import ru.brikster.glyphs.glyph.EmptyGlyph;
import ru.brikster.glyphs.glyph.GlyphComponentBuilder;
import ru.brikster.glyphs.glyph.image.ImageGlyph;
import ru.brikster.glyphs.glyph.space.SpacesGlyphResourceProducer;

import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@UtilityClass
public class FullscreenBackground {

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public @NotNull CompletableFuture<Void> show(@NotNull Audience audience,
                                  @NotNull GlyphComponentBuilder componentBuilder,
                                  @NotNull ImageGlyph backgroundGlyph,
                                  @NotNull Duration fadeIn) {
        audience.showTitle(Title.title(
                constructComponent(componentBuilder, backgroundGlyph).build(),
                Component.empty(),
                Times.times(fadeIn, Duration.ofMinutes(3), Duration.ZERO)
        ));
        return createFuture(fadeIn);
    }

    public @NotNull CompletableFuture<Void> show(@NotNull Audience audience,
                                                 @NotNull GlyphComponentBuilder componentBuilder,
                                                 @NotNull ImageGlyph backgroundGlyph,
                                                 @NotNull Duration fadeIn,
                                                 @NotNull AppendableGlyph @NotNull... content) {
        var component = constructComponent(componentBuilder, backgroundGlyph);
        for (AppendableGlyph glyph : content) {
            component.append(glyph.width() / 2, glyph);
        }

        audience.showTitle(Title.title(
                component.build(),
                Component.empty(),
                Times.times(fadeIn, Duration.ofMinutes(3), Duration.ZERO)
        ));

        return createFuture(fadeIn);
    }

    public record AnimationPart(@NotNull Duration duration, @NotNull AppendableGlyph @NotNull... content) {

        public static @NotNull AnimationPart empty(@NotNull Duration duration) {
            return new AnimationPart(duration, EmptyGlyph.INSTANCE);
        }

    }

    public @NotNull CompletableFuture<Void> showAnimated(@NotNull Audience audience,
                                                         @NotNull Supplier<@NotNull GlyphComponentBuilder> componentBuilderSupplier,
                                                         @NotNull ImageGlyph backgroundGlyph,
                                                         @NotNull List<@NotNull AnimationPart> animationParts) {
        CompletableFuture<Void> voidFuture = null;
        for (AnimationPart part : animationParts) {
            var partVoidFuture = show(audience,
                    componentBuilderSupplier.get(),
                    backgroundGlyph,
                    Duration.ZERO,
                    part.content);

            if (voidFuture == null) {
                voidFuture = partVoidFuture;
            } else {
                voidFuture = voidFuture.thenCompose($ -> partVoidFuture);
            }
        }
        return voidFuture == null
                ? CompletableFuture.completedFuture(null)
                : voidFuture;
    }

    public @NotNull CompletableFuture<Void> showAnimated(@NotNull Audience audience,
                                                         @NotNull SpacesGlyphResourceProducer spaces,
                                                         @NotNull ImageGlyph backgroundGlyph,
                                                         @NotNull List<@NotNull AnimationPart> animationParts) {
        return showAnimated(audience, () -> GlyphComponentBuilder.universal(spaces), backgroundGlyph, animationParts);
    }

    public @NotNull CompletableFuture<Void> hide(@NotNull Audience audience,
                                                 @NotNull GlyphComponentBuilder componentBuilder,
                                                 @NotNull ImageGlyph backgroundGlyph,
                                                 @NotNull Duration fadeOut) {
        audience.showTitle(Title.title(
                constructComponent(componentBuilder, backgroundGlyph).build(),
                Component.empty(),
                Times.times(Duration.ZERO, Duration.ZERO, fadeOut)
        ));
        return createFuture(fadeOut);
    }

    private static @NotNull CompletableFuture<Void> createFuture(@NotNull Duration duration) {
        CompletableFuture<Void> voidFuture = new CompletableFuture<>();
        EXECUTOR_SERVICE.schedule(() -> { voidFuture.complete(null); }, ((int) (duration.toMillis() / 50)) * 50L, TimeUnit.MILLISECONDS);
        return voidFuture;
    }

    private static @NotNull GlyphComponentBuilder constructComponent(@NotNull GlyphComponentBuilder componentBuilder,
                                                                     @NotNull ImageGlyph backgroundGlyph) {
        return componentBuilder.append(-1024, backgroundGlyph);
    }

}
