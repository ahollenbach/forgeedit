package maddjak.forgeedit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;


import maddjak.forgeedit.block.CopyBlock;
import maddjak.forgeedit.block.CreationBlock;
import maddjak.forgeedit.block.CutBlock;
import maddjak.forgeedit.block.FillBlock;
import maddjak.forgeedit.block.MarkerBlock;
import maddjak.forgeedit.block.ReplaceBlock;
import maddjak.forgeedit.block.SaveBlock;
import maddjak.forgeedit.client.ClientPacketHandler;
import maddjak.forgeedit.gui.GuiHandler;
import maddjak.forgeedit.tileentity.TileEntityCreate;
import maddjak.forgeedit.tileentity.TileEntityFill;
import maddjak.forgeedit.tileentity.TileEntityReplace;
import maddjak.forgeedit.tileentity.TileEntitySave;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = ForgeEdit.modid, name = "ForgeEdit", version = "alpha0.1")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, clientPacketHandlerSpec = 
@SidedPacketHandler(channels = {"ForgeEdit" }, packetHandler = ClientPacketHandler.class),
serverPacketHandlerSpec =@SidedPacketHandler(channels = {"ForgeEdit" }, packetHandler = ServerPacketHandler.class))
public class ForgeEdit {
    // The instance of your mod that Forge uses.
    @Instance("ForgeEdit")
    public static ForgeEdit instance = new ForgeEdit();
    public final static String mcsFormatVersion = "1.0";
    public static final String modid = "ForgeEdit";
    
    private GuiHandler guiHandler = new GuiHandler();
    
    /**
     * Keys: stringified int[]
     * Values: filename of structure
     */
    private static HashMap<String,String> structureRecipes = new HashMap<String,String>();
    
    public static String pathToStructures;
    
    // Added blocks and items
	public final static Block cutBlock = new CutBlock(2500, 1);
	public final static Block copyBlock = new CopyBlock(2501, 2);
	public final static Block creationBlock = new CreationBlock(2502, 3);  //paste/build block
	
	public final static Block fillBlock = new FillBlock(2503, 4);
	public final static Block replaceBlock = new ReplaceBlock(2504, 5);
	public final static Block saveBlock = new SaveBlock(2505, 6, "saveBlock");
	public final static Block markerBlock = new MarkerBlock(2506, 7, "markerBlock");
    
