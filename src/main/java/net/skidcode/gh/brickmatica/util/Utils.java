package net.skidcode.gh.brickmatica.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.skidcode.gh.brickmatica.Settings;

public class Utils {
	public static Minecraft mc;
	public static Minecraft mc() {
		if(mc == null) mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
		return mc;
	}
	
	public static void rerenderSchematica() {
		Settings.instance().needsUpdate = true;
	}
}
