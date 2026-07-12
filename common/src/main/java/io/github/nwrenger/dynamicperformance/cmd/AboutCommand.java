package io.github.nwrenger.dynamicperformance.cmd;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.NonNull;

public final class AboutCommand {

    private static final String ISSUES_URL =
        "https://github.com/nwrenger/dynamic-performance/issues";

    private AboutCommand() {}

    public static int run(CommandSourceStack source) {
        source.sendSuccess(AboutCommand::message, false);
        return 1;
    }

    private static Component message() {
        return Command.header(null)
            .append(
                bullet(
                    Component.literal("Check my other projects on ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(link(Command.WEBSITE_URL, Command.WEBSITE_URL))
                )
            )

            .append(
                bullet(
                    Component.literal(
                        "If you have an issue with this mod, report it "
                    )
                        .withStyle(ChatFormatting.GRAY)
                        .append(link("here", ISSUES_URL))
                )
            )

            .append(
                bullet(
                    Component.literal(
                        "To check the current server performance run "
                    )
                        .withStyle(ChatFormatting.GRAY)
                        .append(command("/dp status"))
                )
            )

            .append(
                bullet(
                    Component.literal("Review the active config with ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(command("/dp config"))
                )
            )

            .append(
                bullet(
                    Component.literal("Reload the config changes with ")
                        .withStyle(ChatFormatting.GRAY)
                        .append(command("/dp reload"))
                )
            );
    }

    @NonNull
    private static MutableComponent bullet(Component component) {
        return Component.literal("▸ ")
            .withStyle(ChatFormatting.DARK_GRAY)
            .append(component)
            .append("\n");
    }

    @NonNull
    private static MutableComponent link(@NonNull String text, String url) {
        return Component.literal(text).withStyle(style ->
            style
                .withItalic(true)
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(Command.openUrl(url))
        );
    }

    @NonNull
    private static MutableComponent command(@NonNull String command) {
        return Component.literal(command).withStyle(style ->
            style
                .withItalic(true)
                .withColor(ChatFormatting.AQUA)
                .withClickEvent(runCommand(command))
        );
    }

    @NonNull
    private static ClickEvent runCommand(@NonNull String command) {
        return new ClickEvent.RunCommand(command);
    }
}
