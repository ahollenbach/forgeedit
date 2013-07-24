package maddjak.forgeedit.tileentity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


import maddjak.forgeedit.ForgeEdit;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntitySave extends TileEntity implements IInventory {
	private ItemStack[] inventory;
	private static World world;
	
	private static int[][][] curStructure;

	public TileEntitySave() {
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
		return "SaveTileEntity";
	}
	
	public void saveStructure() {
		String filename = getFileName();
		int[] recipe = getRecipe();
		
		//save to file
		writeToFile(filename, recipe);
		
		//tell the player
		ForgeEdit.inform("Structure saved as " + filename);
	}

	private void writeToFile(String filename, int[] recipe) {
		try {
			// Create file
			FileWriter fstream = new FileWriter(ForgeEdit.pathToStructures + "/" + filename);
			BufferedWriter out = new BufferedWriter(fstream);
			
			//save file and version information
			out.write("<filename>" + filename + "</filename>\n");
			out.write("<version>" + ForgeEdit.mcsFormatVersion + "</version>\n");
			out.write('\n');
			out.write("<recipe>\n\t");
			for (int i = 0; i < 9; i++) {
				if(i==8) {
					out.write(recipe[i] + "\n");
				} else {
					out.write(recipe[i] + ",");
				}
			}
			out.write("</recipe>\n");
			out.write('\n');
			out.write("<dimensions>\n");
				out.write("\t<width>"  + curStructure.length 	   + "</width>\n");
				out.write("\t<height>" + curStructure[0].length    + "</height>\n");
				out.write("\t<depth>"  + curStructure[0][0].length + "</depth>\n");
			out.write("</dimensions>\n");
			out.write('\n');
			out.write("<structure>\n\t");
			for(int w = 0; w < curStructure.length; w++) {
				for(int d = 0; d < curStructure[0][0].length; d++) {
					for(int h = 0; h < curStructure[0].length; h++) {
						out.write(curStructure[w][h][d] + ",");
					}
				}
			}
			out.write("</structure>\n");
			
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		ForgeEdit.addStructure(recipe, filename);
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
	
	private String getFileName() {
		String extension = ".mcs";
		String filePrefix = "struct";
		int fileNumber = 1;
		int curFileNum;

		String files;
		File folder = new File(ForgeEdit.pathToStructures);
		if(!folder.exists()) {
			folder.mkdir();
		}
		ForgeEdit.inform(folder.getAbsolutePath());
		File[] listOfFiles = folder.listFiles();
		if(listOfFiles != null) {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					String filename = listOfFiles[i].getName();
					int extensionIndex = filename.length() - extension.length();
					if(filename.lastIndexOf(extension) == extensionIndex) {
						try {
							curFileNum = Integer.parseInt(filename.substring(filePrefix.length(), extensionIndex));
							if(curFileNum != fileNumber) {
								break;
							}
						} catch(NumberFormatException ex) {
							
						}
						fileNumber++;
					}
				}
			}
		}
		String filename = String.format(filePrefix + "%03d" + extension, fileNumber);
		File outputFile = new File(ForgeEdit.pathToStructures + filename);
		ForgeEdit.inform(outputFile.getAbsolutePath());
		if(!outputFile.exists()) {
			try {
				outputFile.createNewFile();
			} catch (IOException e) {
				System.err.println("Error creating file");
				e.printStackTrace();
			}
		}
		return filename;
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
