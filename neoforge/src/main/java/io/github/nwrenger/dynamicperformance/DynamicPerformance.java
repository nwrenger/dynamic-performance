package io.github.nwrenger.dynamicperformance;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class DynamicPerformance {

    public DynamicPerformance(IEventBus eventBus) {
        Common.init();
    }
}
