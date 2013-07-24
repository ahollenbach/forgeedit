package maddjak.forgeedit.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiButtonPaste extends GuiButton {

	public GuiButtonPaste(int id, int xPos, int yPos) {
		super(id, xPos, yPos, 20, 20, "");
	}
	
	/**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft minecraft, int mousePosX, int mousePosY)
    {
        if (this.drawButton)
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture("/maddjak/forgeedit/creation.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean var4 = mousePosX >= this.xPosition && mousePosY >= this.yPosition && mousePosX < this.xPosition + this.width && mousePosY < this.yPosition + this.height;
            int imgIndex = 166;

            if (var4)
            {
            	imgIndex += this.height;
            }

            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, imgIndex, this.width, this.height);
        }
    }

}
