package org.jurassicraft.client.render.renderdef;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.client.render.entity.DinosaurRenderer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EnumGrowthStage;

@SideOnly(Side.CLIENT)
public class RenderDinosaurDefinition implements IRenderFactory<DinosaurEntity>
{
    private final Dinosaur dinosaur;
    private final ITabulaModelAnimator<DinosaurEntity> animator;

    private final TabulaModel modelAdult;
    private final TabulaModel modelInfant;
    private TabulaModel modelJuvenile;
    private TabulaModel modelAdolescent;

    private float shadowSize = 0.65F;

    public RenderDinosaurDefinition(Dinosaur dinosaur, ITabulaModelAnimator<DinosaurEntity> animator, float parShadowSize)
    {
        this.dinosaur = dinosaur;
        this.animator = animator;
        this.shadowSize = parShadowSize;

        this.modelAdult = getTabulaModel(dinosaur.getModelContainer(EnumGrowthStage.ADULT));
        this.modelInfant = getTabulaModel(dinosaur.getModelContainer(EnumGrowthStage.INFANT));
        this.modelJuvenile = getTabulaModel(dinosaur.getModelContainer(EnumGrowthStage.JUVENILE));
        this.modelAdolescent = getTabulaModel(dinosaur.getModelContainer(EnumGrowthStage.ADOLESCENT));
    }

    public ModelBase getModel(EnumGrowthStage stage)
    {
        switch (stage)
        {
            case INFANT:
                return modelInfant;
            case JUVENILE:
                return dinosaur.useAllGrowthStages() ? modelJuvenile : modelInfant;
            case ADOLESCENT:
                return dinosaur.useAllGrowthStages() ? modelAdolescent : modelAdult;
            default:
                return modelAdult;
        }
    }

    public ITabulaModelAnimator<DinosaurEntity> getModelAnimator()
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

    public Render<? super DinosaurEntity> createRenderFor(RenderManager manager)
    {
        return new DinosaurRenderer(this);
    }
}
