package net.skidcode.gh.brickmatica;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiSchematicControl extends GuiScreen {
	private final Settings settings = Settings.instance();

    private int centerX = 0;
	private int centerY = 0;

	private GuiButton btnDecX = null;
	private GuiButton btnAmountX = null;
	private GuiButton btnIncX = null;

	private GuiButton btnDecY = null;
	private GuiButton btnAmountY = null;
	private GuiButton btnIncY = null;

	private GuiButton btnDecZ = null;
	private GuiButton btnAmountZ = null;
	private GuiButton btnIncZ = null;

	private GuiButton btnDecLayer = null;
	private GuiButton btnIncLayer = null;

	private GuiButton btnHide = null;
	private GuiButton btnMove = null;
	private GuiButton btnFlip = null;
	private GuiButton btnRotate = null;

	private int incrementX = 0;
	private int incrementY = 0;
	private int incrementZ = 0;

	@SuppressWarnings("unchecked")
	@Override
	public void initGui() {
		this.centerX = this.width / 2;
		this.centerY = this.height / 2;

		this.controlList.clear();

		int id = 0;

		this.btnDecX = new GuiButton(id++, this.centerX - 50, this.centerY - 30, 30, 20, "-");
		this.controlList.add(this.btnDecX);

		this.btnAmountX = new GuiButton(id++, this.centerX - 15, this.centerY - 30, 30, 20, Integer.toString(this.settings.increments[this.incrementX]));
		this.controlList.add(this.btnAmountX);

		this.btnIncX = new GuiButton(id++, this.centerX + 20, this.centerY - 30, 30, 20, "+");
		this.controlList.add(this.btnIncX);

		this.btnDecY = new GuiButton(id++, this.centerX - 50, this.centerY - 5, 30, 20, "-");
		this.controlList.add(this.btnDecY);

		this.btnAmountY = new GuiButton(id++, this.centerX - 15, this.centerY - 5, 30, 20, Integer.toString(this.settings.increments[this.incrementY]));
		this.controlList.add(this.btnAmountY);

		this.btnIncY = new GuiButton(id++, this.centerX + 20, this.centerY - 5, 30, 20, "+");
		this.controlList.add(this.btnIncY);

		this.btnDecZ = new GuiButton(id++, this.centerX - 50, this.centerY + 20, 30, 20, "-");
		this.controlList.add(this.btnDecZ);

		this.btnAmountZ = new GuiButton(id++, this.centerX - 15, this.centerY + 20, 30, 20, Integer.toString(this.settings.increments[this.incrementZ]));
		this.controlList.add(this.btnAmountZ);

		this.btnIncZ = new GuiButton(id++, this.centerX + 20, this.centerY + 20, 30, 20, "+");
		this.controlList.add(this.btnIncZ);

		this.btnDecLayer = new GuiButton(id++, this.width - 90, this.height - 150, 25, 20, "-");
		this.controlList.add(this.btnDecLayer);

		this.btnIncLayer = new GuiButton(id++, this.width - 35, this.height - 150, 25, 20, "+");
		this.controlList.add(this.btnIncLayer);

		this.btnHide = new GuiButton(id++, this.width - 90, this.height - 105, 80, 20, this.settings.isRenderingSchematic ? "Hide" : "Show");
		this.controlList.add(this.btnHide);

		this.btnMove = new GuiButton(id++, this.width - 90, this.height - 80, 80, 20, "Move here");
		this.controlList.add(this.btnMove);

		this.btnFlip = new GuiButton(id++, this.width - 90, this.height - 55, 80, 20, "Flip");
		this.controlList.add(this.btnFlip);

		this.btnRotate = new GuiButton(id, this.width - 90, this.height - 30, 80, 20, "Rotate");
		this.controlList.add(this.btnRotate);
	}

	public static int clamp_int(int par0, int par1, int par2)
    {
        return Math.min(Math.max(par0, par1), par2);
    }

	@Override
	protected void actionPerformed(GuiButton guiButton) {
		if (guiButton.enabled) {
			if (guiButton.id == this.btnDecX.id) {
				this.settings.offset.x -= this.settings.increments[this.incrementX];
				this.settings.reloadChunkCache();
			} else if (guiButton.id == this.btnIncX.id) {
				this.settings.offset.x += this.settings.increments[this.incrementX];
				this.settings.reloadChunkCache();
			} else if (guiButton.id == this.btnAmountX.id) {
				this.incrementX = (this.incrementX + 1) % this.settings.increments.length;
				this.btnAmountX.displayString = Integer.toString(this.settings.increments[this.incrementX]);
			} else if (guiButton.id == this.btnDecY.id) {
				this.settings.offset.y -= this.settings.increments[this.incrementY];
				this.settings.reloadChunkCache();
			} else if (guiButton.id == this.btnIncY.id) {
				this.settings.offset.y += this.settings.increments[this.incrementY];
				this.settings.reloadChunkCache();
			} else if (guiButton.id == this.btnAmountY.id) {
				this.incrementY = (this.incrementY + 1) % this.settings.increments.length;
				this.btnAmountY.displayString = Integer.toString(this.settings.increments[this.incrementY]);
			} else if (guiButton.id == this.btnDecZ.id) {
				this.settings.offset.z -= this.settings.increments[this.incrementZ];
				this.settings.reloadChunkCache();
			} else if (guiButton.id == this.btnIncZ.id) {
				this.settings.offset.z += this.settings.increments[this.incrementZ];
				this.settings.reloadChunkCache();
			} else if (guiButton.id == this.btnAmountZ.id) {
				this.incrementZ = (this.incrementZ + 1) % this.settings.increments.length;
				this.btnAmountZ.displayString = Integer.toString(this.settings.increments[this.incrementZ]);
			} else if (guiButton.id == this.btnDecLayer.id) {
				if (this.settings.schematic != null) {
					this.settings.renderingLayer = clamp_int(this.settings.renderingLayer - 1, -1, this.settings.schematic.height() - 1);
				} else {
					this.settings.renderingLayer = -1;
				}
				this.settings.needsUpdate = true;
			} else if (guiButton.id == this.btnIncLayer.id) {
				if (this.settings.schematic != null) {
					this.settings.renderingLayer = clamp_int(this.settings.renderingLayer + 1, -1, this.settings.schematic.height() - 1);
				} else {
					this.settings.renderingLayer = -1;
				}
				this.settings.needsUpdate = true;
			} else if (guiButton.id == this.btnHide.id) {
				this.settings.toggleRendering();
				this.btnHide.displayString = this.settings.isRenderingSchematic ? "Hide" : "Show";
				this.settings.needsUpdate = true;
			} else if (guiButton.id == this.btnMove.id) {
				this.settings.moveHere();
			} else if (guiButton.id == this.btnFlip.id) {
				this.settings.flipWorld();
			} else if (guiButton.id == this.btnRotate.id) {
				this.settings.rotateWorld();
			}
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		// drawDefaultBackground();

		drawCenteredString(this.fontRenderer, "Move schematic", this.centerX, this.centerY - 45, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, "Layers", this.width - 50, this.height - 165, 0xFFFFFF);
		drawCenteredString(this.fontRenderer, "Operations", this.width - 50, this.height - 120, 0xFFFFFF);

		drawCenteredString(this.fontRenderer, this.settings.renderingLayer < 0 ? "ALL" : Integer.toString(this.settings.renderingLayer + 1), this.width - 50, this.height - 145, 0xFFFFFF);

		drawString(this.fontRenderer, "X:", this.centerX - 65, this.centerY - 24, 0xFFFFFF);
		drawString(this.fontRenderer, Integer.toString(this.settings.offset.x), this.centerX + 55, this.centerY - 24, 0xFFFFFF);

		drawString(this.fontRenderer, "Y:", this.centerX - 65, this.centerY + 1, 0xFFFFFF);
		drawString(this.fontRenderer, Integer.toString(this.settings.offset.y), this.centerX + 55, this.centerY + 1, 0xFFFFFF);

		drawString(this.fontRenderer, "Z:", this.centerX - 65, this.centerY + 26, 0xFFFFFF);
		drawString(this.fontRenderer, Integer.toString(this.settings.offset.z), this.centerX + 55, this.centerY + 26, 0xFFFFFF);

		super.drawScreen(par1, par2, par3);
	}
}
