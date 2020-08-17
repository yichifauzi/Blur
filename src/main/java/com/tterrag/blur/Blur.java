package com.tterrag.blur;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Blur implements ClientModInitializer {

    public static final String MODID = "blur";
    public static final String MOD_NAME = "Blur";

    static class ConfigJson {
        String[] blurExclusions = new String[]{ ChatScreen.class.getName() };
        int fadeTimeMillis = 200;
        int radius = 8;
        String gradientStartColor = "75000000";
        String gradientEndColor = "75000000";
    }

    private long start;

    public ConfigJson configs = new ConfigJson();
    public int colorFirst, colorSecond;

    private final ManagedShaderEffect blur = ShaderEffectManager.getInstance().manage(new Identifier(MODID, "shaders/post/fade_in_blur.json"),
            shader -> shader.setUniformValue("Radius", (float) getRadius()));
    private final Uniform1f blurProgress = blur.findUniform1f("Progress");

    public static final Blur INSTANCE = new Blur();

    @Override
    public void onInitializeClient() {
        Path configFile = FabricLoader.getInstance().getConfigDir().resolve(Blur.MODID + ".json");
        try {
            if (!Files.exists(configFile)) {
                Files.createDirectories(configFile.getParent());
                Files.write(configFile, new GsonBuilder().setPrettyPrinting().create().toJson(configs).getBytes(), StandardOpenOption.CREATE_NEW);
            } else {
                configs = new Gson().fromJson(Files.newBufferedReader(configFile), ConfigJson.class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        colorFirst = Integer.parseUnsignedInt(configs.gradientStartColor, 16);
        colorSecond = Integer.parseUnsignedInt(configs.gradientEndColor, 16);

        ShaderEffectRenderCallback.EVENT.register((deltaTick) -> {
            if (start > 0) {
                blurProgress.set(getProgress());
                blur.render(deltaTick);
            }
        });
    }

    public void onScreenChange(Screen newGui) {
        if (MinecraftClient.getInstance().world != null) {
            boolean excluded = newGui == null || ArrayUtils.contains(configs.blurExclusions, newGui.getClass().getName());
            if (!excluded) {
                start = System.currentTimeMillis();
            } else {
                start = -1;
            }
        }
    }

    public int getRadius() {
        return configs.radius;
    }

    private float getProgress() {
        return Math.min((System.currentTimeMillis() - start) / (float) configs.fadeTimeMillis, 1);
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
