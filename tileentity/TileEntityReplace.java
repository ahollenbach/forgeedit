package maddjak.forgeedit.tileentity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import maddjak.forgeedit.ForgeEdit;
import maddjak.forgeedit.block.FillBlock;
import maddjak.forgeedit.block.ReplaceBlock;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityReplace extends TileEntity implements IInventory {

	private ItemStack[] inventory;
	private static World world;
	
	private static int width;
	private static int height;
	private static int depth;

	public TileEntityReplace() {
		this.inventory = new ItemStack[2];
	}
	
	@Override 
	public void setWorldObj(World world_) {
		world = world_;
		worldObj = world_;
	}

	@Override
	public int getSizeInventory() {
		return this.inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int slotIndex) {
		return this.inventory[slotIndex];
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		this.inventory[slot] = stack;

		if (stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}
	}

	/**
	 * Decreases the size of the stack at a given slot by the given amount
	 * 
	 * @param slotIndex
	 *            The slot to remove from.
	 * @param amount
	 *            The amount of items to remove.
	 */
	@Override
	public ItemStack decrStackSize(int slotIndex, int amount) {

		ItemStack stack = getStackInSlot(slotIndex);

		if (stack != null) {
			if (stack.stackSize <= amount) {
				setInventorySlotContents(slotIndex, null);
			} else {
				stack = stack.splitStack(amount);
				if (stack.stackSize == 0) {
					setInventorySlotContents(slotIndex, null);
				}
			}
		}
		return stack;
	}
	
	/**
	 * Stores the stack when closing
	 */
	@Override
	public ItemStack getStackInSlotOnClosing(int slotIndex) {
		ItemStack stack = getStackInSlot(slotIndex);
		
		if (stack != null) {
			setInventorySlotContents(slotIndex, null);
		}

		return stack;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player) {
		return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this
				&& player.getDistanceSq(xCoord + 0.5, yCoord + 0.5,
						zCoord + 0.5) < 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		NBTTagList tagList = tagCompound.getTagList("Inventory");

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.tagAt(i);
			byte slot = tag.getByte("Slot");
			if (slot >= 0 && slot < inventory.length) {
				inventory[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		NBTTagList itemList = new NBTTagList();

		for (int i = 0; i < inventory.length; i++) {
			ItemStack stack = inventory[i];

			if (stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}

		tagCompound.setTag("Inventory", itemList);
	}

	@Override
	public String getInvName() {
		return "ReplaceTileEntity";
	}

	public static void setDimensions(int width_, int height_, int depth_) {
		width = width_;
		height = height_;
		depth = depth_;
	}

	private int[] getReplaceTypes() {
		int[] blockType = new int[getSizeInventory()];
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack itemStack = getStackInSlot(i);
			if (itemStack != null) {
				blockType[i] = getStackInSlot(i).getItem().itemID;
			}
		}
		return blockType;
	}
	
	public void replaceArea() {
		int blockIDs[] = getReplaceTypes();
		//System.out.println(blockIDToFill);
		ReplaceBlock.replaceArea(world, this.xCoord, this.yCoord, this.zCoord, width, height, depth, blockIDs[0], blockIDs[1]);
		
		String name1 = blockIDs[0] == 0 ? "Air" : Block.blocksList[blockIDs[0]].getUnlocalizedName2();
		String name2 = blockIDs[1] == 0 ? "Air" : Block.blocksList[blockIDs[1]].getUnlocalizedName2();
		ForgeEdit.inform("Replaced " + name1  + " with " + name2);
	}

	@Override
	public boolean isInvNameLocalized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStackValidForSlot(int i, ItemStack itemstack) {
		// TODO Auto-generated method stub
		return false;
	}
}
