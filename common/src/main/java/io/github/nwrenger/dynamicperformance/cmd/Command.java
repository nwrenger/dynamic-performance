package io.github.nwrenger.dynamicperformance.cmd;

import java.util.Objects;

import org.jspecify.annotations.NonNull;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import io.github.nwrenger.dynamicperformance.Config;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;

public final class Command {
    private Command() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, Config config) {
        dispatcher.register(createRoot("dp", config));
        dispatcher.register(createRoot("dynamicperformance", config));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createRoot(@NonNull String name, Config config) {
        return Commands.literal(name)
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("about")
                        .executes(context -> AboutCommand.run(context.getSource())))
                .then(Commands.literal("status")
                        .executes(context -> StatusCommand.run(context.getSource(), config)));
    }

    @NonNull
    public static ClickEvent openUrl(String url) {
        return new ClickEvent(ClickEvent.Action.OPEN_URL, Objects.requireNonNull(url));
    }
}
