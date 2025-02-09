package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.src.EntityRenderer;
import net.skidcode.gh.brickmatica.Brickmatica;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin{
	@Inject(method = "renderRainSnow", at = @At("HEAD"))
	public void renderRainSnow(float f, CallbackInfo inf) {
		Brickmatica.render.onRender(f);
	}
	
	@Inject(method = "updateCameraAndRender", at = @At("TAIL"))
	public void updateCameraAndRender_ml(float f, CallbackInfo inf) {
		Brickmatica.handleKeyInputs();
	}
}
