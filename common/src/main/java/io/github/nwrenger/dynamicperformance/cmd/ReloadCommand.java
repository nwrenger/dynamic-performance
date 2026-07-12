package io.github.nwrenger.dynamicperformance.cmd;

import io.github.nwrenger.dynamicperformance.Common;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public class ReloadCommand {

    private ReloadCommand() {}

    public static int run(CommandSourceStack source) {
        // Reload and catch + send errors
        try {
            Common.loadConfig();
        } catch (Exception e) {
            source.sendFailure(
                failure(e.getMessage().replace("[Dynamic Performance] ", ""))
            );
            return 0;
        }

        // No errors, send success message
        source.sendSuccess(ReloadCommand::success, false);
        return 1;
    }

    private static Component success() {
        return Command.header("Reload")
            .append(
                Component.literal("Successful").withStyle(ChatFormatting.GREEN)
            )
            .append("\n");
    }

    private static Component failure(String error) {
        return Command.header("Reload")
            .append(
                Component.literal("Failed")
                    .withStyle(ChatFormatting.RED)
                    .append("\n")
                    .append(
                        Component.literal(error).withStyle(ChatFormatting.GRAY)
                    )
            )
            .append("\n");
    }
}
