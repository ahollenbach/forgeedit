package maddjak.forgeedit.block;

import java.util.Random;


import maddjak.forgeedit.ForgeEdit;
import maddjak.forgeedit.tileentity.TileEntitySave;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class SaveBlock extends EditorBlockContainer {
	public SaveBlock(int id, int texture, String string) {
		super(id, texture, "saveBlock");
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
									EntityPlayer player, int i, float f, float g, float t) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		int[][][] structure = getStructure(world,x,y,z);
		TileEntitySave.setStructure(structure);
		tileEntity.setWorldObj(world);
		player.openGui(ForgeEdit.instance, 0, world, x, y, z); //CHANGE: 0 is id, so increment
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntitySave();
	}
}
