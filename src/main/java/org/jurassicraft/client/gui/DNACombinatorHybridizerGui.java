package org.jurassicraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.DNACombinatorHybridizerContainer;
import org.jurassicraft.server.message.SwitchHybridizerCombinatorMode;
import org.jurassicraft.server.tileentity.DNACombinatorHybridizerTile;

@SideOnly(Side.CLIENT)
public class DNACombinatorHybridizerGui extends GuiContainer
{
    private static final ResourceLocation hybridizerTexture = new ResourceLocation("jurassicraft:textures/gui/dna_hybridizer.png");
    private static final ResourceLocation combinatorTexture = new ResourceLocation("jurassicraft:textures/gui/dna_combinator.png");

    private final InventoryPlayer playerInventory;
    private DNACombinatorHybridizerTile inventory;
    private DNACombinatorHybridizerContainer container;

    public DNACombinatorHybridizerGui(InventoryPlayer playerInv, DNACombinatorHybridizerTile inventory)
    {
        super(new DNACombinatorHybridizerContainer(playerInv, inventory));
        this.playerInventory = playerInv;
        this.inventory = inventory;
        this.container = (DNACombinatorHybridizerContainer) inventorySlots;
    }

    @Override
    public void initGui()
    {
        super.initGui();

        int guiX = (this.width - this.xSize) / 2;
        int guiY = (this.height - this.ySize) / 2;

        this.buttonList.add(new GuiButton(0, guiX + 128, guiY + 64, 30, 10, "<->"));
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
        if (button.id == 0)
        {
            boolean mode = !inventory.getMode();
            container.updateSlots(mode);
            inventory.setMode(mode);
            JurassiCraft.NETWORK_WRAPPER.sendToServer(new SwitchHybridizerCombinatorMode(inventory.getPos(), mode));
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String s = this.inventory.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 4, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        boolean isHybridizer = inventory.getMode();
        this.mc.getTextureManager().bindTexture(isHybridizer ? hybridizerTexture : combinatorTexture);

        int centerX = (this.width - this.xSize) / 2;
        int centerY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(centerX, centerY, 0, 0, this.xSize, this.ySize);

        int progress = this.getProgress(isHybridizer ? 27 : 24);

        if (isHybridizer)
        {
            this.drawTexturedModalRect(centerX + 86, centerY + 25, 176, 0, 4, progress);
        }
        else
        {
            this.drawTexturedModalRect(centerX + 93, centerY + 30, 176, 0, 8, progress);
        }
    }

    private int getProgress(int scale)
    {
        int j = this.inventory.getField(0);
        int k = this.inventory.getField(1);
        return k != 0 && j != 0 ? j * scale / k : 0;
    }
}
