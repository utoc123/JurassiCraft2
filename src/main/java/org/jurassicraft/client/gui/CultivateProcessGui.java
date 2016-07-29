package org.jurassicraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.entity.CultivatorBlockEntity;
import org.jurassicraft.server.dinosaur.Dinosaur;

@SideOnly(Side.CLIENT)
public class CultivateProcessGui extends GuiScreen {
    private CultivatorBlockEntity cultivator;
    private int xSize;
    private int ySize;
    private int guiLeft;
    private int guiTop;

    private ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/gui/cultivator_progress.png");

    public CultivateProcessGui(CultivatorBlockEntity entity) {
        super();
        this.cultivator = entity;
        this.xSize = 176;
        this.ySize = 107;
    }

    @Override
    public void updateScreen() {
        if (!this.cultivator.isProcessing(0)) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char c, int key) {
        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.getKeyCode()) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.clear();

        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.buttonList.add(new GuiButton(0, this.guiLeft + (this.xSize - 100) / 2, this.guiTop + 70, 100, 20, I18n.format("container.close.name")));
    }

    @Override
    public void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.mc.thePlayer.closeScreen();
        }
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        this.mc.renderEngine.bindTexture(this.TEXTURE);

        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(this.guiLeft + 13, this.guiTop + 49, 0, 107, this.getScaled(this.cultivator.getField(0), this.cultivator.getField(1), 150), 9);

        Dinosaur dinosaur = this.cultivator.getDinosaur();

        String name;

        if (dinosaur != null) {
            name = dinosaur.getName();
        } else {
            name = "Unknown";
        }

        String cultivatingLang = I18n.format("container.cultivator.cultivating");
        String progressLang = I18n.format("container.cultivator.progress");

        String progress = progressLang + ": " + this.getScaled(this.cultivator.getField(0), this.cultivator.getField(1), 100) + "%";
        String cultivating = cultivatingLang + ": " + name;

        this.fontRendererObj.drawString(cultivating, this.guiLeft + (this.xSize - this.fontRendererObj.getStringWidth(cultivating)) / 2, this.guiTop + 10, 4210752);
        this.fontRendererObj.drawString(progress, this.guiLeft + (this.xSize - this.fontRendererObj.getStringWidth(progress)) / 2, this.guiTop + 40, 4210752);

        super.drawScreen(x, y, f);
    }

    private int getScaled(int value, int maxValue, int scale) {
        return maxValue != 0 && value != 0 ? value * scale / maxValue : 0;
    }
}
