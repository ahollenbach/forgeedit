package maddjak.forgeedit.block;

import java.util.Random;


import maddjak.forgeedit.ForgeEdit;
import maddjak.forgeedit.tileentity.TileEntityFill;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.gen.layer.GenLayerBiome;

public class FillBlock extends EditorBlockContainer {

	public FillBlock(int id, int texture) {
		super(id, texture, "fillBlock");
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
									EntityPlayer player, int i, float f, float g, float t) {
		
		setBounds(world, x, y, z, "fill");
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		tileEntity.setWorldObj(world);
		player.openGui(ForgeEdit.instance, 1, world, x, y, z);
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityFill();
	}
}