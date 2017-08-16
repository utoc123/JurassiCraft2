package org.jurassicraft.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.entity.CultivatorBlockEntity;
import org.jurassicraft.server.container.CultivateContainer;

@SideOnly(Side.CLIENT)
public class CultivateGui extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/gui/cultivator.png");
    private static final ResourceLocation NUTRIENTS_TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/gui/cultivator_nutrients.png");
    private final InventoryPlayer playerInventory;
    private CultivatorBlockEntity cultivator;

    public CultivateGui(InventoryPlayer inventoryPlayer, CultivatorBlockEntity entity) {
        super(new CultivateContainer(inventoryPlayer, entity));
        this.playerInventory = inventoryPlayer;
        this.cultivator = entity;
        this.xSize = 352;
        this.ySize = 188;
    }

    @Override
    public void updateScreen() {
        if (this.cultivator.isProcessing(0)) {
            this.mc.player.closeScreen();
            this.mc.displayGuiScreen(new CultivateProcessGui(this.playerInventory, this.cultivator));
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int i, int j) {
        String name = this.cultivator.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(name, this.xSize / 2 - this.fontRendererObj.getStringWidth(name) / 2 - 45, 10, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);

        this.fontRendererObj.drawString(I18n.format("cultivator.proximates.name"), 200, 48, 4210752);
        this.fontRendererObj.drawString(I18n.format("cultivator.minerals.name"), 200, 74, 4210752);
        this.fontRendererObj.drawString(I18n.format("cultivator.vitamins.name"), 200, 100, 4210752);
        this.fontRendererObj.drawString(I18n.format("cultivator.lipids.name"), 200, 126, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float var1, int var2, int var3) {
        this.mc.renderEngine.bindTexture(TEXTURE);
        this.drawTexturedModalRect(this.width / 2 - this.xSize / 2, this.height / 2 - this.ySize / 2, 0, 0, 176, 188);

        this.drawTexturedModalRect(this.guiLeft + 48, this.guiTop + 18, 0, 188, 42, 67 - this.getScaled(this.cultivator.getWaterLevel(), 3, 67));

        this.mc.renderEngine.bindTexture(NUTRIENTS_TEXTURE);
        this.drawTexturedModalRect(this.width / 2 + 1, this.height / 2 - this.ySize / 2, 0, 0, 176, 166);

        int maxNutrients = this.cultivator.getMaxNutrients();

        int nutrientsX = this.guiLeft + 190;

        this.drawTexturedModalRect(nutrientsX, this.guiTop + 56, 0, 166, this.getScaled(this.cultivator.getProximates(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, this.guiTop + 82, 0, 175, this.getScaled(this.cultivator.getMinerals(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, this.guiTop + 108, 0, 184, this.getScaled(this.cultivator.getVitamins(), maxNutrients, 150), 9);
        this.drawTexturedModalRect(nutrientsX, this.guiTop + 134, 0, 193, this.getScaled(this.cultivator.getLipids(), maxNutrients, 150), 9);
    }

    private int getScaled(int value, int maxValue, int scale) {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }
}
