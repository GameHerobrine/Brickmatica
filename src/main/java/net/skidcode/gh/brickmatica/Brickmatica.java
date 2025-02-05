package net.skidcode.gh.brickmatica;

import org.lwjgl.input.Keyboard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.KeyBinding;
import net.skidcode.gh.brickmatica.mixin.WorldAccessor;
import net.skidcode.gh.brickmatica.util.Utils;

public class Brickmatica implements ModInitializer{
	public static Minecraft mc = Utils.mc();
	public static net.skidcode.gh.brickmatica.Render render;
	
	public static ISaveHandler getSaveHandler() {
		return ((WorldAccessor)mc.theWorld).saveHandler();
	}
	
	@Override
	public void onInitialize() {
		
		//Configuration configuration = new Configuration(new File(Minecraft.getMinecraftDir(), "/config/Schematica.cfg"));
		//configuration.load();
		Settings settings = Settings.instance();
		settings.enableAlpha = false;//Config.getBoolean(configuration, "alphaEnabled", "general", settings.enableAlpha, "Enable transparent textures.");
		settings.alpha = 255/*(float)Config.getInt(configuration, "alpha", "general", (int)(settings.alpha * 255F), 0, 255, "Alpha value used when rendering the schematic.")*/ / 255F;
		settings.highlight = true/*Config.getBoolean(configuration, "highlight", "general", settings.highlight, "Highlight invalid placed blocks and to be placed blocks.");*/;
		settings.renderRange.x = 20/*Config.getInt(configuration, "renderRangeX", "general", settings.renderRange.x, 5, 50, "Render range along the X axis.")*/;
		settings.renderRange.y = 15/*Config.getInt(configuration, "renderRangeY", "general", settings.renderRange.y, 5, 50, "Render range along the Y axis.")*/;
		settings.renderRange.z = 20/*Config.getInt(configuration, "renderRangeZ", "general", settings.renderRange.z, 5, 50, "Render range along the Z axis.")*/;
		settings.blockDelta = 0.005f/*Config.getFloat(configuration, "blockDelta", "general", settings.blockDelta, 0.0F, 0.5F, "Delta value used for highlighting (if you're having issue with overlapping textures try setting this value higher).")*/;
		//configuration.save();
		//ModLoader.SetInGUIHook(this, true, false);
		
		Settings.schematicDirectory.mkdirs();
		Settings.textureDirectory.mkdirs();
	}

	public static void startGame() {
		render = new Render();
	}

	public static void initOptions(GameSettings opts) {
		Settings settings = Settings.instance();
		KeyBinding keys[] = opts.keyBindings;
		KeyBinding nkeys[] = new KeyBinding[keys.length + settings.keyBindings.length];
		System.arraycopy(keys, 0, nkeys, 0, keys.length);
		for(int i = keys.length; i < nkeys.length; ++i) {
			nkeys[i] = settings.keyBindings[i - keys.length];
		}
		opts.keyBindings = nkeys;
	}
	
	
	public static void handleKeyInputs() {
		Settings settings = Settings.instance();
		for(int i = 0; mc.currentScreen == null && i < settings.keyBindings.length; ++i) {
			KeyBinding kb = settings.keyBindings[i];
			if(Keyboard.isKeyDown(kb.keyCode)) {
				settings.keyboardEvent(kb);
			}
		}
	}

}
