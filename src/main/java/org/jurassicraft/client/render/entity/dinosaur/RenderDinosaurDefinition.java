package org.jurassicraft.client.render.entity.dinosaur;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.render.entity.DinosaurRenderer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.GrowthStage;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import java.util.Locale;

@SideOnly(Side.CLIENT)
public class RenderDinosaurDefinition implements IRenderFactory<DinosaurEntity>
{
    private static TabulaModel DEFAULT_EGG_MODEL;
    private static ResourceLocation DEFAULT_EGG_TEXTURE;

    static
    {
        try
        {
            DEFAULT_EGG_MODEL = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/egg/tyrannosaurus"));
            DEFAULT_EGG_TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/entities/egg/tyrannosaurus.png");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private final Dinosaur dinosaur;
    private final ITabulaModelAnimator<? extends DinosaurEntity> animator;
    private final TabulaModel modelAdult;
    private final TabulaModel modelInfant;
    private TabulaModel modelJuvenile;
    private TabulaModel modelAdolescent;
    private TabulaModel eggModel;
    private ResourceLocation eggTexture;
    private float shadowSize = 0.65F;

    public RenderDinosaurDefinition(Dinosaur dinosaur, ITabulaModelAnimator<? extends DinosaurEntity> animator, float parShadowSize)
    {
        this.dinosaur = dinosaur;
        this.animator = animator;
        this.shadowSize = parShadowSize;

        this.modelAdult = getTabulaModel(dinosaur.getModelContainer(GrowthStage.ADULT));
        this.modelInfant = getTabulaModel(dinosaur.getModelContainer(GrowthStage.INFANT));
        this.modelJuvenile = getTabulaModel(dinosaur.getModelContainer(GrowthStage.JUVENILE));
        this.modelAdolescent = getTabulaModel(dinosaur.getModelContainer(GrowthStage.ADOLESCENT));

        try
        {
            String name = dinosaur.getName().toLowerCase(Locale.ENGLISH);
            this.eggModel = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/entities/egg/" + name));
            this.eggTexture = new ResourceLocation(JurassiCraft.MODID, "textures/entities/egg/" + name + ".png");
        }
        catch (Exception e)
        {
        }
    }

    public ModelBase getModel(GrowthStage stage)
    {
        switch (stage)
        {
            case INFANT:
                return modelInfant;
            case JUVENILE:
                return modelJuvenile;
            case ADOLESCENT:
                return modelAdolescent;
            default:
                return modelAdult;
        }
    }

    public ModelBase getEggModel()
    {
        return eggModel == null ? DEFAULT_EGG_MODEL : eggModel;
    }

    public ResourceLocation getEggTexture()
    {
        return eggTexture == null ? DEFAULT_EGG_TEXTURE : eggTexture;
    }

    public ITabulaModelAnimator<? extends DinosaurEntity> getModelAnimator()
    {
        return animator;
    }

    public float getShadowSize()
    {
        return shadowSize;
    }

    public DinosaurModel getTabulaModel(TabulaModelContainer tabulaModel)
    {
        return new DinosaurModel(tabulaModel, getModelAnimator());
    }

    public Dinosaur getDinosaur()
    {
        return dinosaur;
    }

    @Override
    public Render<? super DinosaurEntity> createRenderFor(RenderManager manager)
    {
        return new DinosaurRenderer(this, manager);
    }
}
