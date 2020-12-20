package com.tterrag.blur.config;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import net.minecraft.client.gui.screen.ChatScreen;

@Config(name = "blur")
public class BlurConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public String[] blurExclusions = new String[]{ ChatScreen.class.getName() };
    public int fadeTimeMillis = 200;
    public int radius = 8;
    public String gradientStartColor = "75000000";
    public String gradientEndColor = "75000000";
}
