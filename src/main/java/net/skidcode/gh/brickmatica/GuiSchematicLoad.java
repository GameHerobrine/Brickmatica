package net.skidcode.gh.brickmatica;

import java.awt.*;
import java.io.File;
import java.util.List;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSmallButton;

import org.lwjgl.Sys;

public class GuiSchematicLoad extends GuiScreen {
	private final Settings settings = Settings.instance();
	private final GuiScreen prevGuiScreen;
	private GuiSchematicLoadSlot schematicGuiChooserSlot;
	private GuiSmallButton btnOpenDir = null;
	private GuiSmallButton btnDone = null;

	public GuiSchematicLoad(GuiScreen guiScreen) {
		this.prevGuiScreen = guiScreen;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		int id = 0;

		this.btnOpenDir = new GuiSmallButton(id++, this.width / 2 - 154, this.height - 48, "Open schematic folder");
		this.controlList.add(this.btnOpenDir);

		this.btnDone = new GuiSmallButton(id, this.width / 2 + 4, this.height - 48, "Done");
		this.controlList.add(this.btnDone);

		this.schematicGuiChooserSlot = new GuiSchematicLoadSlot(this);
		this.schematicGuiChooserSlot.registerScrollButtons(this.controlList, 7, 8);
	}

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton.enabled) {
			if (guiButton.id == this.btnOpenDir.id) {
				boolean success = false;

				try {
					Desktop.getDesktop().browse(Settings.schematicDirectory.toURI());
				} catch (Throwable e) {
					Brickmatica.LOGGER.error("Error occurred while trying to open directory", e);
					success = true;
				}

				if (success) {
					Brickmatica.LOGGER.info("Opening via Sys class!");
					Sys.openURL("file://" + Settings.schematicDirectory.getAbsolutePath());
				}
			} else if (guiButton.id == this.btnDone.id) {
				loadSchematic();
				this.mc.displayGuiScreen(this.prevGuiScreen);
			} else {
				this.schematicGuiChooserSlot.actionPerformed(guiButton);
			}
		}
	}

	@Override
	public void drawScreen(int x, int y, float partialTicks) {
		this.schematicGuiChooserSlot.drawScreen(x, y, partialTicks);

		this.drawCenteredString(this.fontRenderer, "Select schematic file", this.width / 2, 16, 0x00FFFFFF);
		this.drawCenteredString(this.fontRenderer, "(Place schematic files here)", this.width / 2 - 77, this.height - 26, 0x00808080);
		super.drawScreen(x, y, partialTicks);
	}

	@Override
	public void onGuiClosed() {
		// loadSchematic();
	}

	private void loadSchematic() {
		List<String> schematics = this.settings.getSchematicFiles();
		
		try {
			if (!(this.settings.selectedSchematic > 0 && this.settings.selectedSchematic < schematics.size() && this.settings.loadSchematic((new File(Settings.schematicDirectory, schematics.get(this.settings.selectedSchematic))).getPath()))) {
				this.settings.selectedSchematic = 0;
			} else {
				this.settings.renderingLayer = -1;

				if (this.settings.schematic.width() * this.settings.schematic.height() * this.settings.schematic.length() > 125000) {
					this.settings.renderingLayer = 0;
				}
			}
		} catch (Exception e) {
			this.settings.selectedSchematic = 0;
		}
		this.settings.moveHere();
	}
}
