package com.mrnobody.morecommands.util;

import java.awt.Color;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.config.GuiCheckBox;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * The xray configuration gui class
 * 
 * @author MrNobody98
 *
 */
public class XrayConfGui extends GuiScreen {
	/**
	 * The edit gui for a single block
	 * 
	 * @author MrNobody98
	 */
	public class GuiEdit extends GuiScreen {
		/**
		 * A color slider
		 * 
		 * @author MrNobody98
		 */
		public class GuiSlider extends GuiButton {
			private float sliderValue;
			private final float sliderMultiplier;
			private boolean dragging;
			private final String label;

			public GuiSlider(int id, int x, int y, String label, float sliderStart, float sliderMultiplier) {
				super(id, x, y, 150, 20, label);
				this.label = label;
	        	this.sliderValue = sliderStart;
	        	this.sliderMultiplier = sliderMultiplier;
			}
			
			@Override
			public int getHoverState(boolean mouseOver) {
				return 0;
			}
			
			@Override
			protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
	            	if (this.dragging) {
	            		this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
	            		
	            		if (this.sliderValue < 0.0F)
	            			this.sliderValue = 0.0F;
	            		
	            		if (this.sliderValue > 1.0F)
	            			this.sliderValue = 1.0F;
	            	}
	            	
	            	this.displayString = this.label + ": " + (int) (this.sliderValue * this.sliderMultiplier);
	            	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	            	this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
	            	this.drawTexturedModalRect(this.xPosition + (int) (this.sliderValue * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
			}
			
			@Override
			public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
				if (super.mousePressed(mc, mouseX, mouseY)) {
					this.sliderValue = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
					
            		if (this.sliderValue < 0.0F)
            			this.sliderValue = 0.0F;
            		
            		if (this.sliderValue > 1.0F)
            			this.sliderValue = 1.0F;
            		
            		this.dragging = true;
					return true;
				} else {
					return false;
				}
			}
			
			@Override
			public void mouseReleased(int mouseX, int mouseY) {
				this.dragging = false;
			}
		}
		
		private GuiSlider redSlider;
		private GuiSlider greenSlider;
		private GuiSlider blueSlider;
		private GuiButton addButton;
		private GuiCheckBox enableBox;
		
		private boolean isEnabled;
		private final float startR, startG, startB;
		
		public GuiEdit(boolean isEnabled, float startR, float startG, float startB) {
			this.isEnabled = isEnabled;
			this.startR = startR;
			this.startG = startG;
			this.startB = startB;
		}
		
		@Override
		public void initGui() {
			this.redSlider   = new GuiSlider(1, this.width / 2 - 100, this.height / 2 - 40, "Red",   this.startR, 255);
			this.greenSlider = new GuiSlider(2, this.width / 2 - 100, this.height / 2 - 20, "Green", this.startG, 255);
			this.blueSlider  = new GuiSlider(3, this.width / 2 - 100, this.height / 2 -  0, "Blue",  this.startB, 255);
			this.enableBox   = new GuiCheckBox(4, this.width / 2 - 100, this.height / 2 - 60, "Enable Xray for this block", this.isEnabled);
			this.addButton   = new GuiButton(98, this.width - 100, this.height - 20, 100, 20, "Apply");
			
			this.redSlider.enabled   = false;
			this.greenSlider.enabled = false;
			this.blueSlider.enabled  = false;
			
			this.buttonList.add(new GuiButton(99, 2, this.height - 20, 100, 20, "Cancel"));
			this.buttonList.add(this.addButton);
			this.buttonList.add(this.redSlider);
			this.buttonList.add(this.greenSlider);
			this.buttonList.add(this.blueSlider);
			this.buttonList.add(this.enableBox);
		}
		
		@Override
		public void actionPerformed(GuiButton button) {
			switch (button.id) {
				case 4:
					this.redSlider.enabled   = this.enableBox.isChecked(); 
					this.greenSlider.enabled = this.enableBox.isChecked(); 
					this.blueSlider.enabled  = this.enableBox.isChecked();
					break;
				case 98:
					XrayConfGui.this.setBlockInfo(this.redSlider.sliderValue, this.greenSlider.sliderValue, 
													this.blueSlider.sliderValue, this.enableBox.isChecked());
					//omitting "break" intentionally to fall through to the next case to close the screen
				case 99:
					this.mc.thePlayer.closeScreen();
					break;
				default:
					break;
			}
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
			drawDefaultBackground();
			super.drawScreen(mouseX, mouseY, partialTicks);
			
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor3f(this.redSlider.sliderValue, this.greenSlider.sliderValue, this.blueSlider.sliderValue);
			GL11.glVertex2d(this.width / 2 + 50, this.height / 2 - 40);
			GL11.glVertex2d(this.width / 2 + 50, this.height / 2 + 20);
			GL11.glVertex2d(this.width / 2 + 112, this.height / 2 + 20);
			GL11.glVertex2d(this.width / 2 + 112, this.height / 2 - 40);
			GL11.glEnd();
			
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}
	}
	
