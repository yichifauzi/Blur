package com.tterrag.blur.config;

import com.tterrag.blur.Blur;
import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;

public class BlurConfig extends MidnightConfig {
    @Entry
    public static List<String> blurExclusions = Blur.defaultExclusions;
    @Entry(min = 0, max = 5000, width = 4)
    public static int fadeTimeMillis = 200;
    @Entry
    public static boolean ease = true;
    @Entry(min = 0, max = 500, width = 3)
    public static int radius = 8;
    @Entry(isColor = true, width = 7, min = 7)
    public static String gradientStart = "#000000";
    @Entry(min = 0, max = 255)
    public static int gradientStartAlpha = 75;
    @Entry(isColor = true, width = 7, min = 7)
    public static String gradientEnd = "#000000";
    @Entry(min = 0, max = 255)
    public static int gradientEndAlpha = 75;
    @Entry
    public static boolean showScreenTitle = false;
}
