package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.particle.HurtParticle;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import java.util.Map;

/**
 * @author jabelar
 * This class handles per-entity animations.
 */
@SideOnly(Side.CLIENT)
public class JabelarAnimationHandler
{
    private static final Minecraft MC = Minecraft.getMinecraft();

    private final AnimationPass DEFAULT_PASS;
    private final AnimationPass MOVEMENT_PASS;

    public JabelarAnimationHandler(DinosaurEntity entity, DinosaurModel model, AdvancedModelRenderer[][] poses, Map<Animation, int[][]> poseSequences, boolean useInertialTweens)
    {
        this.DEFAULT_PASS = new AnimationPass(poseSequences, poses, useInertialTweens);
        this.MOVEMENT_PASS = new MovementAnimationPass(poseSequences, poses, useInertialTweens);

        this.init(entity, model);
    }

    private void init(DinosaurEntity entity, DinosaurModel model)
    {
        AdvancedModelRenderer[] modelParts = this.getModelParts(model);

        this.DEFAULT_PASS.initSequence(entity, entity.getAnimation());
        this.DEFAULT_PASS.init(modelParts, entity);

        this.MOVEMENT_PASS.init(modelParts, entity);
    }

    public void performAnimations(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks)
    {
        this.performHurtAnimation(entity);

        this.DEFAULT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
        this.MOVEMENT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
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
            return new DinosaurModel(TabulaModelHelper.loadTabulaModel(tabulaModel), null); // okay to use null for animator
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

        ParticleManager particleManager = MC.effectRenderer;

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
                        this.performHurtEffect(world, particleManager, (x / amount * entityWidth) + posX - (entityWidth / 2.0F), (y / amount * entityHeight) + posY, (z / amount * entityWidth) + posZ - (entityWidth / 2.0F));
                    }
                }
            }
        }
    }

    private void performHurtEffect(World world, ParticleManager particleManager, double x, double y, double z)
    {
        particleManager.addEffect((new HurtParticle(world, x + 0.5D, y + 0.5D, z + 0.5D, 0, 0, 0).setBlockPos(new BlockPos((int) x, (int) y, (int) z))));
    }
}
