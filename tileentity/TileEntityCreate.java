package maddjak.forgeedit.tileentity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


import maddjak.forgeedit.ForgeEdit;
import maddjak.forgeedit.block.CreationBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityCreate extends TileEntity implements IInventory {

	private ItemStack[] inventory;
	private static World world;
	
	private static int[][][] curStructure;

	public TileEntityCreate() {
		this.inventory = new ItemStack[9];
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
		return "CreateTileEntity";
	}
	
	public void loadStructure() {
		int[] recipe = getRecipe();
		//check if matches any known, if so, set filename to appropriate file
		String filename = ForgeEdit.isValidRecipe(recipe);
		
		//String filename = "mods/ForgeEdit_structures/struct002.mcs";
		
		if(filename != null) {
			//load the structure from file to curStructure
			int[][][] structure = parseStructureFromFile(filename);
			
			//remove one of each item from recipe
			removeRecipe();
			
			if(curStructure != null) {
				buildStructure(false);
			}
		} else {
			ForgeEdit.inform("Not a valid recipe");
		}
	}
	
	private int[][][] parseStructureFromFile(String filename) {
		int[] dimensions = ForgeEdit.getStructureInfo(2, filename);
		int[] flatStruct = ForgeEdit.getStructureInfo(3, filename);
		int index = 0;
		
		if(dimensions != null && flatStruct != null) {
			curStructure = new int[dimensions[0]][dimensions[1]][dimensions[2]];
			for(int w = 0; w < dimensions[0]; w++) {
				for(int d = 0; d < dimensions[2]; d++) {
					for(int h = 0; h < dimensions[1]; h++) {
						curStructure[w][h][d] = flatStruct[index];
						index++;
					}
				}
			}
		}		
		return null;
	}

	public void pasteStructure() {
		//load the structure from lastStruct to curStructure
		curStructure = CreationBlock.getLastStruct();
		if(curStructure != null) {
			buildStructure(false);
		} else {
			ForgeEdit.inform("Nothing to paste");
		}
	}
	
	/**
	 * Builds the structure loaded into curStructure
	 * 
	 * @param preserveAir A bool - if true, preserve the air tiles, else, don't build air tiles
	 */
	public void buildStructure(Boolean preserveAir) {
		int width =  curStructure.length;
		int height = curStructure[0].length;
		int depth =  curStructure[0][0].length;
		
		for(int w = 1; w < width; w++) {
			for(int d = 1; d < depth; d++) {
				for(int h = 1; h < height; h++) {
					if(preserveAir || curStructure[w][h][d] != 0) {
						world.setBlock(xCoord + w, yCoord + h, zCoord + d, curStructure[w][h][d]);
					}
				}
			}
		}
		
		//tell the player
		ForgeEdit.inform("Structure built");
	}
	
	private int[] getRecipe() {
		int[] recipe = new int[getSizeInventory()];
		for (int i = 0; i < getSizeInventory(); i++) {
			ItemStack itemStack = getStackInSlot(i);
			if (itemStack != null) {
				recipe[i] = getStackInSlot(i).getItem().itemID;
			}
		}
		return recipe;
	}
	
	private void removeRecipe() {
		for (int i = 0; i < getSizeInventory(); i++) {
			decrStackSize(i,1);
		}
	}

	public static void setStructure(int[][][] structure) {
		curStructure = structure;
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
