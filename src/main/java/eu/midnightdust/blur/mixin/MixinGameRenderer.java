package eu.midnightdust.blur.mixin;

import eu.midnightdust.blur.Blur;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @ModifyVariable(method = "renderBlur", at = @At("STORE"), ordinal = 1)
    private float blur$modifyRadius(float radius) { // Modify the radius based on the animation progress

        if (!Blur.screenChanged && Blur.start >= 0) // Only update the progress after all tests have been completed
            Blur.updateProgress(Blur.screenHasBlur);
        return radius * Blur.progress;
    }
}
