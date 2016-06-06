package org.jurassicraft.client.render.entity;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.item.CageSmallEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.lwjgl.opengl.GL11;

public class CageSmallRenderer implements IRenderFactory<CageSmallEntity>
{
    @Override
    public Render<? super CageSmallEntity> createRenderFor(RenderManager manager)
    {
        return new Renderer(manager);
    }

    public static class Renderer extends Render<CageSmallEntity>
    {
        private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/cage_small/cage_small.png");
        private static final ResourceLocation TEXTURE_MARINE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/cage_small/cage_small_marine.png");
        private TabulaModel model;

        public Renderer(RenderManager manager)
        {
            super(manager);

            String modelLoc = "/assets/jurassicraft/models/entities/cage_small/cage_small";

            try
            {
                model = new TabulaModel(TabulaModelHelper.loadTabulaModel(modelLoc));
            }
            catch (Exception e)
            {
                JurassiCraft.INSTANCE.getLogger().fatal("Couldn't load the model " + modelLoc, e);
            }
        }

        @Override
        public void doRender(CageSmallEntity cage, double x, double y, double z, float yaw, float partialTicks)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 1.5F, (float) z);
            GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);

            if (cage.getEntity() != null)
            {
                Minecraft.getMinecraft().getRenderManager().doRenderEntity(cage.getEntity(), 0.0D, -1.45D, 0.0D, 0.0F, 0.0F, false);
            }

            float f4 = 0.75F;
            GlStateManager.scale(f4, f4, f4);
            GlStateManager.scale(1.0F / f4, 1.0F / f4, 1.0F / f4);
            this.bindEntityTexture(cage);
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.model.render(cage, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.popMatrix();
            super.doRender(cage, x, y, z, yaw, partialTicks);
        }

        @Override
        protected ResourceLocation getEntityTexture(CageSmallEntity entity)
        {
            return entity.isMarine() ? TEXTURE_MARINE : TEXTURE;
        }
    }
}