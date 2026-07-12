package io.github.nwrenger.dynamicperformance.cmd;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.net.URI;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import org.jspecify.annotations.NonNull;

public final class Command {

    public static final String WEBSITE_URL = "https://nwrenger.dev";

    private Command() {}

    public static void register(
        CommandDispatcher<CommandSourceStack> dispatcher
    ) {
        dispatcher.register(createRoot("dp"));
        dispatcher.register(createRoot("dynamicperformance"));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createRoot(
        @NonNull String name
    ) {
        return Commands.literal(name)
            .requires(source -> source.hasPermission(2))
            .then(
                Commands.literal("about").executes(context ->
                    AboutCommand.run(context.getSource())
                )
            )
            .then(
                Commands.literal("config").executes(context ->
                    ConfigCommand.run(context.getSource())
                )
            )
            .then(
                Commands.literal("status").executes(context ->
                    StatusCommand.run(context.getSource())
                )
            )
            .then(
                Commands.literal("reload").executes(context ->
                    ReloadCommand.run(context.getSource())
                )
            );
    }

    public static MutableComponent header(String title) {
        MutableComponent h = Component.literal("\n")
            .append(
                Component.literal("Dynamic Performance").withStyle(style ->
                    style.withBold(true).withColor(ChatFormatting.GOLD)
                )
            )
            .append(Component.literal(" by ").withStyle(ChatFormatting.WHITE))
            .append(
                Component.literal("nwrenger").withStyle(style ->
                    style
                        .withItalic(true)
                        .withColor(TextColor.fromRgb(0xF223F2))
                        .withClickEvent(Command.openUrl(Command.WEBSITE_URL))
                )
            );

        if (title != null && !title.isEmpty()) {
            h.append(
                Component.literal(" - %s".formatted(title)).withStyle(
                    ChatFormatting.WHITE
                )
            );
        }

        h.append("\n\n");

        return h;
    }

    public static MutableComponent entry(
        String name,
        @NonNull Component value
    ) {
        return Component.literal("▸ ")
            .withStyle(ChatFormatting.DARK_GRAY)
            .append(
                Component.literal(name + ": ").withStyle(ChatFormatting.GRAY)
            )
            .append(value);
    }

    @NonNull
    public static ClickEvent openUrl(String url) {
        return new ClickEvent.OpenUrl(Objects.requireNonNull(URI.create(url)));
    }
}
