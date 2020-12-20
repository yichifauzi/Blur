package com.tterrag.blur;

import com.tterrag.blur.config.BlurConfig;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;

public class Blur implements ClientModInitializer {
    public static BlurConfig BLUR_CONFIG;

    public static final String MODID = "blur";

    private long start;
    public int colorFirst, colorSecond;

    private final ManagedShaderEffect blur = ShaderEffectManager.getInstance().manage(new Identifier(MODID, "shaders/post/fade_in_blur.json"),
            shader -> shader.setUniformValue("Radius", (float) getRadius()));
    private final Uniform1f blurProgress = blur.findUniform1f("Progress");

    public static final Blur INSTANCE = new Blur();

    @Override
    public void onInitializeClient() {
        AutoConfig.register(BlurConfig.class, JanksonConfigSerializer::new);
        BLUR_CONFIG = AutoConfig.getConfigHolder(BlurConfig.class).getConfig();

        ShaderEffectRenderCallback.EVENT.register((deltaTick) -> {
            if (start > 0) {
                blurProgress.set(getProgress());
                blur.render(deltaTick);
            }
        });
    }

    private boolean doFade = false;
    public void onScreenChange(Screen newGui) {
        if (MinecraftClient.getInstance().world != null) {
            boolean excluded = newGui == null || ArrayUtils.contains(BLUR_CONFIG.blurExclusions, newGui.getClass().getName());
            if (!excluded) {
                blur.setUniformValue("Radius", (float) getRadius());
                colorFirst = Integer.parseUnsignedInt(BLUR_CONFIG.gradientStartColor, 16);
                colorSecond = Integer.parseUnsignedInt(BLUR_CONFIG.gradientEndColor, 16);
                if (doFade == true) {
                    start = System.currentTimeMillis();
                    doFade = false;
                }
            } else {
                start = -1;
                doFade = true;
            }
        }
    }

    public int getRadius() {
        return BLUR_CONFIG.radius;
    }

    private float getProgress() {
        return Math.min((System.currentTimeMillis() - start) / (float) BLUR_CONFIG.fadeTimeMillis, 1);
    }

    public int getBackgroundColor(boolean second) {
        int color = second ? colorSecond : colorFirst;
        int a = color >>> 24;
        int r = (color >> 16) & 0xFF;
        int b = (color >> 8) & 0xFF;
        int g = color & 0xFF;
        float prog = INSTANCE.getProgress();
        a *= prog;
        r *= prog;
        g *= prog;
        b *= prog;
        return a << 24 | r << 16 | b << 8 | g;
    }
}
