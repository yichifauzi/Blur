package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class MixinInGameHud {
    @Final @Shadow private MinecraftClient client;

    @Inject(at = @At("TAIL"), method = "render")
    public void blur$renderFadeOut(DrawContext context, float delta, CallbackInfo ci) { // Adds a fade-out effect when a player is in a world and closes all screens
        if (client.currentScreen == null && client.world != null && Blur.start >= 0 && Blur.prevScreenHasBlur) {
            Blur.doTest = false;
            Blur.screenChanged = false;
            this.client.gameRenderer.renderBlur(delta);
            this.client.getFramebuffer().beginWrite(false);

            if (Blur.prevScreenHasBackground) Blur.renderRotatedGradient(context, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        }
    }
}
