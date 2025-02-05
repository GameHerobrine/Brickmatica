package net.skidcode.gh.brickmatica;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.Material;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.SaveHandlerMP;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;
import net.skidcode.gh.brickmatica.util.Utils;

public class SchematicWorld extends World {
	//public final static WorldSettings worldSettings = new WorldSettings(0, EnumGameType.CREATIVE, false, false, WorldType.FLAT);

	public final Settings settings = Settings.instance();
	public int[][][] blocks;
	public int[][][] metadata;
	public List<TileEntity> tileEntities;
	public short width;
	public short length;
	public short height;

	public SchematicWorld() {
		
		super(Utils.mc().theWorld, Utils.mc().theWorld.worldProvider);
		this.blocks = null;
		this.metadata = null;
		this.tileEntities = null;
		this.width = 0;
		this.length = 0;
		this.height = 0;
	}

	public SchematicWorld(int[][][] blocks, int[][][] metadata, List<TileEntity> tileEntities, short width, short height, short length) {
		this();
		this.blocks = blocks;
		this.metadata = metadata;
		this.tileEntities = tileEntities;
		this.width = width;
		this.length = length;
		this.height = height;
	}

	
	
	@SuppressWarnings("null")
	public void readFromNBT(NBTTagCompound tagCompound) {
		byte localBlocks[] = tagCompound.getByteArray("Blocks");
		byte localMetadata[] = tagCompound.getByteArray("Data");

		boolean extra = false;
		byte extraBlocks[] = null;
		if ((extra = tagCompound.hasKey("Add")) == true) {
			extraBlocks = tagCompound.getByteArray("Add");
		}

		this.width = tagCompound.getShort("Width");
		this.length = tagCompound.getShort("Length");
		this.height = tagCompound.getShort("Height");

		this.blocks = new int[this.width][this.height][this.length];
		this.metadata = new int[this.width][this.height][this.length];

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				for (int z = 0; z < this.length; z++) {
					this.blocks[x][y][z] = (localBlocks[x + (y * this.length + z) * this.width]) & 0xFF;
					this.metadata[x][y][z] = (localMetadata[x + (y * this.length + z) * this.width]) & 0xFF;
					
					if (extra) {
						this.blocks[x][y][z] |= ((extraBlocks[x + (y * this.length + z) * this.width]) & 0xFF) << 8;
					}
				}
			}
		}

		this.tileEntities = new ArrayList<TileEntity>();

		NBTTagList tileEntitiesList = tagCompound.getTagList("TileEntities");

		for (int i = 0; i < tileEntitiesList.tagCount(); i++) {
			TileEntity tileEntity = TileEntity.createAndLoadEntity((NBTTagCompound) tileEntitiesList.tagAt(i));
			if(tileEntity != null) {
				tileEntity.worldObj = this;
				this.tileEntities.add(tileEntity);
			}
		}

		refreshChests();
	}

	public void writeToNBT(NBTTagCompound tagCompound) {
		tagCompound.setShort("Width", this.width);
		tagCompound.setShort("Length", this.length);
		tagCompound.setShort("Height", this.height);

		byte localBlocks[] = new byte[this.width * this.length * this.height];
		byte localMetadata[] = new byte[this.width * this.length * this.height];
		byte extraBlocks[] = new byte[this.width * this.length * this.height];
		boolean extra = false;

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				for (int z = 0; z < this.length; z++) {
					localBlocks[x + (y * this.length + z) * this.width] = (byte) this.blocks[x][y][z];
					localMetadata[x + (y * this.length + z) * this.width] = (byte) this.metadata[x][y][z];
					if ((extraBlocks[x + (y * this.length + z) * this.width] = (byte) (this.blocks[x][y][z] >> 8)) > 0) {
						extra = true;
					}
				}
			}
		}

		tagCompound.setString("Materials", "Classic");
		tagCompound.setByteArray("Blocks", localBlocks);
		tagCompound.setByteArray("Data", localMetadata);
		if (extra) {
			tagCompound.setByteArray("Add", extraBlocks);
		}
		tagCompound.setTag("Entities", new NBTTagList());

		NBTTagList tileEntitiesList = new NBTTagList();
		for (TileEntity tileEntity : this.tileEntities) {
			NBTTagCompound tileEntityTagCompound = new NBTTagCompound();
			tileEntity.tileEntityInvalid(tileEntityTagCompound); //XXX !!!! MCP MAPPINGS ARE NOT MCP !!!!
			tileEntitiesList.setTag(tileEntityTagCompound);
		}

		tagCompound.setTag("TileEntities", tileEntitiesList);
	}

	@Override
	public int getBlockId(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length) {
			return 0;
		}
		return (this.blocks[x][y][z]) & 0xFFF;
	}

	@Override
	public TileEntity getBlockTileEntity(int x, int y, int z) {
		for (int i = 0; i < this.tileEntities.size(); i++) {
			if (this.tileEntities.get(i).xCoord == x && this.tileEntities.get(i).yCoord == y && this.tileEntities.get(i).zCoord == z) {
				return this.tileEntities.get(i);
			}
		}
		return null;
	}

	/*@Override
	public int getLightBrightnessForSkyBlocks(int var1, int var2, int var3, int var4) {
		return 15;
	}*/

	@Override
	public float getBrightness(int var1, int var2, int var3, int var4) {
		return 1.0f;
	}

	@Override
	public float getLightBrightness(int x, int y, int z) {
		return 1.0f;
	}

	@Override
	public int getBlockMetadata(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length) {
			return 0;
		}
		return this.metadata[x][y][z];
	}

	@Override
	public Material getBlockMaterial(int x, int y, int z) {
		return getBlock(x, y, z) != null ? getBlock(x, y, z).blockMaterial : Material.air;
	}

	@Override
	public boolean isBlockOpaqueCube(int x, int y, int z) {
		if (this.settings.renderingLayer != -1 && this.settings.renderingLayer != y) {
			return false;
		}
		return getBlock(x, y, z) != null && getBlock(x, y, z).isOpaqueCube();
	}

	@Override
	public boolean isBlockNormalCube(int x, int y, int z) {
		return getBlockMaterial(x, y, z).getIsTranslucent() && getBlock(x, y, z) != null && getBlock(x, y, z).renderAsNormalBlock();
	}

	@Override
	public boolean isAirBlock(int x, int y, int z) {
		if (x < 0 || y < 0 || z < 0 || x >= this.width || y >= this.height || z >= this.length) {
			return true;
		}
		return this.blocks[x][y][z] == 0;
	}

	/*@Override
	public boolean extendedLevelsInChunkCache() {
		return false;
	}

	@Override
	public boolean doesBlockHaveSolidTopSurface(int var1, int var2, int var3) {
		return false;
	}*/

	@Override
	protected IChunkProvider getChunkProvider() {
		return null;
	}

	/*@Override
	public Entity getEntityByID(int var1) {
		return null;
	}*/

	public void setBlockMetadata(int x, int y, int z, byte metadata) {
		this.metadata[x][y][z] = metadata;
	}

	public Block getBlock(int x, int y, int z) {
		return Block.blocksList[getBlockId(x, y, z)];
	}

	public void setTileEntities(List<TileEntity> tileEntities) {
		this.tileEntities = tileEntities;
	}

	public List<TileEntity> getTileEntities() {
		return this.tileEntities;
	}

	public void refreshChests() {
		TileEntity tileEntity;
		
	}

	public void checkForAdjacentChests(TileEntityChest tileEntityChest) {
		
	}

	public void flip() {
		int tmp;
		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				for (int z = 0; z < (this.length + 1) / 2; z++) {
					tmp = this.blocks[x][y][z];
					this.blocks[x][y][z] = this.blocks[x][y][this.length - 1 - z];
					this.blocks[x][y][this.length - 1 - z] = tmp;

					if (z == this.length - 1 - z) {
						this.metadata[x][y][z] = flipMetadataZ(this.metadata[x][y][z], this.blocks[x][y][z]);
					} else {
						tmp = this.metadata[x][y][z];
						this.metadata[x][y][z] = flipMetadataZ(this.metadata[x][y][this.length - 1 - z], this.blocks[x][y][z]);
						this.metadata[x][y][this.length - 1 - z] = flipMetadataZ(tmp, this.blocks[x][y][this.length - 1 - z]);
					}
				}
			}
		}

		refreshChests();
	}

	public int flipMetadataZ(int blockMetadata, int blockId) {
		if (blockId == Block.torchWood.blockID || blockId == Block.torchRedstoneActive.blockID || blockId == Block.torchRedstoneIdle.blockID) {
			switch (blockMetadata) {
			case 0x3:
				return 0x4;
			case 0x4:
				return 0x3;
			}
		} else if (blockId == Block.rail.blockID) {
			switch (blockMetadata) {
			case 0x4:
				return 0x5;
			case 0x5:
				return 0x4;
			case 0x6:
				return 0x9;
			case 0x7:
				return 0x8;
			case 0x8:
				return 0x7;
			case 0x9:
				return 0x6;
			}
		} else if (blockId == Block.railDetector.blockID || blockId == Block.railPowered.blockID) {
			switch (blockMetadata & 0x7) {
			case 0x4:
				return (byte) (0x5 | (blockMetadata & 0x8));
			case 0x5:
				return (byte) (0x4 | (blockMetadata & 0x8));
			}
		} else if (blockId == Block.stairCompactCobblestone.blockID || blockId == Block.stairCompactPlanks.blockID) {
			switch (blockMetadata & 0x3) {
			case 0x2:
				return (byte) (0x3 | (blockMetadata & 0x4));
			case 0x3:
				return (byte) (0x2 | (blockMetadata & 0x4));
			}
		} else if (blockId == Block.lever.blockID) {
			switch (blockMetadata & 0x7) {
			case 0x3:
				return (byte) (0x4 | (blockMetadata & 0x8));
			case 0x4:
				return (byte) (0x3 | (blockMetadata & 0x8));
			}
		} else if (blockId == Block.doorWood.blockID || blockId == Block.doorSteel.blockID) {
			if ((blockMetadata & 0x8) == 0x8) {
				return (byte) (blockMetadata ^ 0x1);
			}
			switch (blockMetadata & 0x3) {
			case 0x1:
				return (byte) ((0x3 | (blockMetadata & 0xC)));
			case 0x3:
				return (byte) ((0x1 | (blockMetadata & 0xC)));
			}
		}else if (blockId == Block.signPost.blockID) {
			switch (blockMetadata) {
			case 0x0:
				return 0x8;
			case 0x1:
				return 0x7;
			case 0x2:
				return 0x6;
			case 0x3:
				return 0x5;
			case 0x4:
				return 0x4;
			case 0x5:
				return 0x3;
			case 0x6:
				return 0x2;
			case 0x7:
				return 0x1;
			case 0x8:
				return 0x0;
			case 0x9:
				return 0xF;
			case 0xA:
				return 0xE;
			case 0xB:
				return 0xD;
			case 0xC:
				return 0xC;
			case 0xD:
				return 0xB;
			case 0xE:
				return 0xA;
			case 0xF:
				return 0x9;
			}
		} else if (blockId == Block.ladder.blockID || blockId == Block.signWall.blockID || blockId == Block.stoneOvenActive.blockID || blockId == Block.stoneOvenIdle.blockID || blockId == Block.dispenser.blockID || blockId == Block.chest.blockID) {
			switch (blockMetadata) {
			case 0x2:
				return 0x3;
			case 0x3:
				return 0x2;
			}
		} else if (blockId == Block.pumpkin.blockID || blockId == Block.pumpkinLantern.blockID) {
			switch (blockMetadata) {
			case 0x0:
				return 0x2;
			case 0x2:
				return 0x0;
			}
		} else if (blockId == Block.blockBed.blockID) {
			switch (blockMetadata & 0x3) {
			case 0x0:
				return (byte) (0x2 | (blockMetadata & 0xC));
			case 0x2:
				return (byte) (0x0 | (blockMetadata & 0xC));
			}
		} else if (blockId == Block.redstoneRepeaterActive.blockID || blockId == Block.redstoneRepeaterIdle.blockID) {
			switch (blockMetadata & 0x3) {
			case 0x0:
				return (byte) (0x2 | (blockMetadata & 0xC));
			case 0x2:
				return (byte) (0x0 | (blockMetadata & 0xC));
			}
		} else if (blockId == Block.trapdoor.blockID) {
			switch (blockMetadata) {
			case 0x0:
				return 0x1;
			case 0x1:
				return 0x0;
			}
		} else if (blockId == Block.pistonBase.blockID || blockId == Block.pistonStickyBase.blockID || blockId == Block.pistonExtension.blockID) {
			switch (blockMetadata & 0x7) {
			case 0x2:
				return (byte) (0x3 | (blockMetadata & 0x8));
			case 0x3:
				return (byte) (0x2 | (blockMetadata & 0x8));
			}
		}

		return blockMetadata;
	}

	public void rotate() {
		int[][][] localBlocks = new int[this.length][this.height][this.width];
		int[][][] localMetadata = new int[this.length][this.height][this.width];

		for (int x = 0; x < this.width; x++) {
			for (int y = 0; y < this.height; y++) {
				for (int z = 0; z < this.length; z++) {
					localBlocks[z][y][x] = this.blocks[this.width - 1 - x][y][z];
					localMetadata[z][y][x] = rotateMetadata(this.metadata[this.width - 1 - x][y][z], this.blocks[this.width - 1 - x][y][z]);
				}
			}
		}

		this.blocks = localBlocks;
		this.metadata = localMetadata;

		TileEntity tileEntity;
		int coord;
		refreshChests();

		short tmp = this.width;
		this.width = this.length;
		this.length = tmp;
	}

	public int rotateMetadata(int blockMetadata, int blockId) {
		if (blockId == Block.torchWood.blockID || blockId == Block.torchRedstoneActive.blockID || blockId == Block.torchRedstoneIdle.blockID) {
			switch (blockMetadata) {
			case 0x1:
				return 0x4;
			case 0x2:
				return 0x3;
			case 0x3:
				return 0x1;
			case 0x4:
				return 0x2;
			}
		} else if (blockId == Block.rail.blockID) {
			switch (blockMetadata) {
			case 0x0:
				return 0x1;
			case 0x1:
				return 0x0;
			case 0x2:
				return 0x4;
			case 0x3:
				return 0x5;
			case 0x4:
				return 0x3;
			case 0x5:
				return 0x2;
			case 0x6:
				return 0x9;
			case 0x7:
				return 0x6;
			case 0x8:
				return 0x7;
			case 0x9:
				return 0x8;
			}
		} else if (blockId == Block.railDetector.blockID || blockId == Block.railPowered.blockID) {
			switch (blockMetadata & 0x7) {
			case 0x0:
				return (byte) (0x1 | (blockMetadata & 0x8));
			case 0x1:
				return (byte) (0x0 | (blockMetadata & 0x8));
			case 0x2:
				return (byte) (0x4 | (blockMetadata & 0x8));
			case 0x3:
				return (byte) (0x5 | (blockMetadata & 0x8));
			case 0x4:
				return (byte) (0x3 | (blockMetadata & 0x8));
			case 0x5:
				return (byte) (0x2 | (blockMetadata & 0x8));
			}
		} else if (blockId == Block.stairCompactCobblestone.blockID || blockId == Block.stairCompactPlanks.blockID) {
			switch (blockMetadata & 0x3) {
			case 0x0:
				return (byte) (0x3 | (blockMetadata & 0x4));
			case 0x1:
				return (byte) (0x2 | (blockMetadata & 0x4));
			case 0x2:
				return (byte) (0x0 | (blockMetadata & 0x4));
			case 0x3:
				return (byte) (0x1 | (blockMetadata & 0x4));
			}
		} else if (blockId == Block.lever.blockID) {
			switch (blockMetadata & 0x7) {
			case 0x1:
				return (byte) (0x4 | (blockMetadata & 0x8));
			case 0x2:
				return (byte) (0x3 | (blockMetadata & 0x8));
			case 0x3:
				return (byte) (0x1 | (blockMetadata & 0x8));
			case 0x4:
				return (byte) (0x2 | (blockMetadata & 0x8));
			case 0x5:
				return (byte) (0x6 | (blockMetadata & 0x8));
			case 0x6:
				return (byte) (0x5 | (blockMetadata & 0x8));
			}
		} else if (blockId == Block.doorWood.blockID || blockId == Block.doorSteel.blockID) {
			if ((blockMetadata & 0x8) == 0x8) {
				return blockMetadata;
			}
			switch (blockMetadata & 0x3) {
			case 0x0:
				return (byte) (0x3 | (blockMetadata & 0xC));
			case 0x1:
				return (byte) (0x0 | (blockMetadata & 0xC));
			case 0x2:
				return (byte) (0x1 | (blockMetadata & 0xC));
			case 0x3:
				return (byte) (0x2 | (blockMetadata & 0xC));
			}
		}else if (blockId == Block.signPost.blockID) {
			return (byte) ((blockMetadata + 0xC) % 0x10);
			/*
			 * switch (blockMetadata) { case 0x0: return 0xC; case 0x1: return
			 * 0xD; case 0x2: return 0xE; case 0x3: return 0xF; case 0x4: return
			 * 0x0; case 0x5:
			 * return 0x1; case 0x6: return 0x2; case 0x7: return 0x3; case 0x8:
			 * return 0x4; case 0x9: return 0x5; case 0xA: return 0x6; case 0xB:
			 * return 0x7;
			 * case 0xC: return 0x8; case 0xD: return 0x9; case 0xE: return 0xA;
			 * case 0xF: return 0xB; }
			 */
		} else if (blockId == Block.ladder.blockID || blockId == Block.signWall.blockID || blockId == Block.stoneOvenActive.blockID || blockId == Block.stoneOvenIdle.blockID || blockId == Block.dispenser.blockID || blockId == Block.chest.blockID) {
			switch (blockMetadata) {
			case 0x2:
				return 0x4;
			case 0x3:
				return 0x5;
			case 0x4:
				return 0x3;
			case 0x5:
				return 0x2;
			}
		} else if (blockId == Block.pumpkin.blockID || blockId == Block.pumpkinLantern.blockID) {
			switch (blockMetadata) {
			case 0x0:
				return 0x3;
			case 0x1:
				return 0x0;
			case 0x2:
				return 0x1;
			case 0x3:
				return 0x2;
			}
		} else if (blockId == Block.blockBed.blockID) {
			switch (blockMetadata & 0x3) {
			case 0x0:
				return (byte) (0x3 | (blockMetadata & 0xC));
			case 0x1:
				return (byte) (0x0 | (blockMetadata & 0xC));
			case 0x2:
				return (byte) (0x1 | (blockMetadata & 0xC));
			case 0x3:
				return (byte) (0x2 | (blockMetadata & 0xC));
			}
		} else if (blockId == Block.redstoneRepeaterActive.blockID || blockId == Block.redstoneRepeaterIdle.blockID) {
			switch (blockMetadata & 0x3) {
			case 0x0:
				return (byte) (0x3 | (blockMetadata & 0xC));
			case 0x1:
				return (byte) (0x0 | (blockMetadata & 0xC));
			case 0x2:
				return (byte) (0x1 | (blockMetadata & 0xC));
			case 0x3:
				return (byte) (0x2 | (blockMetadata & 0xC));
			}
		} else if (blockId == Block.trapdoor.blockID) {
			switch (blockMetadata) {
			case 0x0:
				return 0x2;
			case 0x1:
				return 0x3;
			case 0x2:
				return 0x1;
			case 0x3:
				return 0x0;
			}
		} else if (blockId == Block.pistonBase.blockID || blockId == Block.pistonStickyBase.blockID || blockId == Block.pistonExtension.blockID) {
			switch (blockMetadata & 0x7) {
			case 0x0:
				return (byte) (0x0 | (blockMetadata & 0x8));
			case 0x1:
				return (byte) (0x1 | (blockMetadata & 0x8));
			case 0x2:
				return (byte) (0x4 | (blockMetadata & 0x8));
			case 0x3:
				return (byte) (0x5 | (blockMetadata & 0x8));
			case 0x4:
				return (byte) (0x3 | (blockMetadata & 0x8));
			case 0x5:
				return (byte) (0x2 | (blockMetadata & 0x8));
			}
		}
		return blockMetadata;
	}

	public int width() {
		return this.width;
	}

	public int length() {
		return this.length;
	}

	public int height() {
		return this.height;
	}
}
