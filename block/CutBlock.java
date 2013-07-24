package maddjak.forgeedit.block;

import java.util.Random;


import maddjak.forgeedit.ForgeEdit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.gen.layer.GenLayerBiome;

public class CutBlock extends EditorBlock {
	
	private static int[][][] curStruct;
	
	public CutBlock(int id, int texture) {
		super(id, texture, "cutBlock");
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer par5EntityPlayer, 
										int par6, float par7, float par8, float par9) {
		
		int[][][] struct = getStructure(world,x,y,z);
		curStruct = struct;
		fillArea(world,x,y,z, width, height, depth, 0);
		if(!world.isRemote) {
			ForgeEdit.inform("Structure copied to clipboard");
		}
		return true;
	}
}