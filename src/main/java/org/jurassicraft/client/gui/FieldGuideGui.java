package org.jurassicraft.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.DinosaurStatus;
import org.jurassicraft.server.lang.LangHelper;
import org.lwjgl.opengl.GL11;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class FieldGuideGui extends GuiScreen
{
    private int sizeX = 256;
    private int sizeY = 192;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/field_guide/background.png");

    private DinosaurEntity entity;

    private static final Map<DinosaurStatus, ResourceLocation> STATUS_TEXTURES = new HashMap<>();

    static
    {
        for (DinosaurStatus status : DinosaurStatus.values())
        {
            STATUS_TEXTURES.put(status, new ResourceLocation(JurassiCraft.MODID, "textures/field_guide/status/" + status.name().toLowerCase() + ".png"));
        }
    }

    public FieldGuideGui(DinosaurEntity entity)
    {
        this.entity = entity;
    }

    @Override
    public void initGui()
    {
        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaleFactor = scaledResolution.getScaleFactor();

        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        int x = (this.width - sizeX) / 2;
        int y = (this.height - sizeY) / 2;

        this.zLevel = -1000;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
        this.drawTexturedModalRect(x, y, 0, 0, 256, 179);

        Dinosaur dinosaur = entity.getDinosaur();

        drawScaledString(new LangHelper("entity.jurassicraft." + dinosaur.getName().toLowerCase() + ".name").build().toUpperCase(), x + 15, y + 10, 1.3F, 0);
        drawScaledString(entity.getGrowthStage().name() + " // " + new LangHelper("gender." + (entity.isMale() ? "male" : "female") + ".name").build().toUpperCase(), x + 16, y + 24, 1.0F, 0);

        int statisticsX = x + (sizeX / 2) + 15;

        DecimalFormat decimalFormat = new DecimalFormat("#.#");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

        drawScaledString("Dinosaur Statistics:", statisticsX, y + 10, 1.0F, 0);
        int statisticTextX = x + (sizeX / 2 + sizeX / 4);
        drawCenteredScaledString("Health:", statisticTextX, y + 35, 1.0F, 0);
        drawCenteredScaledString("Hunger:", statisticTextX, y + 65, 1.0F, 0);
        drawCenteredScaledString("Thirst:", statisticTextX, y + 95, 1.0F, 0);
        drawCenteredScaledString("Age:", statisticTextX, y + 125, 1.0F, 0);

        this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        drawBar(statisticsX, y + 45, entity.isCarcass() ? 0 : entity.getHealth(), entity.getMaxHealth(), 0xFF0000);
        drawBar(statisticsX, y + 75, entity.getMetabolism().getEnergy(), entity.getMetabolism().getMaxEnergy(), 0x94745A);
        drawBar(statisticsX, y + 105, entity.getMetabolism().getWater(), entity.getMetabolism().getMaxWater(), 0x0000FF);
        drawBar(statisticsX, y + 135, entity.getDinosaurAge(), dinosaur.getMaximumAge(), 0x00FF00);

        drawCenteredScaledString(entity.getDaysExisted() + " days old", statisticTextX, y + 155, 1.0F, 0);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((x + 15) * scaleFactor, (height - y - 140) * scaleFactor, 100 * scaleFactor, 100 * scaleFactor);
        this.drawEntity(x + 65, y + 110, 45.0F / entity.height, entity);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        int statusX = 0;
        int statusY = 0;

        List<DinosaurStatus> activeStatuses = DinosaurStatus.getActiveStatuses(entity);

        for (DinosaurStatus status : activeStatuses)
        {
            mc.getTextureManager().bindTexture(STATUS_TEXTURES.get(status));

            int size = 16;

            drawFullTexturedRect(statusX + x + 35, statusY + y + (sizeY - 40), size, size);

            statusX += 18;

            if (statusX > sizeX / 2 - 60)
            {
                statusX = 0;
                statusY -= 18;
            }
        }

        statusX = 0;
        statusY = 0;

        for (DinosaurStatus status : activeStatuses)
        {
            int size = 16;

            int renderX = statusX + x + 35;
            int renderY = statusY + y + (sizeY - 40);

            if (mouseX >= renderX && mouseY >= renderY && mouseX <= renderX + size && mouseY <= renderY + size)
            {
                this.drawCreativeTabHoveringText(new LangHelper("status." + status.name().toLowerCase() + ".name").build(), mouseX, mouseY);
            }

            statusX += 18;

            if (statusX > sizeX / 2 - 60)
            {
                statusX = 0;
                statusY -= 18;
            }
        }

        GlStateManager.disableLighting();
    }

    private void drawBar(int x, int y, float value, float max, int color)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(x, y, 0, 179, 98, 8);
        GlStateManager.color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F);
        drawTexturedModalRect(x, y, 0, 187, (int) ((value / max) * 98), 8);
    }

    private void drawScaledString(String text, float x, float y, float scale, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        fontRendererObj.drawString(text, x / scale, y / scale, color, false);
        GlStateManager.popMatrix();
    }

    private void drawCenteredScaledString(String text, float x, float y, float scale, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        fontRendererObj.drawString(text, (x - fontRendererObj.getStringWidth(text) / 2) / scale, y / scale, color, false);
        GlStateManager.popMatrix();
    }

    private void drawFullTexturedRect(int x, int y, int width, int height)
    {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, this.zLevel).tex(0.0F, 1.0F).endVertex();
        buffer.pos(x + width, y + height, this.zLevel).tex(1.0F, 1.0F).endVertex();
        buffer.pos(x + width, y, this.zLevel).tex(1.0F, 0.0F).endVertex();
        buffer.pos(x, y, this.zLevel).tex(0.0F, 0.0F).endVertex();
        tessellator.draw();
    }

    @Override
    public void actionPerformed(GuiButton button)
    {
    }

    public void drawEntity(int posX, int posY, float scale, EntityLivingBase entity)
    {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) posX, (float) posY, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(15.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(0.0F, 0.0F, 0.0F);
        RenderManager renderManager = mc.getRenderManager();
        renderManager.setPlayerViewY(180.0F);
        renderManager.setRenderShadow(false);
        renderManager.doRenderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        renderManager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
}
