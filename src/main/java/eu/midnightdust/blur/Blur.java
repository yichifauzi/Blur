package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.lib.util.MidnightColorUtil;
import net.fabricmc.api.ClientModInitializer;

public class Blur implements ClientModInitializer {

    public static long start;
    public static float progress;

    public static boolean prevScreenHasBlur;
    public static boolean screenHasBlur;

    public static boolean prevScreenHasBackground;
    public static boolean screenHasBackground;

    public static boolean doTest = true;
    public static boolean screenChanged = true;
    public static long lastScreenChange = System.currentTimeMillis();

    @Override
    public void onInitializeClient() {
        BlurConfig.init("blur", BlurConfig.class);
    }

    public static boolean doFade = false;

    public static void onScreenChange() {
        if (screenHasBlur) {
            if (doFade) {
                start = System.currentTimeMillis();
                doFade = false;
            }
        } else if (prevScreenHasBlur && BlurConfig.fadeOutTimeMillis > 0) {
            start = System.currentTimeMillis();
            doFade = true;
        } else {
            start = -1;
            doFade = true;
        }
    }

    public static void updateProgress(boolean fadeIn) {
        float x;
        if (fadeIn) {
            x = Math.min((System.currentTimeMillis() - start) / (float) BlurConfig.fadeTimeMillis, 1);
            if (BlurConfig.animationCurve.equals(BlurConfig.AnimationCurve.EASE)) x *= (2 - x);  // easeInCubic
        }
        else {
            x = Math.max(1 + (start - System.currentTimeMillis()) / (float) BlurConfig.fadeOutTimeMillis, 0);
            if (BlurConfig.animationCurve.equals(BlurConfig.AnimationCurve.EASE)) x *= (2 - x);  // easeOutCubic
            if (x <= 0) {
                start = -1;
                x = 0;
            }
        }
        Blur.progress = x;
    }

    public static int getBackgroundColor(boolean second) {
        int a = second ? BlurConfig.gradientEndAlpha : BlurConfig.gradientStartAlpha;
        var col = MidnightColorUtil.hex2Rgb(second ? BlurConfig.gradientEnd : BlurConfig.gradientStart);
        int r = (col.getRGB() >> 16) & 0xFF;
        int b = (col.getRGB() >> 8) & 0xFF;
        int g = col.getRGB() & 0xFF;
        float prog = progress;
        a *= prog;
        r *= prog;
        g *= prog;
        b *= prog;
        return a << 24 | r << 16 | b << 8 | g;
    }
}