    // Says where the client and server 'proxy' code is loaded.
    @SidedProxy(clientSide="maddjak.forgeedit.client.ClientProxy", serverSide="maddjak.forgeedit.CommonProxy")
    public static CommonProxy proxy;
    
    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
    	setPathToStructures();
    }
    
    private void setPathToStructures() {
    	String userDir = System.getProperty("user.home");
    	String structureLocation = "mods/ForgeEdit_structures/";
    	String osName = System.getProperty("os.name");
    	String mcDir;
    	///new File(proxy.getMinecraftDir(), "bboutline.properties")
    	if(osName.contains("Windows")) {
    		mcDir = "AppData/Roaming/.minecraft";
    	} else if(osName.contains("Mac")) {
    		System.err.println("The mod creator was lazy and never figured out how to handle this.");
    		mcDir = "library/application support/minecraft";
    	} else {
    		mcDir = ".minecraft";
    	}
    	
		pathToStructures = userDir + "/" + mcDir + "/" + structureLocation;
	}

	@Init
    public void load(FMLInitializationEvent event) {
    	createStructureMap();
    	
    	//Added recipe for creation block
    	ItemStack dirtStack = new ItemStack(Block.dirt);
    	ItemStack cobbleStack = new ItemStack(Block.cobblestone);

    	GameRegistry.addRecipe(new ItemStack(ForgeEdit.cutBlock), "x x", " x ", "x x",
    	        'x', dirtStack);
    	GameRegistry.addRecipe(new ItemStack(ForgeEdit.copyBlock), "xx ", "xxx", " xx",
    	        'x', dirtStack);
    	GameRegistry.addRecipe(new ItemStack(ForgeEdit.creationBlock), "xxx", "xxx", "xxx",
    	        'x', dirtStack);
    	
    	GameRegistry.addRecipe(new ItemStack(ForgeEdit.fillBlock), "xxx", "   ", "xxx",
    	        'x', dirtStack);
    	GameRegistry.addRecipe(new ItemStack(ForgeEdit.replaceBlock), "xxx", "yyy", "xxx",
    	        'x', dirtStack, 'y', cobbleStack);
    	GameRegistry.addRecipe(new ItemStack(ForgeEdit.saveBlock), "   ", " xx", " xx",
    	        'x', dirtStack);
    	GameRegistry.addRecipe(new ItemStack(ForgeEdit.markerBlock), "   ", " x ", "   ",
    	        'x', dirtStack);
    	
    	//Register added items and blocks
    	Block[] customBlocks = { cutBlock, copyBlock, creationBlock, fillBlock, replaceBlock, saveBlock, markerBlock };
    	
    	for(int i=0; i<customBlocks.length; i++) {
    		String name = formatBlockName(customBlocks[i],1);
    		LanguageRegistry.addName(customBlocks[i], name);
    		
    		name = formatBlockName(customBlocks[i],2);
    		GameRegistry.registerBlock(customBlocks[i], name);
    	}
        
        //Register TileEntities
        GameRegistry.registerTileEntity(TileEntitySave.class, "tileEntitySave");
        GameRegistry.registerTileEntity(TileEntityFill.class, "tileEntityFill");
        GameRegistry.registerTileEntity(TileEntityReplace.class, "tileEntityReplace");
        GameRegistry.registerTileEntity(TileEntityCreate.class, "tileEntityCreate");
    	
        proxy.registerRenderers();
        NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
    }
    
    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
    
    /**
     * Gets the structure information at the specified line.
     * Line 1 = recipe 
     * Line 2 = dimensions 
     * Line 3 = structure 
     * 
     * @param line the line of the file to read from
     * @param filename the filename only (do NOT include pathToStructures)
     * @return an array of ints that represent the values on that line or null if errored
     */
    public static int[] getStructureInfo(int line, String filename) {
    	int index = 0;
		int width;
		int height;
		int depth;
    	BufferedReader br = null;
    	String curLine = "";
    	int[] values = null;
    	
    	try {
    		br = new BufferedReader(new FileReader(pathToStructures + filename));
    		for(int i=1;i<=line;i++) {
    			curLine = br.readLine();
    		}
    		if(curLine != null) {
	    		String[] lineSplit = curLine.split(",");
	    		values = new int[lineSplit.length];
	    		for(int i=0;i<lineSplit.length;i++) {
	    			if(lineSplit[i] != null && !("".equals(lineSplit[i]))) { //if not "" or null
	    				values[i] = Integer.parseInt(lineSplit[i]);
	    			}
	    		}
    		}
    	} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
    	return values;
    }
    
    private void createStructureMap() {
    	String extension = ".mcs";
    	File folder = new File(pathToStructures);
    	if(!folder.exists()) {
			return;
		}
    	File[] listOfFiles = folder.listFiles();
    	for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String filename = listOfFiles[i].getName();
				int extensionIndex = filename.length() - extension.length();
				if(filename.lastIndexOf(extension) == extensionIndex) {
					//parse file
					int[] recipe = getStructureInfo(1, filename); //get the recipe
					structureRecipes.put(Arrays.toString(recipe), filename);
				}
			}
		}
    }
    
    /**
     * Adds a structure to the list of current structures 
     * @param recipe 
     * @param filename
     */
    public static void addStructure(int[] recipe, String filename) {
    	structureRecipes.put(Arrays.toString(recipe), filename);
    }
    
    /**
     * Finds if the recipe is among the structures.
     * 
     * @param recipe
     * @return the filename of the structure or null if no match
     */
    public static String isValidRecipe(int[] recipe) {
    	return structureRecipes.get(Arrays.toString(recipe));
    }
    
    /**
     * Prints out to the Minecraft console using a standard ForgeEdit design.
     * 
     * @param message the message to print to console
     */
    public static void inform(String message) {
    	String prefix = "[ForgeEdit] ";
    	Minecraft.getMinecraft().thePlayer.addChatMessage(prefix + message);
    }
    
    /**
     * Formats the block name given the specified format, because the original one sucks.
     * 
     * @param block The block to fetch the name of
     * @param formatType How to format the the string, examples:
     * <pre>
     * 1 - IN:tile.wood		OUT:Wood Block
     * 2 - IN:tile.wood		OUT:woodBlock
     * @return
     */
    public static String formatBlockName(Block block, int formatType) {
    	// TODO: used to be block.getBlockName()
    	String name = block.getUnlocalizedName().substring(5);
    	switch(formatType) {
    	case 1:
    		return (Character.toUpperCase(name.charAt(0)) + name.substring(1,name.lastIndexOf("Block"))) + " Block";
    	case 2:
    		return name;
    	default:
    		return "errorBlock";
    	}
    }
}