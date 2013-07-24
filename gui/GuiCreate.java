package maddjak.forgeedit.gui;

import maddjak.forgeedit.block.CreationBlock;
import maddjak.forgeedit.inventory.ContainerCreate;
import maddjak.forgeedit.tileentity.TileEntityCreate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;


public class GuiCreate extends GuiContainer{

    TileEntityCreate tileEntity;
    
    public GuiCreate(InventoryPlayer player_inventory, TileEntityCreate tileEntity){
            super(new ContainerCreate(tileEntity, player_inventory));
            this.tileEntity = tileEntity;
    }

	@Override
	public void initGui() {
		super.initGui();
								// id, x, y, width, height, text
		//controlList.add(new GuiButton(0, 194, 70, 34, 20, "Build"));
										// id, x, y
		//controlList.add(new GuiButtonPaste(1, 254, 70));
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		switch (guibutton.id) {
		case 0:
			tileEntity.loadStructure();
			break;
		case 1:
			tileEntity.pasteStructure();
			break;
		default:
			break;
	}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		fontRenderer.drawString("Build", 8, 6, 0x404040);
		fontRenderer.drawString("Load", 117, 6, 0x404040);
		fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 0x404040);
	}
   
   
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mod/gui/creation.png");
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}