package io.github.nwrenger.dynamicperformance.cmd;

import java.util.Objects;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;

import org.jspecify.annotations.NonNull;

public final class AboutCommand {
    public static final String WEBSITE_URL = "https://nwrenger.dev";
    private static final String ISSUES_URL = "https://github.com/nwrenger/dynamic-performance/issues";

    private AboutCommand() {
    }

    public static int run(CommandSourceStack source) {
        source.sendSuccess(AboutCommand::message, false);
        return 1;
    }

    private static Component message() {
        return Component.literal("\n")
            .append(Component.literal("Dynamic Performance")
                .withStyle(style -> style
                    .withBold(true)
                    .withColor(ChatFormatting.GOLD)
                )
            )
            .append(Component.literal(" by "))
            .append(Component.literal("nwrenger")
                .withStyle(style -> style
                    .withItalic(true)
                    .withColor(TextColor.fromRgb(0xF223F2))
                    .withClickEvent(Command.openUrl(WEBSITE_URL))
                )
            )
            .append(Component.literal("\n\n"))

            .append(bullet())
            .append(Component.literal("Check my other projects on ")
                .withStyle(ChatFormatting.GRAY)
            )
            .append(link(WEBSITE_URL, WEBSITE_URL))
            .append(Component.literal("\n"))

            .append(bullet())
            .append(Component.literal("If you have an issue with this mod, report it ")
                .withStyle(ChatFormatting.GRAY)
            )
            .append(link("here", ISSUES_URL))
            .append(Component.literal("\n"))

            .append(bullet())
            .append(Component.literal("To check the current server performance run ")
                .withStyle(ChatFormatting.GRAY)
            )
            .append(command("/dp status"))
            .append(Component.literal("\n"));
    }

    @NonNull
    private static MutableComponent bullet() {
        return Component.literal("▸ ")
            .withStyle(ChatFormatting.DARK_GRAY);
    }

    @NonNull
    private static MutableComponent link(@NonNull String text, String url) {
        return Component.literal(text)
            .withStyle(style -> style
                .withItalic(true)
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(Command.openUrl(url))
            );
    }

    @NonNull
    private static MutableComponent command(@NonNull String command) {
        return Component.literal(command)
            .withStyle(style -> style
                .withItalic(true)
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(runCommand(command))
            );
    }

    @NonNull
    private static ClickEvent runCommand(@NonNull String command) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, Objects.requireNonNull(command));
    }
}