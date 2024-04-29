package eu.midnightdust.blur;

import eu.midnightdust.blur.config.BlurConfig;
import eu.midnightdust.blur.util.RainbowColor;
import eu.midnightdust.lib.util.MidnightColorUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.DrawContext;
import org.joml.Math;

import java.awt.*;
import java.lang.Double;

import static eu.midnightdust.blur.util.RainbowColor.hue;
import static eu.midnightdust.blur.util.RainbowColor.hue2;

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
        ClientTickEvents.END_CLIENT_TICK.register(RainbowColor::tick);
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
        double x;
        if (fadeIn) {
            x = Math.min((System.currentTimeMillis() - start) / (double) BlurConfig.fadeTimeMillis, 1);
        }
        else {
            x = Math.max(1 + (start - System.currentTimeMillis()) / (double) BlurConfig.fadeOutTimeMillis, 0);
            if (x <= 0) {
                start = -1;
            }
        }
        x = BlurConfig.animationCurve.apply(x, fadeIn);
        x = Math.clamp(0, 1, x);

        Blur.progress = Double.valueOf(x).floatValue();
    }

    public static int getBackgroundColor(boolean second) {
        int a = second ? BlurConfig.gradientEndAlpha : BlurConfig.gradientStartAlpha;
        var col = MidnightColorUtil.hex2Rgb(second ? BlurConfig.gradientEnd : BlurConfig.gradientStart);
        if (BlurConfig.rainbowMode) col = second ? Color.getHSBColor(hue, 1, 1) : Color.getHSBColor(hue2, 1, 1);
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
    public static int getRotation() {
        if (BlurConfig.rainbowMode) return RainbowColor.rotation;
        return BlurConfig.gradientRotation;
    }
    public static boolean renderRotatedGradient(DrawContext context, int width, int height) {
        if (getRotation() > 0) {
            context.getMatrices().peek().getPositionMatrix().rotationZ(Math.toRadians(getRotation()));
            context.getMatrices().peek().getPositionMatrix().setTranslation(width / 2f, height / 2f, 0); // Make the gradient's center the pivot point
            context.getMatrices().peek().getPositionMatrix().scale(Math.sqrt((float) width*width + height*height) / height); // Scales the gradient to the maximum diagonal value needed
            context.fillGradient(-width / 2, -height / 2, width / 2, height / 2, Blur.getBackgroundColor(false), Blur.getBackgroundColor(true)); // Actually draw the gradient
            context.getMatrices().peek().getPositionMatrix().rotationZ(0);
            return true;
        } return false;
    }
}
