package com.tterrag.blur.config;

import net.minecraft.client.gui.screen.ChatScreen;

public class BlurConfig extends MidnightConfig {
    @Entry
    public static String[] blurExclusions = new String[]{ ChatScreen.class.getName() };
    @Entry(min = 0, max = 5000)
    public static int fadeTimeMillis = 200;
    @Entry(min = 0, max = 500)
    public static int radius = 8;
    @Entry(min = 0, max = 99999999)
    public static int gradientStartColor = 75000000;
    @Entry(min = 0, max = 99999999)
    public static int gradientEndColor = 75000000;
}
