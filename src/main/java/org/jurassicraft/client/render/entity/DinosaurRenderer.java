package org.jurassicraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.render.entity.dinosaur.RenderDinosaurDefinition;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class DinosaurRenderer extends RenderLiving<DinosaurEntity> implements IDinosaurRenderer
{
    private static final DynamicTexture dynamicTexture = new DynamicTexture(16, 16);

    public Dinosaur dinosaur;
    public RenderDinosaurDefinition renderDef;

    public Random random;

    public DinosaurRenderer(RenderDinosaurDefinition renderDef, RenderManager renderManager)
    {
        super(renderManager, renderDef.getModel(GrowthStage.INFANT), renderDef.getShadowSize());

        this.dinosaur = renderDef.getDinosaur();
        this.random = new Random();
        this.renderDef = renderDef;

        addLayer(new LayerEyelid(this));
    }

    @Override
    public void preRenderCallback(DinosaurEntity entity, float partialTick)
    {
        //this.renderDef.getModelAnimator().preRenderCallback(entity, partialTick); TODO

        float scale = (float) entity.transitionFromAge(dinosaur.getScaleInfant(), dinosaur.getScaleAdult()); //TODO scale offset

        shadowSize = scale * renderDef.getShadowSize();

        GlStateManager.translate(dinosaur.getOffsetX() * scale, dinosaur.getOffsetY() * scale, dinosaur.getOffsetZ() * scale);

        String name = entity.getCustomNameTag();

        switch (name)
        {
            case "iLexiconn":
            case "JTGhawk137":
                GlStateManager.scale(0.1F, scale, scale);
                break;
            case "Gegy":
                int ticksExisted = entity.ticksExisted / 25 + entity.getEntityId();
                int colorTypes = EnumDyeColor.values().length;
                int k = ticksExisted % colorTypes;
                int l = (ticksExisted + 1) % colorTypes;
                float time = ((float) (entity.ticksExisted % 25) + 2) / 25.0F;
                float[] colors = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(k));
                float[] colors2 = EntitySheep.getDyeRgb(EnumDyeColor.byMetadata(l));
                GlStateManager.color(colors[0] * (1.0F - time) + colors2[0] * time, colors[1] * (1.0F - time) + colors2[1] * time, colors[2] * (1.0F - time) + colors2[2] * time);
                if (time > 0.5F)
                {
                    time = 1 - time;
                }
                GlStateManager.scale(scale * (0.5F + time * 0.5F), scale * (1 + time * 0.5F), scale * (0.9F + time * 0.25F));
                break;
            case "Notch":
            case "Jumbo":
                GlStateManager.scale(scale * 2, scale * 2, scale * 2);
                break;
            case "jglrxavpok":
                GlStateManager.scale(scale, scale, scale * -1);
                break;
            default:
                GlStateManager.scale(scale, scale, scale);
                break;
        }
    }


    @Override
    public ResourceLocation getEntityTexture(DinosaurEntity entity)
    {
        GrowthStage growthStage = entity.getGrowthStage();

        if (!dinosaur.doesSupportGrowthStage(growthStage))
        {
            growthStage = GrowthStage.ADULT;
        }

        return entity.isMale() ? dinosaur.getMaleTexture(growthStage) : dinosaur.getFemaleTexture(growthStage);
    }

    @Override
    protected void rotateCorpse(DinosaurEntity entity, float p_77043_2_, float p_77043_3_, float partialTicks)
    {
        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
    }

    @Override
    public void setModel(ModelBase model)
    {
        this.mainModel = model;
    }

    @Override
    public RenderDinosaurDefinition getRenderDef()
    {
        return renderDef;
    }

    @SideOnly(Side.CLIENT)
    public class LayerEyelid implements LayerRenderer<DinosaurEntity>
    {
        private final DinosaurRenderer renderer;

        public LayerEyelid(DinosaurRenderer renderer)
        {
            this.renderer = renderer;
        }

        @Override
        public void doRenderLayer(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float yaw, float pitch, float scale)
        {
            if (!entity.isInvisible())
            {
                ResourceLocation texture = renderer.dinosaur.getEyelidTexture(entity);

                if (texture != null && entity.areEyelidsClosed())
                {
                    ITextureObject textureObject = Minecraft.getMinecraft().getTextureManager().getTexture(texture);

                    if (textureObject != TextureUtil.MISSING_TEXTURE)
                    {
                        this.renderer.bindTexture(texture);

                        this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
                        this.renderer.setLightmap(entity, partialTicks);
                    }
                }
            }
        }

        @Override
        public boolean shouldCombineTextures()
        {
            return true;
        }
    }
}
