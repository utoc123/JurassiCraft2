package org.jurassicraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.container.FeederContainer;
import org.jurassicraft.server.tile.FeederTile;

@SideOnly(Side.CLIENT)
public class FeederGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("jurassicraft:textures/gui/feeder.png");
    private final InventoryPlayer playerInventory;
    private IInventory inventory;

    public FeederGui(InventoryPlayer playerInventory, IInventory inventory) {
        super(new FeederContainer(playerInventory, (FeederTile) inventory));
        this.playerInventory = playerInventory;
        this.inventory = inventory;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = this.inventory.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 3, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
    }
}
