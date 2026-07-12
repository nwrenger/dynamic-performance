package io.github.nwrenger.dynamicperformance.cmd;

import io.github.nwrenger.dynamicperformance.Common;
import io.github.nwrenger.dynamicperformance.Config;
import io.github.nwrenger.dynamicperformance.level.Level;
import io.github.nwrenger.dynamicperformance.level.LevelType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ConfigCommand {

    private ConfigCommand() {}

    public static int run(CommandSourceStack source) {
        Config config = Common.getConfig();
        source.sendSuccess(() -> message(config), false);
        return 1;
    }

    private static Component message(Config config) {
        MutableComponent msg = Command.header("Config")
            .append(
                Command.entry(
                    "Interval",
                    Component.literal(
                        "%ds".formatted(config.interval)
                    ).withStyle(ChatFormatting.AQUA)
                )
            )
            .append("\n")

            .append(
                Command.entry(
                    "Thresholds",
                    Component.literal("recovery ")
                        .withStyle(ChatFormatting.GREEN)
                        .append(
                            Component.literal("≤ ").withStyle(
                                ChatFormatting.DARK_AQUA
                            )
                        )
                        .append(
                            Component.literal(
                                "%.2f ms".formatted(config.recovery_threshold)
                            ).withStyle(ChatFormatting.AQUA)
                        )
                        .append(
                            Component.literal(" · ").withStyle(
                                ChatFormatting.DARK_GRAY
                            )
                        )
                        .append(
                            Component.literal("lag ").withStyle(
                                ChatFormatting.RED
                            )
                        )
                        .append(
                            Component.literal("≥ ").withStyle(
                                ChatFormatting.DARK_AQUA
                            )
                        )
                        .append(
                            Component.literal(
                                "%.2f ms".formatted(config.lag_threshold)
                            ).withStyle(ChatFormatting.AQUA)
                        )
                )
            )
            .append("\n")

            .append(Command.entry("Levels", Component.empty()))
            .append("\n");

        int levelIndex = 1;
        for (Level level : config.levels) {
            msg = msg
                .append(
                    Component.literal("  ")
                        .append(
                            Component.literal(
                                "%d. ".formatted(levelIndex)
                            ).withStyle(ChatFormatting.DARK_GRAY)
                        )
                        .append(levelType(level.type))
                        .append(" ")
                        .append(
                            Component.literal(
                                String.valueOf(level.max)
                            ).withStyle(ChatFormatting.AQUA)
                        )
                        .append(
                            Component.literal(" → ").withStyle(
                                ChatFormatting.DARK_GRAY
                            )
                        )
                        .append(
                            Component.literal(
                                String.valueOf(level.min)
                            ).withStyle(ChatFormatting.AQUA)
                        )
                        .append(
                            Component.literal(levelUnit(level.type)).withStyle(
                                ChatFormatting.AQUA
                            )
                        )
                        .append(
                            Component.literal(" ± ").withStyle(
                                ChatFormatting.DARK_AQUA
                            )
                        )
                        .append(
                            Component.literal(
                                String.valueOf(level.increment)
                            ).withStyle(ChatFormatting.AQUA)
                        )
                )
                .append("\n");

            levelIndex++;
        }

        return msg;
    }

    private static MutableComponent levelType(LevelType type) {
        return Component.literal(
            switch (type) {
                case VIEW_DISTANCE -> "View Distance";
                case SIMULATION_DISTANCE -> "Simulation Distance";
                case MOB_CAP_PERCENTAGE -> "Mob Cap";
            }
        )
            .withStyle(ChatFormatting.GRAY)
            .append(Component.literal(":").withStyle(ChatFormatting.GRAY));
    }

    private static String levelUnit(LevelType type) {
        return switch (type) {
            case MOB_CAP_PERCENTAGE -> "%";
            default -> "";
        };
    }
}
