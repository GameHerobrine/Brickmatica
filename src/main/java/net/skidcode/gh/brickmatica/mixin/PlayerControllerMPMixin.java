package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.src.PlayerControllerMP;
import net.skidcode.gh.brickmatica.util.Utils;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin{
	//would be much better to check packet
	@Inject(method = "sendBlockRemoved", at = @At("RETURN"))
	public void sendBlockRemoved(CallbackInfoReturnable<Boolean> inf) {
		Utils.rerenderSchematica();
	}
	
	@Inject(method = "sendPlaceBlock", at = @At("RETURN"))
	public void sendPlaceBlock(CallbackInfoReturnable<Boolean> inf) {
		Utils.rerenderSchematica();
	}
}
