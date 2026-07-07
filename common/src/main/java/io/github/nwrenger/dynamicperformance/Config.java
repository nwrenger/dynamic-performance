package io.github.nwrenger.dynamicperformance;

import com.google.gson.GsonBuilder;
import io.github.nwrenger.dynamicperformance.level.Level;
import io.github.nwrenger.dynamicperformance.level.LevelType;
import io.github.nwrenger.dynamicperformance.platform.Services;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import org.jspecify.annotations.Nullable;

public class Config {

    public static final double TICKS_PER_SECOND = 20.0;

    private static final File CONFIG_FILE = new File(
        Services.PLATFORM.getConfigDir().toFile(),
        "dynamic-performance.json"
    );

    /// The interval in seconds at which the server tick rate is checked and
    /// distances are adjusted accordingly
    public int interval = 15;
    /// The threshold for lag in milliseconds
    public float lag_threshold = 45.0f;
    /// The threshold for recovery in milliseconds
    public float recovery_threshold = 30.0f;
    /// Performance levels that define the view distance, simulation distance, and
    /// mob cap percentage at different performance degradations
    public Level[] levels = {
        new Level(LevelType.SIMULATION_DISTANCE, 16, 8, 1),
        new Level(LevelType.MOB_CAP_PERCENTAGE, 100, 75, 5),
        new Level(LevelType.SIMULATION_DISTANCE, 8, 5, 1),
        new Level(LevelType.MOB_CAP_PERCENTAGE, 75, 30, 15),
        new Level(LevelType.VIEW_DISTANCE, 20, 4, 2),
    };

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
        } catch (IOException e) {
            Constants.LOG.error(
                "[Dynamic Performance] IO error while trying to save config file",
                e
            );
        }
    }

    public static Config load() {
        if (!CONFIG_FILE.exists()) {
            Config newConfig = new Config();
            newConfig.save();
            return newConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath())) {
            return new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .fromJson(reader, Config.class);
        } catch (IOException e) {
            Constants.LOG.error(
                "[Dynamic Performance] Unable to read config file, using default config",
                e
            );
            return new Config();
        }
    }

    public static void validate(@Nullable Config config) {
        if (config == null) {
            throw new IllegalStateException(
                "[Dynamic Performance] Config is null, this should never happen"
            );
        }

        if (config.interval <= 0) {
            throw new IllegalStateException(
                "[Dynamic Performance] `interval` must be greater than 0"
            );
        }

        if (config.lag_threshold <= config.recovery_threshold) {
            throw new IllegalStateException(
                "[Dynamic Performance] `lag_threshold` must be greater than `recovery_threshold`"
            );
        }

        if (config.levels.length == 0) {
            throw new IllegalStateException(
                "[Dynamic Performance] Config must contain at least one level"
            );
        }

        for (Level level : config.levels) {
            if (level.increment <= 0) {
                throw new IllegalStateException(
                    "[Dynamic Performance] `level.increment` must be greater than 0"
                );
            }

            if (level.min > level.max) {
                throw new IllegalStateException(
                    "[Dynamic Performance] `level.min` must not be greater than `level.max`"
                );
            }
        }
    }

    public static double intoTicks(int seconds) {
        return seconds * TICKS_PER_SECOND;
    }
}
