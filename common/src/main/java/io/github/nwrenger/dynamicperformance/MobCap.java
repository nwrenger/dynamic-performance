package io.github.nwrenger.dynamicperformance;

import io.github.nwrenger.dynamicperformance.mixin.MobCategoryAccessor;
import net.minecraft.world.entity.MobCategory;

public final class MobCap {

        private MobCap() {
        }

        private static int DEFAULT_MONSTER_CAP = 70;
        private static int DEFAULT_CREATURE_CAP = 10;
        private static int DEFAULT_AMBIENT_CAP = 15;
        private static int DEFAULT_AXOLOTLS_CAP = 5;
        private static int DEFAULT_UNDERGROUND_WATER_CREATURE_CAP = 5;
        private static int DEFAULT_WATER_CREATURE_CAP = 5;
        private static int DEFAULT_WATER_AMBIENT_CAP = 20;

        private static int appliedPercentage = percentageFromCategory();

        public static void setPercentage(int percentage) {
                appliedPercentage = percentage;

                if (percentage == 100) {
                        applyDefaults();
                        return;
                }

                setMobCap(
                                MobCategory.MONSTER,
                                calculateMobCap(DEFAULT_MONSTER_CAP, percentage));
                setMobCap(
                                MobCategory.CREATURE,
                                calculateMobCap(DEFAULT_CREATURE_CAP, percentage));
                setMobCap(
                                MobCategory.AMBIENT,
                                calculateMobCap(DEFAULT_AMBIENT_CAP, percentage));
                setMobCap(
                                MobCategory.AXOLOTLS,
                                calculateMobCap(DEFAULT_AXOLOTLS_CAP, percentage));
                setMobCap(
                                MobCategory.UNDERGROUND_WATER_CREATURE,
                                calculateMobCap(DEFAULT_UNDERGROUND_WATER_CREATURE_CAP, percentage));
                setMobCap(
                                MobCategory.WATER_CREATURE,
                                calculateMobCap(DEFAULT_WATER_CREATURE_CAP, percentage));
                setMobCap(
                                MobCategory.WATER_AMBIENT,
                                calculateMobCap(DEFAULT_WATER_AMBIENT_CAP, percentage));

                Constants.LOG.debug(
                                "[Dynamic Performance] Updated mob cap to {}%",
                                percentage);
        }

        public static int getPercentage() {
                return appliedPercentage;
        }

        private static int percentageFromCategory() {
                int monsterCap = ((MobCategoryAccessor) (Object) MobCategory.MONSTER).dynamicperformance$getMax();
                return Math.round(((float) monsterCap / DEFAULT_MONSTER_CAP) * 100);
        }

        private static int calculateMobCap(int defaultCap, int percentage) {
                return Math.max(1, Math.round((defaultCap * percentage) / 100.0f));
        }

        private static void setMobCap(MobCategory category, int cap) {
                ((MobCategoryAccessor) (Object) category).dynamicperformance$setMax(
                                cap);
        }

        private static void applyDefaults() {
                setMobCap(MobCategory.MONSTER, DEFAULT_MONSTER_CAP);
                setMobCap(MobCategory.CREATURE, DEFAULT_CREATURE_CAP);
                setMobCap(MobCategory.AMBIENT, DEFAULT_AMBIENT_CAP);
                setMobCap(MobCategory.AXOLOTLS, DEFAULT_AXOLOTLS_CAP);
                setMobCap(
                                MobCategory.UNDERGROUND_WATER_CREATURE,
                                DEFAULT_UNDERGROUND_WATER_CREATURE_CAP);
                setMobCap(MobCategory.WATER_CREATURE, DEFAULT_WATER_CREATURE_CAP);
                setMobCap(MobCategory.WATER_AMBIENT, DEFAULT_WATER_AMBIENT_CAP);
        }
}
