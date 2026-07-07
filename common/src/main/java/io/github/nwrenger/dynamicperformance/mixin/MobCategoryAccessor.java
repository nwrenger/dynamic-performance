package io.github.nwrenger.dynamicperformance.mixin;

import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobCategory.class)
public interface MobCategoryAccessor {
    @Mutable
    @Accessor("max")
    void dynamicperformance$setMax(int max);

    @Accessor("max")
    int dynamicperformance$getMax();
}
