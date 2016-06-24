package org.jurassicraft.client.render.entity;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.ResetControlTabulaModel;
import org.jurassicraft.client.model.animation.entity.vehicle.JeepWranglerAnimator;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public class JeepWranglerRenderer implements IRenderFactory<JeepWranglerEntity>
{
    @Override
    public Render<? super JeepWranglerEntity> createRenderFor(RenderManager manager)
    {
        return new Renderer(manager);
    }

    public static class Renderer extends Render<JeepWranglerEntity>
    {
        private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/jeep_wrangler/jeep_wrangler.png");
        private TabulaModel baseModel;
        private TabulaModel windscreen;

        public Renderer(RenderManager manager)
        {
            super(manager);

            try
            {
                TabulaModelContainer container = TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/jeep_wrangler/jeep_wrangler.tbl");
                baseModel = new ResetControlTabulaModel(container, new JeepWranglerAnimator());
                baseModel.getCube("Windscreen").showModel = false;

                windscreen = new TabulaModel(container);

                for (Map.Entry<String, AdvancedModelRenderer> entry : windscreen.getCubes().entrySet())
                {
                    entry.getValue().showModel = entry.getKey().equals("Windscreen");
                }
            }
            catch (Exception e)
            {
                JurassiCraft.INSTANCE.getLogger().fatal("Failed to load the models for the Jeep Wrangler", e);
            }
        }

        @Override
        public void doRender(JeepWranglerEntity entity, double x, double y, double z, float yaw, float partialTicks)
        {
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            renderModel(entity, x, y, z, yaw, false);
            renderModel(entity, x, y, z, yaw, true);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            super.doRender(entity, x, y, z, yaw, partialTicks);
        }

        private void renderModel(JeepWranglerEntity entity, double x, double y, double z, float yaw, boolean windscreen)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y + 1.25F, (float) z);
            GlStateManager.rotate(180.0F - yaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);
            this.bindEntityTexture(entity);
            (windscreen ? this.windscreen : this.baseModel).render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        }

        @Override
        protected ResourceLocation getEntityTexture(JeepWranglerEntity entity)
        {
            return TEXTURE;
        }
    }
}