package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.src.PlayerController;
import net.skidcode.gh.brickmatica.util.Utils;

@Mixin(PlayerController.class)
public class PlayerControllerMixin{
	
	@Inject(method = "sendPlaceBlock", at = @At("RETURN"))
	public void sendPlaceBlock(CallbackInfoReturnable<Boolean> inf) {
		Utils.rerenderSchematica();
	}
}
