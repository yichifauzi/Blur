package com.tterrag.blur.config;

import com.sun.org.apache.xerces.internal.xs.StringList;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.ConfigHolder;
import me.sargunvohra.mcmods.autoconfig1u.ConfigManager;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.gui.registry.DefaultGuiRegistryAccess;
import me.shedaniel.clothconfig2.api.ConfigScreen;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@Config(name = "blur")
@SuppressWarnings("No GUI provider registered")
public class BlurConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    public String[] blurExclusions = new String[]{ ChatScreen.class.getName() };
    public int fadeTimeMillis = 200;
    public int radius = 8;
    public String gradientStartColor = "75000000";
    public String gradientEndColor = "75000000";
}
