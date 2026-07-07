package io.github.nwrenger.dynamicperformance.level;

public class Level {

    public LevelType type;
    public Integer max;
    public Integer min;
    public Integer increment;

    public Level(LevelType type, Integer max, Integer min, Integer increment) {
        this.type = type;
        this.max = max;
        this.min = min;
        this.increment = increment;
    }
}
