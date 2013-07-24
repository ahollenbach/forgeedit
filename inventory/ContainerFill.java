package maddjak.forgeedit.inventory;


import maddjak.forgeedit.tileentity.TileEntityFill;
import maddjak.forgeedit.tileentity.TileEntitySave;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerFill extends Container {
	protected TileEntityFill tileEntity;

	public ContainerFill(TileEntityFill tileEntity,
			InventoryPlayer player_inventory) {
		this.tileEntity = tileEntity;

		int width = 18;
		int height = 18;
		int id = 0;						        	// id, x, y
		addSlotToContainer(new Slot(tileEntity, id, 36 + 1 * width, 32));
		//addSlotToContainer(new Slot(tileEntity, 1, 62 + 2 * width, 17));
		
		bindPlayerInventory(player_inventory);
	}

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tileEntity.isUseableByPlayer(player);
	}

	protected void bindPlayerInventory(InventoryPlayer playerInventory) {
		// packback
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}
		// hotbar
		for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}

	/* TODO: Figure this one out
	@Override
	public ItemStack putStackInSlot(int slot_index, ItemStack items) {
		ItemStack stack = null;
		Slot slot_object = (Slot) inventorySlots.get(slot_index);

		if (slot_object != null && slot_object.getHasStack()) {
			ItemStack stack_in_slot = slot_object.getStack();
			stack = stack_in_slot.copy();

			if (slot_index == 0) {
				if (!mergeItemStack(stack_in_slot, 1, inventorySlots.size(),
						true)) {
					return null;
				}
			} else if (!mergeItemStack(stack_in_slot, 0, 1, false)) {
				return null;
			}

			if (stack_in_slot.stackSize == 0) {
				slot_object.putStack(null);
			} else {
				slot_object.onSlotChanged();
			}
		}

		return stack;
	}*/
}
