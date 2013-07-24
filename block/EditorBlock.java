package maddjak.forgeedit.block;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


import maddjak.forgeedit.CommonProxy;
import maddjak.forgeedit.ForgeEdit;
import maddjak.forgeedit.tileentity.TileEntityFill;
import maddjak.forgeedit.tileentity.TileEntityReplace;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraft.world.gen.layer.GenLayerBiome;

public abstract class EditorBlock extends Block {
	
	protected static int width;
	protected static int depth;
	protected static int height;
	
	public EditorBlock(int id, int texture, String blockName) {
		super(id, Material.ground);
		setCreativeTab(CreativeTabs.tabMisc);
		setHardness(0.1F);
		setStepSound(Block.soundGravelFootstep);
		setUnlocalizedName(blockName);
	}
	
	@SideOnly(Side.CLIENT)
	private Icon[] icons;
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconReg) {
		icons = new Icon[4];

		for(int i = 0; i < icons.length; i++) {
			icons[i] = iconReg.registerIcon(ForgeEdit.modid + ":" + (this.getUnlocalizedName().substring(5)) + i);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int metadata) {
		switch (metadata) {
		case 0:
			return icons[0];
		case 1: {
			switch (side) {
			case 1:
				return icons[3];
			case 3:
				return icons[1];
			case 5:
				return icons[2];
			default:
				return icons[0];
			}
		}
		default: {
			System.out.println("Invalid metadata for " + this.getUnlocalizedName());
			return icons[0];
		}
		}
	}
	
	//TODO: save metadata for blocks as well
	protected int[][][] getStructure(World world, int x, int y, int z) {
		if(!world.isRemote) {
			setBounds(world, x, y, z);
			
			int[][][] structure = new int[width][height][depth];
			
			for(int w = 1; w < width; w++) {
				for(int d = 1; d < depth; d++) {
					for(int h = 1; h < height; h++) {
						structure[w][h][d] = world.getBlockId(x + w, y + h, z + d);
					}
				}
			}
			CreationBlock.setLastStruct(structure);
			return structure;
		}
		return null;
	}
	
	protected static void fillArea(World world, int x, int y, int z, int width, int height, int depth, int blockID) {
		for(int w = 1; w < width; w++) {
			for(int d = 1; d < depth; d++) {
				for(int h = 1; h < height; h++) {
					world.setBlock(x + w, y + h, z + d, blockID);
				}
			}
		}
	}
	
	protected void setBounds(World world, int x, int y, int z) {
		//max search space
		int searchMax = 100;
		
		//marker block 
		//TODO: make custom marker blocks, i.e. clear area (add air first) or not, etc
		int corner = ForgeEdit.markerBlock.blockID;
		
		//search space variables
		width  = 0;
		depth  = 0;
		height = 0;
		
		//find height (y direction)
		for(int h = 1; h < searchMax; h++) {
			int curBlock = world.getBlockId(x, y + h, z);
			if(curBlock == corner) {
				height = h;
				break;
			}
		}
		if(height == 0) height = searchMax;
		
		//now width (x direction)
		for(int w = 1; w < searchMax; w++) {
			int curBlock = world.getBlockId(x + w, y, z);
			if(curBlock == corner) {
				width = w;
				break;
			}
		}
		if(width == 0) width = searchMax;
		
		//now depth (z direction)
		for(int d = 1; d < searchMax; d++) {
			int curBlock = world.getBlockId(x, y, z + d);
			if(curBlock == corner) {
				depth = d;
				break;
			}
		}
		if(depth == 0) depth = searchMax;
	}
}