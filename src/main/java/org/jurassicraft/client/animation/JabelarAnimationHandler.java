package org.jurassicraft.client.animation;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.fx.BloodEntityFX;

import java.util.Map;

/**
 * @author jabelar This class is used to hold per-entity animation variables for use with Jabelar's animation tweening system.
 */
@SideOnly(Side.CLIENT)
public class JabelarAnimationHandler
{
    private static final Minecraft MC = Minecraft.getMinecraft();

    private final AnimationPass DEFAULT_PASS;

    /**
     * @param model             the model to animate
     * @param poses             for each pose(-index) an array of posed Renderers
     * @param poseSequences     maps from an {@link Animations} to the sequence of (pose-index, tween-length)
     * @param useInertialTweens
     */
    public JabelarAnimationHandler(DinosaurEntity entity, DinosaurModel model, AdvancedModelRenderer[][] poses, Map<Animation, int[][]> poseSequences, boolean useInertialTweens)
    {
        this.DEFAULT_PASS = new AnimationPass(poseSequences, poses, useInertialTweens);

        this.init(entity, model);
    }

    private void init(DinosaurEntity entity, DinosaurModel model)
    {
        AdvancedModelRenderer[] modelParts = this.getModelParts(model);

        this.DEFAULT_PASS.initSequence(entity, entity.getAnimation());
        this.DEFAULT_PASS.init(entity, modelParts);
    }

    public void performAnimations(DinosaurEntity entity, float partialTicks)
    {
        this.performHurtAnimation(entity);

        this.DEFAULT_PASS.performAnimations(entity, partialTicks);
    }

    private AdvancedModelRenderer[] getModelParts(DinosaurModel model)
    {
        String[] partNames = model.getCubeNamesArray();

        AdvancedModelRenderer[] modelParts = new AdvancedModelRenderer[partNames.length];

        for (int i = 0; i < modelParts.length; i++)
        {
            modelParts[i] = model.getCube(partNames[i]);
        }

        return modelParts;
    }

    public static DinosaurModel getTabulaModel(String tabulaModel, int geneticVariant)
    {
        // catch the exception so you can call method without further catching
        try
        {
            return new DinosaurModel(TabulaModelHandler.INSTANCE.loadTabulaModel(tabulaModel), null); // okay to use null for animator
            // parameter as we get animator
            // from passed-in model
        }
        catch (Exception e)
        {
            JurassiCraft.INSTANCE.getLogger().error("Could not load Tabula model = " + tabulaModel);
        }

        return null;
    }

    public DinosaurModel getTabulaModel(String tabulaModel)
    {
        return getTabulaModel(tabulaModel, 0);
    }

    private void performHurtAnimation(DinosaurEntity entity)
    {
        double posX = entity.posX;
        double posY = entity.posY;
        double posZ = entity.posZ;

        World world = entity.worldObj;

        EffectRenderer effectRenderer = MC.effectRenderer;

        if (entity.hurtTime == entity.maxHurtTime - 1)
        {
            float entityWidth = entity.width;
            float entityHeight = entity.height;

            float amount = 2;

            for (int x = 0; x < amount; x++)
            {
                for (int y = 0; y < amount; y++)
                {
                    for (int z = 0; z < amount; z++)
                    {
                        this.performHurtEffect(world, effectRenderer, (x / amount * entityWidth) + posX - (entityWidth / 2.0F), (y / amount * entityHeight) + posY, (z / amount * entityWidth) + posZ - (entityWidth / 2.0F));
                    }
                }
            }
        }
    }

    private void performHurtEffect(World world, EffectRenderer effectRenderer, double x, double y, double z)
    {
        effectRenderer.addEffect((new BloodEntityFX(world, x + 0.5D, y + 0.5D, z + 0.5D, 0, 0, 0)).func_174846_a(new BlockPos(x, y, z)));
    }
}
