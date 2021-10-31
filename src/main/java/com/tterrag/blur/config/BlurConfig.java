package com.tterrag.blur.config;

import com.tterrag.blur.Blur;
import eu.midnightdust.lib.config.MidnightConfig;

import java.util.List;

public class BlurConfig extends MidnightConfig {
    @Entry
    public static List<String> blurExclusions = Blur.defaultExclusions;
    @Entry(min = 0, max = 5000, width = 4)
    public static int fadeTimeMillis = 200;
    @Entry(min = 0, max = 500, width = 3)
    public static int radius = 8;
    @Entry(min = 0, max = 99999999, width = 8)
    public static int gradientStartColor = 75000000;
    @Entry(min = 0, max = 99999999, width = 8)
    public static int gradientEndColor = 75000000;
    @Entry
    public static boolean showScreenTitle = false;
}
