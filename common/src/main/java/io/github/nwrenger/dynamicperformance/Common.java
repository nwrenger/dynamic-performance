package io.github.nwrenger.dynamicperformance;

import io.github.nwrenger.dynamicperformance.level.Level;
import io.github.nwrenger.dynamicperformance.platform.Services;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.PlayerList;
import org.jspecify.annotations.NonNull;

public class Common {

    public static Config config;

    private static int tickCount = 0;

    public static void init() {
        loadConfig();

        // Command registration for the `/dp` and `/dynamicperformance` commands
        Services.PLATFORM.registerCommands();

        // Register a server tick event to check the tick rate and adjust performance
        // settings accordingly
        Services.PLATFORM.registerEndServerTick(Common::tick);

        Constants.LOG.info("[Dynamic Performance] Started successfully");
    }

    public static ResourceLocation id(@NonNull String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }

    public static void loadConfig() {
        // Load and update state
        Config newConfig = Config.load();

        // Validate the config to ensure that all values are within acceptable ranges
        Config.validate(newConfig);

        // All is validated, so apply
        config = newConfig;
    }

    public static Config getConfig() {
        return Objects.requireNonNull(config);
    }

    public static void tick(MinecraftServer server) {
        // Makes sure we only check the tick rate every `config.interval` seconds
        tickCount++;
        if (tickCount >= Config.intoTicks(config.interval)) {
            tickCount = 0;
            check(server);
        }
    }

    private static void check(MinecraftServer server) {
        float mspt = server.getAverageTickTimeNanos() / 1_000_000.0f;

        if (mspt >= config.lag_threshold) {
            scaleDown(server.getPlayerList());
        } else if (mspt <= config.recovery_threshold) {
            scaleUp(server.getPlayerList());
        }
    }

    private static void scaleDown(PlayerList playerList) {
        Level level = getScaleDownLevel(playerList);

        if (level == null) {
            return;
        }

        int current = getCurrentValue(playerList, level);
        int next = Math.max(level.min, current - level.increment);

        applyValue(playerList, level, next);
    }

    private static void scaleUp(PlayerList playerList) {
        Level level = getScaleUpLevel(playerList);

        if (level == null) {
            return;
        }

        int current = getCurrentValue(playerList, level);
        int next = Math.min(level.max, current + level.increment);

        applyValue(playerList, level, next);
    }

    private static Level getScaleDownLevel(PlayerList playerList) {
        for (Level level : config.levels) {
            int current = getCurrentValue(playerList, level);

            if (current > level.min) {
                return level;
            }
        }

        return null;
    }

    private static Level getScaleUpLevel(PlayerList playerList) {
        for (int i = config.levels.length - 1; i >= 0; i--) {
            Level level = config.levels[i];
            int current = getCurrentValue(playerList, level);

            if (current < level.max) {
                return level;
            }
        }

        return null;
    }

    public static boolean canScaleDown(PlayerList playerList) {
        for (Level level : config.levels) {
            int current = getCurrentValue(playerList, level);

            if (current > level.min) {
                return true;
            }
        }

        return false;
    }

    public static boolean canScaleUp(PlayerList playerList) {
        for (int i = config.levels.length - 1; i >= 0; i--) {
            Level level = config.levels[i];
            int current = getCurrentValue(playerList, level);

            if (current < level.max) {
                return true;
            }
        }

        return false;
    }

    private static int getCurrentValue(PlayerList playerList, Level level) {
        return switch (level.type) {
            case VIEW_DISTANCE -> playerList.getViewDistance();
            case SIMULATION_DISTANCE -> playerList.getSimulationDistance();
            case MOB_CAP_PERCENTAGE -> MobCap.getPercentage();
        };
    }

    private static void applyValue(
        PlayerList playerList,
        Level level,
        int value
    ) {
        switch (level.type) {
            case VIEW_DISTANCE -> Distance.setView(playerList, value);
            case SIMULATION_DISTANCE -> Distance.setSimulation(
                playerList,
                value
            );
            case MOB_CAP_PERCENTAGE -> MobCap.setPercentage(value);
        }
    }
}
