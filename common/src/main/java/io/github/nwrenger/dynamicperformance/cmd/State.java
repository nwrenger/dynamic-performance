package io.github.nwrenger.dynamicperformance.cmd;

import net.minecraft.ChatFormatting;

public enum State {
    SCALING_DOWN("Scaling Down", ChatFormatting.RED),
    LAGGING("Lagging", ChatFormatting.DARK_RED),
    SCALING_UP("Scaling Up", ChatFormatting.GREEN),
    OPTIMAL("Optimal", ChatFormatting.AQUA),
    STABLE("Stable", ChatFormatting.YELLOW);

    private final String label;
    private final ChatFormatting color;

    State(String label, ChatFormatting color) {
        this.label = label;
        this.color = color;
    }

    public String label() {
        return label;
    }

    public ChatFormatting color() {
        return color;
    }
}
