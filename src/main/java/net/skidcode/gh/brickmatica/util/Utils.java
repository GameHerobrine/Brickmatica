package net.skidcode.gh.brickmatica.util;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.minecraft.client.Minecraft;
import net.skidcode.gh.brickmatica.Settings;

public class Utils {
	private static final Minecraft mc = (Minecraft) FabricLoaderImpl.INSTANCE.getGameInstance();

	public static Minecraft mc() {
		return mc;
	}
	
	public static void rerenderSchematica() {
		Settings.instance().needsUpdate = true;
	}
}
