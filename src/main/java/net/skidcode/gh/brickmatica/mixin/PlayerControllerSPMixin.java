package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.src.PlayerController;
import net.minecraft.src.PlayerControllerSP;
import net.skidcode.gh.brickmatica.util.Utils;

@Mixin(PlayerControllerSP.class)
public class PlayerControllerSPMixin{
	
	@Inject(method = "sendBlockRemoved", at = @At("RETURN"))
	public void sendBlockRemoved(CallbackInfoReturnable<Boolean> inf) {
		Utils.rerenderSchematica();
	}
}
