package org.jurassicraft.client.render.renderdef;

import net.ilexiconn.llibrary.client.model.entity.animation.IModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;
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
    private final IModelAnimator animator;

    private final ModelJson modelAdult;
    private final ModelJson modelInfant;
    private ModelJson modelJuvenile;
    private ModelJson modelAdolescent;

    private float shadowSize = 0.65F;

    public RenderDinosaurDefinition(Dinosaur dinosaur, IModelAnimator animator, float parShadowSize)
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

    public IModelAnimator getModelAnimator()
    {
        return animator;
    }

    public float getShadowSize()
    {
        return shadowSize;
    }

    public DinosaurModel getTabulaModel(JsonTabulaModel tabulaModel)
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
