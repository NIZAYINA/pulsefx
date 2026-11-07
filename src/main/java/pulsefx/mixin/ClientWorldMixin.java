package pulsefx.mixin;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pulsefx.FxConfig;
import pulsefx.SkyPresets;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "getSkyColor", at = @At("RETURN"), cancellable = true)
    private void pulsefx$sky(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Integer> cir) {
        if (FxConfig.sky != 0) cir.setReturnValue(SkyPresets.sky());
    }

    @Inject(method = "getCloudsColor", at = @At("RETURN"), cancellable = true)
    private void pulsefx$clouds(float tickDelta, CallbackInfoReturnable<Integer> cir) {
        if (FxConfig.sky != 0) cir.setReturnValue(SkyPresets.clouds());
    }
}
