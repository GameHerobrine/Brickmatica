package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.src.World;
import net.skidcode.gh.brickmatica.SchematicWorld;

@Mixin(World.class)
public class WorldMixin {
	
	@Inject(method = "chunkExists", at = @At("HEAD"), cancellable = true)
	public void chunkExists(int x, int y, CallbackInfoReturnable<Boolean> inf) {
		World w = (World)(Object)this;
		if(w instanceof SchematicWorld) {
			inf.setReturnValue(false);
			inf.cancel();
		}
	}
}
