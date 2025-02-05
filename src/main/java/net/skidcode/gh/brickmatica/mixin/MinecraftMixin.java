package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.Minecraft;
import net.skidcode.gh.brickmatica.Brickmatica;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	
	@Inject(method = "startGame()V", at = @At("TAIL"))
	public void initDone(CallbackInfo ci) {
		Brickmatica.startGame();
	}
}
