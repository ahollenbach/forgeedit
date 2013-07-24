package maddjak.forgeedit.block;

import java.util.Random;


import maddjak.forgeedit.ForgeEdit;
import maddjak.forgeedit.tileentity.TileEntityCreate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.layer.GenLayerBiome;

public class CreationBlock extends EditorBlockContainer {
	
	private static int[][][] lastStruct;
	
	public CreationBlock(int id, int texture) {
		super(id, texture, "creationBlock");
		setLightValue(1.0F);
	}
	
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, 
										int par6, float par7, float par8, float par9) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		tileEntity.setWorldObj(world);
		player.openGui(ForgeEdit.instance, 3, world, x, y, z);
		return true;
	}
	
	public static void setLastStruct(int[][][] structure) {
		lastStruct = structure;
	}
	
	public static Boolean hasLastStruct() {
		return lastStruct != null;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityCreate();
	}

	public static int[][][] getLastStruct() {
		return lastStruct;
	}
}