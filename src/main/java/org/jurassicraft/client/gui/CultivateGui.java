package org.jurassicraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.CultivateContainer;
import org.jurassicraft.server.tileentity.CultivatorTile;

@SideOnly(Side.CLIENT)
public class CultivateGui extends GuiContainer
{
    private CultivatorTile cultivator;

    private final InventoryPlayer playerInventory;

    private static final ResourceLocation gui = new ResourceLocation(JurassiCraft.MODID, "textures/gui/cultivator.png");
    private static final ResourceLocation nutrients = new ResourceLocation(JurassiCraft.MODID, "textures/gui/cultivator_nutrients.png");

    public CultivateGui(InventoryPlayer inventoryPlayer, CultivatorTile entity)
    {
        super(new CultivateContainer(inventoryPlayer, entity));
        this.playerInventory = inventoryPlayer;
        this.cultivator = entity;
        this.xSize = 352;
        this.ySize = 188;
    }

    @Override
    public void updateScreen()
    {
        if (this.cultivator.isCultivating())
        {
            this.mc.thePlayer.closeScreen();
            this.mc.displayGuiScreen(new CultivateProcessGui(cultivator));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j)
    {
        String s = this.cultivator.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2 - 45, 10, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

        this.fontRendererObj.drawString(StatCollector.translateToLocal("cultivator.proximates.name"), 200, 48, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("cultivator.minerals.name"), 200, 74, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("cultivator.vitamins.name"), 200, 100, 4210752);
        this.fontRendererObj.drawString(StatCollector.translateToLocal("cultivator.lipids.name"), 200, 126, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3)
    {
        mc.renderEngine.bindTexture(gui);
        drawTexturedModalRect(this.width / 2 - xSize / 2, this.height / 2 - ySize / 2, 0, 0, 176, 188);

        this.drawTexturedModalRect(guiLeft + 48, guiTop + 18, 0, 188, 42, 67 - getScaled(cultivator.getWaterLevel(), 3, 67));

        mc.renderEngine.bindTexture(nutrients);
        drawTexturedModalRect(this.width / 2 + 1, this.height / 2 - ySize / 2, 0, 0, 176, 166);

        int maxNutrients = cultivator.getMaxNutrients();

        int nutrientsX = guiLeft + 190;

        this.drawTexturedModalRect(nutrientsX, guiTop + 56, 0, 166, getScaled(cultivator.getProximates(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, guiTop + 82, 0, 175, getScaled(cultivator.getMinerals(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, guiTop + 108, 0, 184, getScaled(cultivator.getVitamins(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, guiTop + 134, 0, 193, getScaled(cultivator.getLipids(), maxNutrients, 150), 9);
    }

    private int getScaled(int value, int maxValue, int scale)
    {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }
}
