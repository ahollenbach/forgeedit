package maddjak.forgeedit.inventory;


import maddjak.forgeedit.tileentity.TileEntityCreate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCreate extends Container {
	protected TileEntityCreate tileEntity;

	public ContainerCreate(TileEntityCreate tile_entity,
			InventoryPlayer player_inventory) {
		this.tileEntity = tile_entity;

		int width = 18;
		int height = 18;
		int id = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				addSlotToContainer(new Slot(tileEntity, j + i * 3, 13 + j
						* width, 17 + i * height));
			}
		}
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
	
	/**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     *//*
    public ItemStack transferStackInSlot(int par1)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par1 < 9)
            {
                if (!mergeItemStack(itemstack1, 9, inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 0, 9, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }*/
}
