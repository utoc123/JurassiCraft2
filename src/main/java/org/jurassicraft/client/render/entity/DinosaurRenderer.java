package org.jurassicraft.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
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
import org.jurassicraft.client.render.renderdef.RenderDinosaurDefinition;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class DinosaurRenderer extends RenderLiving<DinosaurEntity> implements IDinosaurRenderer
{
    private static final DynamicTexture dynamicTexture = new DynamicTexture(16, 16);

    public Dinosaur dinosaur;
    public RenderDinosaurDefinition renderDef;

    public Random random;

    public DinosaurRenderer(RenderManager manager, RenderDinosaurDefinition renderDef)
    {
        super(manager, renderDef.getModel(GrowthStage.INFANT), renderDef.getShadowSize());

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
                float[] colors = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(k));
                float[] colors2 = EntitySheep.func_175513_a(EnumDyeColor.byMetadata(l));
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

    // need to override so that red overlay doesn't persist after death
    @Override
    protected boolean setBrightness(DinosaurEntity entity, float partialTicks, boolean combineTextures)
    {
        float f1 = entity.getBrightness(partialTicks);
        int i = this.getColorMultiplier(entity, f1, partialTicks);
        boolean flag1 = (i >> 24 & 255) > 0;
        boolean flag2 = false; // entity.hurtTime > 0 || entity.deathTime > 0;

        if (!flag1)
        {
            return false;
        }
        else
        {
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableTexture2D();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            this.brightnessBuffer.position(0);

            float f2 = (i >> 24 & 255) / 255.0F;
            float f3 = (i >> 16 & 255) / 255.0F;
            float f4 = (i >> 8 & 255) / 255.0F;
            float f5 = (i & 255) / 255.0F;
            this.brightnessBuffer.put(f3);
            this.brightnessBuffer.put(f4);
            this.brightnessBuffer.put(f5);
            this.brightnessBuffer.put(1.0F - f2);

            this.brightnessBuffer.flip();
            GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, this.brightnessBuffer);
            GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
            GlStateManager.enableTexture2D();
            GlStateManager.bindTexture(dynamicTexture.getGlTextureId());
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            return true;
        }
    }

    @Override
    public ResourceLocation getEntityTexture(DinosaurEntity entity)
    {
        int rareVariant = entity.getRareVariant();

        GrowthStage growthStage = entity.getGrowthStage();

        if (!dinosaur.doesSupportGrowthStage(growthStage))
        {
            growthStage = GrowthStage.ADULT;
        }

        if (rareVariant != 0)
        {
            return dinosaur.getRareVariantTexture(rareVariant, growthStage);
        }

        return entity.isMale() ? dinosaur.getMaleTexture(growthStage) : dinosaur.getFemaleTexture(growthStage);
    }

    @Override
    protected void rotateCorpse(DinosaurEntity entity, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        if (!(entity.deathTime > 0))
        {
            super.rotateCorpse(entity, p_77043_2_, p_77043_3_, p_77043_4_);
        }
        else
        {
            GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
        }
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
        public void doRenderLayer(DinosaurEntity entity, float v, float v1, float v2, float v3, float v4, float v5, float v6)
        {
            if (!entity.isInvisible())
            {
                ResourceLocation texture = renderer.dinosaur.getEyelidTexture(entity);

                if (texture != null && entity.areEyelidsClosed())
                {
                    ITextureObject textureObject = Minecraft.getMinecraft().getTextureManager().getTexture(texture);

                    if (textureObject != TextureUtil.missingTexture)
                    {
                        this.renderer.bindTexture(texture);

                        this.renderer.getMainModel().render(entity, v, v1, v3, v4, v5, v6);
                        this.renderer.func_177105_a(entity, v2);
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
