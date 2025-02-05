package net.skidcode.gh.brickmatica;

import net.minecraft.src.Block;
import net.minecraft.src.BlockChest;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.SignModel;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.TileEntityRenderer;
import net.minecraft.src.TileEntitySign;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderTileEntity {
	private final Settings settings = Settings.instance();
	//private final ModelChest chestModel = new ModelChest();
	//private final ModelChest largeChestModel = new ModelLargeChest();
	private final SignModel modelSign = new SignModel();
	private final SchematicWorld world;

	public RenderTileEntity(SchematicWorld world) {
		this.world = world;
	}

	public void renderTileEntitySignAt(TileEntitySign par1TileEntitySign) {
		Block var9 = getBlockType(par1TileEntitySign);
		GL11.glPushMatrix();
		float var10 = 0.6666667f;
		float var12;

		if (var9 == Block.signPost) {
			GL11.glTranslatef(par1TileEntitySign.xCoord + 0.5f, par1TileEntitySign.yCoord + 0.75f * var10, par1TileEntitySign.zCoord + 0.5f);
			float var11 = getBlockMetadata(par1TileEntitySign) * 360 / 16.0f;
			GL11.glRotatef(-var11, 0.0f, 1.0f, 0.0f);
			this.modelSign.signStick.showModel = true;
		} else {
			int var16 = getBlockMetadata(par1TileEntitySign);
			var12 = 0.0f;

			if (var16 == 2) {
				var12 = 180.0f;
			}

			if (var16 == 4) {
				var12 = 90.0f;
			}

			if (var16 == 5) {
				var12 = -90.0f;
			}

			GL11.glTranslatef(par1TileEntitySign.xCoord + 0.5f, par1TileEntitySign.yCoord + 0.75f * var10, par1TileEntitySign.zCoord + 0.5f);
			GL11.glRotatef(-var12, 0.0f, 1.0f, 0.0f);
			GL11.glTranslatef(0.0f, -0.3125f, -0.4375f);
			this.modelSign.signStick.showModel = false;
		}

		this.bindTextureByName("/item/sign.png");
		GL11.glPushMatrix();
		GL11.glScalef(var10, -var10, -var10);
		this.modelSign.func_887_a();
		GL11.glPopMatrix();

		FontRenderer var17 = this.settings.minecraft.fontRenderer;
		var12 = 0.016666668f * var10;
		GL11.glTranslatef(0.0f, 0.5f * var10, 0.07f * var10);
		GL11.glScalef(var12, -var12, var12);
		GL11.glNormal3f(0.0f, 0.0f, -1.0f * var12);
		GL11.glDepthMask(false);
		int var13 = (int) (this.settings.alpha * 255) * 0x1000000;

		for (int var14 = 0; var14 < par1TileEntitySign.signText.length; ++var14) {
			String var15 = par1TileEntitySign.signText[var14];
			var17.drawString(var15, -var17.getStringWidth(var15) / 2, var14 * 10 - par1TileEntitySign.signText.length * 5, var13);
		}

		GL11.glDepthMask(true);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, this.settings.alpha);
		GL11.glPopMatrix();
	}

	private Block getBlockType(TileEntity tileEntity) {
		return this.world.getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
	}

	private int getBlockMetadata(TileEntity tileEntity) {
		return this.world.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
	}

	private void unifyAdjacentChests(BlockChest par1BlockChest, SchematicWorld par2World, int par3, int par4, int par5) {
		int var5 = par2World.getBlockId(par3, par4, par5 - 1);
		int var6 = par2World.getBlockId(par3, par4, par5 + 1);
		int var7 = par2World.getBlockId(par3 - 1, par4, par5);
		int var8 = par2World.getBlockId(par3 + 1, par4, par5);
		int var10;
		int var11;
		byte metadata;
		int var14;

		if (var5 != par1BlockChest.blockID && var6 != par1BlockChest.blockID) {
			if (var7 != par1BlockChest.blockID && var8 != par1BlockChest.blockID) {
				metadata = 3;

				if (Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var6]) {
					metadata = 3;
				}

				if (Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var5]) {
					metadata = 2;
				}

				if (Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var8]) {
					metadata = 5;
				}

				if (Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var7]) {
					metadata = 4;
				}
			} else {
				var10 = par2World.getBlockId(var7 == par1BlockChest.blockID ? par3 - 1 : par3 + 1, par4, par5 - 1);
				var11 = par2World.getBlockId(var7 == par1BlockChest.blockID ? par3 - 1 : par3 + 1, par4, par5 + 1);
				metadata = 3;
				if (var7 == par1BlockChest.blockID) {
					var14 = par2World.getBlockMetadata(par3 - 1, par4, par5);
				} else {
					var14 = par2World.getBlockMetadata(par3 + 1, par4, par5);
				}

				if (var14 == 2) {
					metadata = 2;
				}

				if ((Block.opaqueCubeLookup[var5] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var6] && !Block.opaqueCubeLookup[var11]) {
					metadata = 3;
				}

				if ((Block.opaqueCubeLookup[var6] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var5] && !Block.opaqueCubeLookup[var10]) {
					metadata = 2;
				}
			}
		} else {
			var10 = par2World.getBlockId(par3 - 1, par4, var5 == par1BlockChest.blockID ? par5 - 1 : par5 + 1);
			var11 = par2World.getBlockId(par3 + 1, par4, var5 == par1BlockChest.blockID ? par5 - 1 : par5 + 1);
			metadata = 5;
			if (var5 == par1BlockChest.blockID) {
				var14 = par2World.getBlockMetadata(par3, par4, par5 - 1);
			} else {
				var14 = par2World.getBlockMetadata(par3, par4, par5 + 1);
			}

			if (var14 == 4) {
				metadata = 4;
			}

			if ((Block.opaqueCubeLookup[var7] || Block.opaqueCubeLookup[var10]) && !Block.opaqueCubeLookup[var8] && !Block.opaqueCubeLookup[var11]) {
				metadata = 5;
			}

			if ((Block.opaqueCubeLookup[var8] || Block.opaqueCubeLookup[var11]) && !Block.opaqueCubeLookup[var7] && !Block.opaqueCubeLookup[var10]) {
				metadata = 4;
			}

			par2World.setBlockMetadata(par3, par4, par5, metadata);
		}
	}

	private void bindTextureByName(String string) {
		RenderEngine var2 = TileEntityRenderer.instance.renderEngine;

		if (var2 != null) {
			var2.bindTexture(var2.getTexture(string));
		}
	}
}
