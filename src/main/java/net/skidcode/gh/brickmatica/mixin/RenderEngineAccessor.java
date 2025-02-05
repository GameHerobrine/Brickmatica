package net.skidcode.gh.brickmatica.mixin;

import java.nio.IntBuffer;
import java.util.HashMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.src.RenderEngine;

@Mixin(RenderEngine.class)
public interface RenderEngineAccessor {
	@Accessor("textureMap")
	public HashMap textureMap();
	
	@Accessor("singleIntBuffer")
	public IntBuffer singleIntBuffer();
	
}
