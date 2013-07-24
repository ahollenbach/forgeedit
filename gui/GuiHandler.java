package maddjak.forgeedit.gui;


import cpw.mods.fml.common.network.IGuiHandler;
import maddjak.forgeedit.inventory.ContainerCreate;
import maddjak.forgeedit.inventory.ContainerFill;
import maddjak.forgeedit.inventory.ContainerReplace;
import maddjak.forgeedit.inventory.ContainerSave;
import maddjak.forgeedit.tileentity.TileEntityCreate;
import maddjak.forgeedit.tileentity.TileEntityFill;
import maddjak.forgeedit.tileentity.TileEntityReplace;
import maddjak.forgeedit.tileentity.TileEntitySave;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if(tileEntity != null) {
			switch(id) {
			case 0:
				return new ContainerSave((TileEntitySave) tileEntity, player.inventory);
			case 1:
				return new ContainerFill((TileEntityFill) tileEntity, player.inventory);
			case 2:
				return new ContainerReplace((TileEntityReplace) tileEntity, player.inventory);
			case 3:
				return new ContainerCreate((TileEntityCreate) tileEntity, player.inventory);
			}
		}
		
		/*if (tileEntity instanceof TileEntitySave) { //Write if for each entity
			return new ContainerSave((TileEntitySave) tileEntity, player.inventory);
		} else if (tileEntity instanceof TileEntityFill) {
			return new ContainerFill((TileEntityFill) tileEntity, player.inventory);
		}*/

		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z) {
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		
		if(tileEntity != null) {
			switch(id) {
			case 0:
				return new GuiSave(player.inventory, (TileEntitySave) tileEntity);
			case 1:
				return new GuiFill(player.inventory, (TileEntityFill) tileEntity);
			case 2:
				return new GuiReplace(player.inventory, (TileEntityReplace) tileEntity);
			case 3:
				return new GuiCreate(player.inventory, (TileEntityCreate) tileEntity);
			}
		}
		/*
		if (tileEntity instanceof TileEntitySave) {
			return new GuiSave(player.inventory, (TileEntitySave) tileEntity);
		} else if (tileEntity instanceof TileEntityFill) {
			return new GuiFill(player.inventory, (TileEntityFill) tileEntity);
		}*/

		return null;
	}
}