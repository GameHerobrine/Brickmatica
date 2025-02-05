package net.skidcode.gh.brickmatica.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.src.ISaveHandler;
import net.minecraft.src.World;

@Mixin(World.class)
public interface WorldAccessor {
	
	@Accessor("saveHandler")
	public ISaveHandler saveHandler();
}