	/**
	 * The block list gui
	 * 
	 * @author MrNobody98
	 *
	 */
	public class GuiList extends GuiSlot {
		private RenderItem itemRender = new RenderItem();
		
		public GuiList() {
			super(XrayConfGui.this.mc, XrayConfGui.this.width, XrayConfGui.this.height, 30, 
					XrayConfGui.this.height - 30, XrayConfGui.this.mc.fontRenderer.FONT_HEIGHT + 10);
		}
		
		@Override
		protected int getSize() {
			return XrayConfGui.this.blockList.length;
		}
		
		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
			XrayConfGui.this.elementSelected = slotIndex;
			boolean buttonsEnabled = XrayConfGui.this.elementSelected >= 0 && XrayConfGui.this.elementSelected < this.getSize();
			XrayConfGui.this.guiButtonConfigure.enabled = buttonsEnabled;
			
			if (isDoubleClick && buttonsEnabled)
				XrayConfGui.this.loadConfigGUI(XrayConfGui.this.xray.drawBlock(XrayConfGui.this.blockList[XrayConfGui.this.elementSelected]) ? 
						XrayConfGui.this.xray.getColor(XrayConfGui.this.blockList[XrayConfGui.this.elementSelected]) : null);
		}
		
		@Override
		protected boolean isSelected(int index) {
			return index == XrayConfGui.this.elementSelected;
		}
		
		@Override
		protected int getContentHeight() {
			return XrayConfGui.this.blockList.length * (XrayConfGui.this.mc.fontRenderer.FONT_HEIGHT + 10);
		}
		
		@Override
		protected void drawBackground() {
			XrayConfGui.this.drawDefaultBackground();
		}
		
		@Override
		protected void drawContainerBackground(Tessellator tessellator) {}
		
		@Override
		protected void drawSlot(int index, int left, int top, int slotHeight, Tessellator tesselator, int mouseX, int mouseY) {
			String blockName = XrayConfGui.this.blockList[index].getLocalizedName();
			Item blockItem = Item.getItemFromBlock(XrayConfGui.this.blockList[index]);
			
			if (blockItem != null) {
				this.itemRender.zLevel = 100F;
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				this.itemRender.renderItemAndEffectIntoGUI(XrayConfGui.this.mc.fontRenderer, XrayConfGui.this.mc.getTextureManager(), new ItemStack(blockItem), left, top);
				this.itemRender.renderItemOverlayIntoGUI(XrayConfGui.this.mc.fontRenderer, XrayConfGui.this.mc.getTextureManager(), new ItemStack(blockItem), left, top, blockItem.getUnlocalizedName());
		        GL11.glDisable(GL11.GL_LIGHTING);
		        this.itemRender.zLevel = 0.0F;
			}
			
			XrayConfGui.this.drawString(XrayConfGui.this.mc.fontRenderer, blockName + " - " + (XrayConfGui.this.xray.drawBlock(XrayConfGui.this.blockList[index]) ? "ENABLED" : "DISABLED"), left + 20, top + 3, 0xFFFFFF);
		}
	}
	
	private Minecraft mc;
	private Xray xray;
	private String heading = "MoreCommands: Xray Configuration";
	private int elementSelected;
	private GuiList guiList;
	private GuiButton guiButtonConfigure;
	private Block[] blockList;
	
	public XrayConfGui(Minecraft mc, Xray xray) {
		this.mc = mc;
		this.xray = xray;
		this.blockList = xray.getAllBlocks();
	}
	
	@Override
	public void initGui() {
		this.guiList = new GuiList();
		this.guiList.registerScrollButtons(4, 5);
		this.guiButtonConfigure = new GuiButton(1, this.width - 100, this.height - 20, 100, 20, "Configure");
		this.guiButtonConfigure.enabled = false;
		this.buttonList.add(this.guiButtonConfigure);
		this.buttonList.add(new GuiButton(0, 2, this.height - 20, 100, 20, "Cancel"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.enabled) {
			switch (button.id) {
				case 1:
					if (this.blockList[this.elementSelected] != null)
						this.loadConfigGUI(this.xray.drawBlock(this.blockList[this.elementSelected]) ? 
											this.xray.getColor(this.blockList[this.elementSelected]) : null);
					break;
				case 0:
					this.mc.thePlayer.closeScreen();
					break;
				default:
					break;
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.guiList.drawScreen(mouseX, mouseY, partialTicks);
		this.drawCenteredString(this.fontRendererObj, this.heading, this.width / 2, 10, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public void loadConfigGUI(Color startColor) {
		this.mc.displayGuiScreen(new GuiEdit(startColor != null, startColor != null ? startColor.getRed() / 255F : 1F,
				startColor != null ? startColor.getGreen() / 255F : 1F, startColor != null ? startColor.getBlue() / 255F : 1F));
	}
	
	public void setBlockInfo(float red, float green, float blue, boolean draw) {
		this.xray.changeBlockSettings(this.blockList[this.elementSelected], draw, new Color(red, green, blue));
	}
	
	public void displayGUI() {this.initGui(); this.mc.displayGuiScreen(this);}
}
