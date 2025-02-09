package net.skidcode.gh.brickmatica;

import net.fabricmc.api.ClientModInitializer;
import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GameSettings;
import net.minecraft.src.KeyBinding;
import net.skidcode.gh.brickmatica.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Brickmatica implements ClientModInitializer
{
	public static final Logger LOGGER = LoggerFactory.getLogger(Brickmatica.class);

	public static final Minecraft mc = Utils.mc();
	public static net.skidcode.gh.brickmatica.Render render;

	@SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
	public void onInitializeClient() {

        Settings settings = Settings.instance();
		settings.alpha = 255 / 255F;
		settings.highlight = true;
		settings.renderRange.x = 20;
		settings.renderRange.y = 15;
		settings.renderRange.z = 20;
		settings.blockDelta = 0.005f;

        Settings.schematicDirectory.mkdirs();
        Settings.textureDirectory.mkdirs();
	}

	public static void startGame() {
		render = new Render();
	}

	public static void initOptions(GameSettings opts) {
		Settings settings = Settings.instance();
		KeyBinding[] keys = opts.keyBindings;
		KeyBinding[] nkeys = new KeyBinding[keys.length + settings.keyBindings.length];
		System.arraycopy(keys, 0, nkeys, 0, keys.length);
        if (nkeys.length - keys.length >= 0)
            System.arraycopy(settings.keyBindings, 0, nkeys, keys.length, nkeys.length - keys.length);
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
