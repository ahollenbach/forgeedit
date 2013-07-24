package maddjak.forgeedit.gui;

import maddjak.forgeedit.inventory.ContainerSave;
import maddjak.forgeedit.tileentity.TileEntitySave;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;


public class GuiSave extends GuiContainer{

    TileEntitySave tileEntity;
    
    public GuiSave(InventoryPlayer player_inventory, TileEntitySave tileEntity){
            super(new ContainerSave(tileEntity, player_inventory));
            this.tileEntity = tileEntity;
    }

	@Override
	public void initGui() {
		super.initGui();
									// id, x, y, width, height, text
		//controlList.add(new GuiButton(4, 250, 70, 30, 20, "Save"));
	}
	
	protected void actionPerformed(GuiButton guibutton) {
		switch (guibutton.id) {
		case 4:
			//write the item to file
			tileEntity.saveStructure();
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j) {
		fontRenderer.drawString("Recipe", 8, 6, 0x404040);
		fontRenderer.drawString(
				StatCollector.translateToLocal("container.inventory"), 8,
				ySize - 96 + 2, 0x404040);
	}
   
   
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.renderEngine.bindTexture("/mod/gui/trap.png");
		int x = (width - xSize) / 2;
		int y = (height - ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
	}
}