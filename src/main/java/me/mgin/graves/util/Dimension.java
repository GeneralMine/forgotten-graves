package me.mgin.graves.util;

import net.minecraft.world.World;

public class Dimension {
    public static String getKey(World world) {
        return world.getRegistryKey().getValue().toString();
    }
}
