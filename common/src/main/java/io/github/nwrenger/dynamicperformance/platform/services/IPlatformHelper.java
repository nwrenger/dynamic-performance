package io.github.nwrenger.dynamicperformance.platform.services;

import io.github.nwrenger.dynamicperformance.Config;
import java.nio.file.Path;
import java.util.function.Consumer;
import net.minecraft.server.MinecraftServer;

public interface IPlatformHelper {
    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Gets the path to the configuration directory.
     *
     * @return The path to the configuration directory.
     */
    Path getConfigDir();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Checks if the game is running on a physical client.
     *
     * @return True if running on a physical client, false otherwise.
     */
    boolean isPhysicalClient();

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Registers commands for the mod.
     *
     * @param config The configuration object to use for command registration.
     */
    void registerCommands(Config config);

    /**
     * Registers a listener for the end of the server tick event.
     *
     * @param listener The listener to register.
     */
    void registerEndServerTick(Consumer<MinecraftServer> listener);
}
