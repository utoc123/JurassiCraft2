package org.jurassicraft.client.gui;

import org.jurassicraft.server.container.SkeletonAssemblyContainer;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SkeletonAssemblyGui extends GuiContainer {
    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = new ResourceLocation(
            "jurassicraft:textures/gui/skeleton_assembler.png");
    public SkeletonAssemblyContainer cont;

    public SkeletonAssemblyGui(SkeletonAssemblyContainer cont) {
        super(cont);
        this.cont = cont;
        this.xSize = 176;
        this.ySize = 194;

    }

    public static SkeletonAssemblyContainer createContainer(InventoryPlayer playerInv, World worldIn,
            BlockPos blockPosition) {
        return new SkeletonAssemblyContainer(playerInv, worldIn, blockPosition);
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.FONT_HEIGHT = 6;
        this.fontRendererObj.drawString(this.cont.error, 7, 99, 4210752);
        this.fontRendererObj.FONT_HEIGHT = 9;
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(CRAFTING_TABLE_GUI_TEXTURES);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}