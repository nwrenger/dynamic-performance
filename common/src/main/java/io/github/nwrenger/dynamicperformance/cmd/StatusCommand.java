package io.github.nwrenger.dynamicperformance.cmd;

import org.jspecify.annotations.NonNull;

import io.github.nwrenger.dynamicperformance.Common;
import io.github.nwrenger.dynamicperformance.Config;
import io.github.nwrenger.dynamicperformance.MobCap;

import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;

public class StatusCommand {

        private StatusCommand() {
        }

        public static int run(CommandSourceStack source, Config config) {
                MinecraftServer server = source.getServer();
                PlayerList playerList = server.getPlayerList();

                float mspt = server.getAverageTickTimeNanos() / 1_000_000.0f;

                int viewDistance = playerList.getViewDistance();
                int simulationDistance = playerList.getSimulationDistance();
                int mobCapPercentage = MobCap.getPercentage();

                State state = getState(mspt, config, playerList);

                source.sendSuccess(
                                () -> message(
                                                state,
                                                mspt,
                                                viewDistance,
                                                simulationDistance,
                                                mobCapPercentage),
                                false);

                return 1;
        }

        @SuppressWarnings("null")
        private static Component message(
                        State state,
                        float mspt,
                        int viewDistance,
                        int simulationDistance,
                        int mobCapPercentage) {
                return Component.literal("\n")
                                .append(Component.literal("Dynamic Performance")
                                                .withStyle(style -> style
                                                                .withBold(true)
                                                                .withColor(ChatFormatting.GOLD)))
                                .append(Component.literal(" by "))
                                .append(Component.literal("nwrenger")
                                                .withStyle(style -> style
                                                                .withItalic(true)
                                                                .withColor(TextColor.fromRgb(0xF223F2))
                                                                .withClickEvent(Command
                                                                                .openUrl(AboutCommand.WEBSITE_URL))))
                                .append(Component.literal(" - Status"))
                                .append(Component.literal("\n\n"))

                                .append(entry("State", Component.literal(state.label())
                                                .withStyle(state.color()))
                                                .append(Component.literal(" (")
                                                                .withStyle(ChatFormatting.DARK_GRAY))
                                                .append(Component.literal("%.2f ms".formatted(mspt))
                                                                .withStyle(getTickTimeColor(mspt)))
                                                .append(Component.literal(")")
                                                                .withStyle(ChatFormatting.DARK_GRAY)))
                                .append(Component.literal("\n"))

                                .append(entry("View Distance", Component.literal(String.valueOf(viewDistance))
                                                .withStyle(ChatFormatting.AQUA)))
                                .append(Component.literal("\n"))

                                .append(entry("Simulation Distance",
                                                Component.literal(String.valueOf(simulationDistance))
                                                                .withStyle(ChatFormatting.AQUA)))
                                .append(Component.literal("\n"))

                                .append(entry("Mob Cap", Component.literal(mobCapPercentage + "%")
                                                .withStyle(ChatFormatting.AQUA)))
                                .append(Component.literal("\n"));
        }

        private static MutableComponent entry(String name, @NonNull Component value) {
                return Component.literal("▸ ")
                                .withStyle(ChatFormatting.DARK_GRAY)
                                .append(Component.literal(name + ": ")
                                                .withStyle(ChatFormatting.GRAY))
                                .append(value);
        }

        private static ChatFormatting getTickTimeColor(float mspt) {
                if (mspt >= 50.0f) {
                        return ChatFormatting.RED;
                }

                if (mspt >= 40.0f) {
                        return ChatFormatting.YELLOW;
                }

                return ChatFormatting.GREEN;
        }

        private static State getState(float mspt, Config config, PlayerList playerList) {
                if (mspt >= config.lag_threshold) {
                        if (Common.canScaleDown(playerList)) {
                                return State.SCALING_DOWN;
                        }

                        return State.LAGGING;
                }

                if (mspt < config.recovery_threshold) {
                        if (Common.canScaleUp(playerList)) {
                                return State.SCALING_UP;
                        }

                        return State.OPTIMAL;
                }

                return State.STABLE;
        }
}