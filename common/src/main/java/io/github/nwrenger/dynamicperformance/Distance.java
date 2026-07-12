package io.github.nwrenger.dynamicperformance;

import io.github.nwrenger.dynamicperformance.platform.Services;
import net.minecraft.server.players.PlayerList;

public class Distance {

    public static void setView(PlayerList playerList, int value) {
        playerList.setViewDistance(value);

        // Update the client-side view distance if this is a physical client
        if (Services.PLATFORM.isPhysicalClient()) {
            net.minecraft.client.Minecraft.getInstance()
                .options.renderDistance()
                .set(value);
        }

        Constants.LOG.debug(
            "[Dynamic Performance] Updated view distance to {}",
            value
        );
    }

    public static void setSimulation(PlayerList playerList, int value) {
        playerList.setSimulationDistance(value);

        // Update the client-side simulation distance if this is a physical client
        if (Services.PLATFORM.isPhysicalClient()) {
            net.minecraft.client.Minecraft.getInstance()
                .options.simulationDistance()
                .set(value);
        }

        Constants.LOG.debug(
            "[Dynamic Performance] Updated simulation distance to {}",
            value
        );
    }
}
