package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.src.GameSettings;
import net.skidcode.gh.brickmatica.Brickmatica;

@Mixin(GameSettings.class)
public class GameSettingsMixin {
	@Inject(method = "loadOptions()V", at = @At("HEAD"))
	public void init(CallbackInfo ci) {
		Brickmatica.initOptions((GameSettings)(Object)this);
	}
}
