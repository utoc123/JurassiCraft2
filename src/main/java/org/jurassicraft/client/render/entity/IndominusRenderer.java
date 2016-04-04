package org.jurassicraft.client.render.entity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.render.renderdef.RenderDinosaurDefinition;
import org.jurassicraft.server.dinosaur.IndominusDinosaur;
import org.jurassicraft.server.entity.IndominusEntity;
import org.jurassicraft.server.entity.base.DinosaurEntity;

@SideOnly(Side.CLIENT)
public class IndominusRenderer extends DinosaurRenderer
{
    public IndominusRenderer(RenderDinosaurDefinition renderDef, RenderManager manager)
    {
        super(renderDef, manager);

        addLayer(new LayerIndominusCamo(this));
    }

    @Override
    public void doRender(DinosaurEntity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void preRenderCallback(DinosaurEntity entity, float side)
    {
        super.preRenderCallback(entity, side);

        IndominusEntity indominus = (IndominusEntity) entity;
        float[] color = indominus.getSkinColor();
        GlStateManager.color(color[0], color[1], color[2], 1.0F);
    }

    @SideOnly(Side.CLIENT)
    public class LayerIndominusCamo implements LayerRenderer
    {
        private final IndominusRenderer renderer;

        public LayerIndominusCamo(IndominusRenderer renderer)
        {
            this.renderer = renderer;
        }

        public void render(DinosaurEntity entity, float armSwing, float armSwingAmount, float p_177148_4_, float p_177148_5_, float p_177148_6_, float p_177148_7_, float partialTicks)
        {
            if (!entity.isInvisible())
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                IndominusDinosaur indominusDino = (IndominusDinosaur) dinosaur;

                this.renderer.bindTexture(indominusDino.getCamoTexture(entity.getGrowthStage()));

                this.renderer.getMainModel().render(entity, armSwing, armSwingAmount, p_177148_5_, p_177148_6_, p_177148_7_, partialTicks);
                this.renderer.setLightmap(entity, p_177148_4_);
            }
        }

        @Override
        public boolean shouldCombineTextures()
        {
            return true;
        }

        @Override
        public void doRenderLayer(EntityLivingBase entity, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
        {
            this.render((DinosaurEntity) entity, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
        }
    }
}
