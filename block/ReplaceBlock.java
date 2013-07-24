package maddjak.forgeedit.block;


import maddjak.forgeedit.ForgeEdit;
import maddjak.forgeedit.tileentity.TileEntityReplace;
import maddjak.forgeedit.tileentity.TileEntitySave;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ReplaceBlock extends EditorBlockContainer {

	public ReplaceBlock(int id, int texture) {
		super(id, texture, "replaceBlock");
	}

	public boolean onBlockActivated(World world, int x, int y, int z,
									EntityPlayer player, int par6, float par7, float par8, float par9) {

		setBounds(world, x, y, z, "replace");
		
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);

		if (tileEntity == null || player.isSneaking()) {
			return false;
		}
		tileEntity.setWorldObj(world);
		player.openGui(ForgeEdit.instance, 2, world, x, y, z);
		
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return new TileEntityReplace();
	}

}
