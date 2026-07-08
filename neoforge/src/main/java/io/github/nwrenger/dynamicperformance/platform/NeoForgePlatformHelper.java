package io.github.nwrenger.dynamicperformance.platform;

import java.nio.file.Path;
import java.util.function.Consumer;

import io.github.nwrenger.dynamicperformance.Config;
import io.github.nwrenger.dynamicperformance.cmd.Command;
import io.github.nwrenger.dynamicperformance.platform.services.IPlatformHelper;
import net.minecraft.server.MinecraftServer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isPhysicalClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public void registerCommands(Config config) {
        NeoForge.EVENT_BUS
                .addListener((RegisterCommandsEvent event) -> Command.register(event.getDispatcher(), config));
    }

    @Override
    public void registerEndServerTick(Consumer<MinecraftServer> listener) {
        NeoForge.EVENT_BUS.addListener((ServerTickEvent.Post event) -> listener.accept(event.getServer()));
    }

}