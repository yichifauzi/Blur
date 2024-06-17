package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.config.BlurConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import eu.midnightdust.blur.Blur;

import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class MixinScreen {

    @Shadow protected MinecraftClient client;

    @Shadow @Final protected Text title;

    @Shadow public abstract void renderInGameBackground(DrawContext context);

    @Shadow public int width;

    @Shadow public int height;

    @Inject(at = @At("HEAD"), method = "render")
    public void blur$processScreenChange(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!Blur.doTest && Blur.screenChanged) { // After the tests for blur and background color have been completed
            Blur.onScreenChange();
            Blur.screenChanged = false;
        }

        if (Blur.start >= 0 && !Blur.screenHasBlur && Blur.prevScreenHasBlur) { // Fade out in non-blurred screens
            this.client.gameRenderer.renderBlur(delta);
            this.client.getFramebuffer().beginWrite(false);

            if (Blur.prevScreenHasBackground) Blur.renderRotatedGradient(context, width, height);
        }
        Blur.doTest = false; // Set the test state to completed, as tests will happen in the same tick.
    }
    @Inject(at = @At("HEAD"), method = "renderInGameBackground")
    public void blur$getBackgroundEnabled(DrawContext context, CallbackInfo ci) {
        Blur.screenHasBackground = true; // Test if the screen has a background
    }
    @Inject(at = @At("HEAD"), method = "applyBlur")
    public void blur$getBlurEnabled(float delta, CallbackInfo ci) {
        if (!BlurConfig.excludedScreens.contains(this.getClass().toString()))
            Blur.screenHasBlur = true; // Test if the screen has blur
    }

    @Inject(at = @At("HEAD"), method = "renderDarkening(Lnet/minecraft/client/gui/DrawContext;IIII)V", cancellable = true)
    public void blur$applyGradient(DrawContext context, int x, int y, int width, int height, CallbackInfo ci) {
        if (BlurConfig.useGradient) { // Replaces the background texture with a gradient
            renderInGameBackground(context);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "renderInGameBackground", cancellable = true)
    public void blur$rotatedGradient(DrawContext context, CallbackInfo ci) { // Replaces the default gradient with our rotated one
        Blur.renderRotatedGradient(context, width, height);
        ci.cancel();
    }
}
