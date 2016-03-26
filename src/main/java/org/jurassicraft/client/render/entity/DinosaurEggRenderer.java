package org.jurassicraft.client.render.entity;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.lwjgl.opengl.GL11;

public class DinosaurEggRenderer implements IRenderFactory<DinosaurEggEntity>
{
    @Override
    public Render<? super DinosaurEggEntity> createRenderFor(RenderManager manager)
    {
        return new Renderer();
    }

    public static class Renderer extends Render<DinosaurEggEntity>
    {
        private static final ResourceLocation texture = new ResourceLocation(JurassiCraft.MODID, "textures/entities/egg/tyrannosaurus.png");
        private TabulaModel model;

        /**
         * This is just what I decided to go with for this push. I tried using a Hashmap and load all the tabula models,
         * then decide which model to use in the doRender, but the dinosaur was returning null.
         */
        public Renderer()
        {
            super(Minecraft.getMinecraft().getRenderManager());

            String modelLoc = "/assets/jurassicraft/models/entities/egg/tyrannosaurus";
            try
            {
                model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel(modelLoc));
            }
            catch (Exception e)
            {
                JurassiCraft.instance.getLogger().fatal("Couldn't load the model " + modelLoc, e);
            }
        }

        @Override
        public void doRender(DinosaurEggEntity egg, double x, double y, double z, float yaw, float partialTicks)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 1.5F, (float) z);
            GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
            float scale = 0.75F;
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.scale(1.0F / scale, 1.0F / scale, 1.0F / scale);
            this.bindEntityTexture(egg);
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            GlStateManager.enableBlend();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.model.render(egg, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.disableBlend();
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GlStateManager.popMatrix();
            super.doRender(egg, x, y, z, yaw, partialTicks);
        }

        @Override
        protected ResourceLocation getEntityTexture(DinosaurEggEntity entity)
        {
            return texture;
        }
    }
}