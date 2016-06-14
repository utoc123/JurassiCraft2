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
    private int sizeX;
    private int sizeY;
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/field_guide/background.png");

    private final ResourceLocation fileTexture;

    private DinosaurEntity entity;
    private boolean file;

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
        this.fileTexture = new ResourceLocation(JurassiCraft.MODID, "textures/field_guide/" + entity.getDinosaur().getName().toLowerCase() + ".png");
    }

    @Override
    public void initGui()
    {
        super.initGui();

        ScaledResolution resolution = new ScaledResolution(mc);
        int scaleFactor = resolution.getScaleFactor();

        sizeX = 153 * scaleFactor;
        sizeY = 108 * scaleFactor;

        int x = (this.width - sizeX) / 2;
        int y = (this.height - sizeY) / 2;

        buttonList.add(new GuiButton(0, x - 25, y + sizeY - 20, 20, 20, "<"));
        buttonList.add(new GuiButton(1, x + sizeX + 5, y + sizeY - 20, 20, 20, ">"));

        updateArrowStates();
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
        this.drawFullTexturedRect(x, y, sizeX, sizeY);

        if (file)
        {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.mc.getTextureManager().bindTexture(fileTexture);
            this.drawFullTexturedRect(x, y, sizeX, sizeY);
        }
        else
        {
            Dinosaur dinosaur = entity.getDinosaur();

            drawScaledString(new LangHelper("entity.jurassicraft." + dinosaur.getName().toLowerCase() + ".name").build().toUpperCase(), x + 15, y + 10, 2.0F, 0);
            drawScaledString(entity.getGrowthStage().name() + " // " + new LangHelper("gender." + (entity.isMale() ? "male" : "female") + ".name").build().toUpperCase(), x + 15, y + 28, 1.0F, 0);

            int statisticsX = scaledResolution.getScaledWidth() / 2 + 8;

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

            drawScaledString("Dinosaur Statistics:", statisticsX, y + 10, 1.28F, 0);
            drawScaledString("Health: " + (entity.isCarcass() ? 0 : decimalFormat.format(entity.getHealth())) + "/" + decimalFormat.format(entity.getMaxHealth()), statisticsX, y + 25, 1.0F, 0);
            drawScaledString("Age: " + entity.getDaysExisted() + " days", statisticsX, y + 35, 1.0F, 0);
            drawScaledString("Hunger: " + entity.getMetabolism().getEnergy() + "/" + entity.getMetabolism().getMaxEnergy(), statisticsX, y + 45, 1.0F, 0);
            drawScaledString("Thirst: " + entity.getMetabolism().getWater() + "/" + entity.getMetabolism().getMaxWater(), statisticsX, y + 55, 1.0F, 0);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor((int) ((scaledResolution.getScaledWidth() / 6.5F) * scaledResolution.getScaleFactor()), (int) ((scaledResolution.getScaledHeight() / 2.9F) * scaledResolution.getScaleFactor()), (int) ((scaledResolution.getScaledWidth() / 2.9F) * scaledResolution.getScaleFactor()), (int) ((scaledResolution.getScaledHeight() / 1.9F) * scaledResolution.getScaleFactor()));
            this.drawEntity((int) (scaledResolution.getScaledWidth() / 3.1F), scaledResolution.getScaledHeight() / 2, (16.0F / dinosaur.getAdultSizeY()) * scaledResolution.getScaleFactor(), entity);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int statusX = 0;
            int statusY = 0;

            List<DinosaurStatus> activeStatuses = DinosaurStatus.getActiveStatuses(entity);

            for (DinosaurStatus status : activeStatuses)
            {
                int renderX = (int) (statusX + (sizeX / 1.45F) + 10);
                int renderY = (int) (statusY + (sizeY / 1.06F));

                mc.getTextureManager().bindTexture(STATUS_TEXTURES.get(status));

                int size = scaleFactor * 6;

                drawFullTexturedRect(renderX, renderY, size, size);

                statusX += 8 * scaleFactor;

                if (statusX > scaleFactor * 40)
                {
                    statusX = 0;
                    statusY -= scaleFactor * 8;
                }
            }

            statusX = 0;
            statusY = 0;

            for (DinosaurStatus status : activeStatuses)
            {
                int renderX = (int) (statusX + (sizeX / 1.45F) + 10);
                int renderY = (int) (statusY + (sizeY / 1.06F));

                int size = scaleFactor * 6;

                if (mouseX >= renderX && mouseY >= renderY && mouseX <= renderX + size && mouseY <= renderY + size)
                {
                    this.drawCreativeTabHoveringText(new LangHelper("status." + status.name().toLowerCase() + ".name").build(), mouseX, mouseY);
                }

                statusX += 8 * scaleFactor;

                if (statusX > scaleFactor * 40)
                {
                    statusX = 0;
                    statusY -= scaleFactor * 8;
                }
            }

            GlStateManager.disableLighting();
        }
    }

    private void drawScaledString(String text, float x, float y, float scale, int color)
    {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0F);
        fontRendererObj.drawString(text, x / scale, y / scale, color, false);
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
        file = !file;

        updateArrowStates();
    }

    private void updateArrowStates()
    {
        buttonList.get(0).enabled = file;
        buttonList.get(1).enabled = !file;
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
