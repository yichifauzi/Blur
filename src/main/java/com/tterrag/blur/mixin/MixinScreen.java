package com.tterrag.blur.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import com.tterrag.blur.Blur;

import net.minecraft.client.gui.screen.Screen;

@Mixin(Screen.class)
public class MixinScreen {

    @ModifyConstant(
            method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V",
            constant = @Constant(intValue = -1072689136))
    public int getFirstBackgroundColor(int color) {
        return Blur.INSTANCE.getBackgroundColor(false);
    }

    @ModifyConstant(
            method = "renderBackground(Lnet/minecraft/client/util/math/MatrixStack;I)V",
            constant = @Constant(intValue = -804253680))
    public int getSecondBackgroundColor(int color) {
        return Blur.INSTANCE.getBackgroundColor(true);
    }
}
