package com.schnurritv.sexmod.util;

import java.util.*;
import net.minecraft.server.*;
import net.minecraft.util.math.*;

public class Reference
{
    public static final String MOD_ID = "sexmod";
    public static final String NAME = "Sex Mod";
    public static final String VERSION = "1.0";
    public static final String ACCEPTED_VERSION = "[1.12.2]";
    public static final String CLIENT = "com.schnurritv.sexmod.proxy.ClientProxy";
    public static final String COMMON = "com.schnurritv.sexmod.proxy.CommonProxy";
    public static final Random RANDOM;
    public static final int ENTITY_JENNY = 177013;
    public static final int ENTITY_ELLIE = 228922;
    public static final int GUI_GIRL = 0;
    public static MinecraftServer server;
    
    public static Vec3d Lerp(final Vec3d start, final Vec3d end, final double step) {
        if (step == 0.0) {
            return end;
        }
        try {
            final Vec3d distance = end.subtract(start);
            return start.add(distance.x / step, distance.y / step, distance.z / step);
        }
        catch (NullPointerException e) {
            System.out.println("couldn't calculate distance @Reference.Lerp");
            System.out.println(start);
            System.out.println(end);
            System.out.println(step);
            return end;
        }
    }
    
    public static double Lerp(final double start, final double end, final double step) {
        return start + (end - start) * step;
    }
    
    static {
        RANDOM = new Random();
    }
}
