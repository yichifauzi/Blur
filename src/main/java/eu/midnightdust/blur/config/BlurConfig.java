package eu.midnightdust.blur.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class BlurConfig extends MidnightConfig {
    public static final String ANIMATIONS = "animations";
    public static final String STYLE = "style";
    @Entry @Hidden public static int configVersion = 2;

    @Entry(category = STYLE)
    public static boolean blurContainers = true;
    @Entry(category = ANIMATIONS, min = 0, max = 2000, isSlider = true)
    public static int fadeTimeMillis = 200;
    @Entry(category = ANIMATIONS, min = 0, max = 2000, isSlider = true)
    public static int fadeOutTimeMillis = 200;
    @Entry(category = ANIMATIONS)
    public static AnimationCurve animationCurve = AnimationCurve.EASE;
    @Entry(category = STYLE)
    public static boolean useGradient = true;
    @Entry(category = STYLE, isColor = true, width = 7, min = 7)
    public static String gradientStart = "#000000";
    @Entry(category = STYLE, isSlider = true, min = 0, max = 255)
    public static int gradientStartAlpha = 75;
    @Entry(category = STYLE, isColor = true, width = 7, min = 7)
    public static String gradientEnd = "#000000";
    @Entry(category = STYLE, isSlider = true, min = 0, max = 255)
    public static int gradientEndAlpha = 75;

    public enum AnimationCurve {
        EASE, FLAT;
    }
}