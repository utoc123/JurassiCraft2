package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import java.util.Map;

/**
 * @author jabelar
 *         This class handles per-entity animations.
 */
@SideOnly(Side.CLIENT)
public class JabelarAnimationHandler
{
    private static final Minecraft MC = Minecraft.getMinecraft();

    private final AnimationPass DEFAULT_PASS;
    private final AnimationPass MOVEMENT_PASS;

    public JabelarAnimationHandler(DinosaurEntity entity, DinosaurModel model, PosedCuboid[][] poses, Map<Animation, int[][]> poseSequences, boolean useInertialTweens)
    {
        this.DEFAULT_PASS = new AnimationPass(poseSequences, poses, useInertialTweens);
        this.MOVEMENT_PASS = new MovementAnimationPass(poseSequences, poses, useInertialTweens);

        this.init(entity, model);
    }

    public static DinosaurModel loadModel(String tabulaModel, int geneticVariant)
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

    private void init(DinosaurEntity entity, DinosaurModel model)
    {
        AdvancedModelRenderer[] modelParts = this.getModelParts(model);

        this.DEFAULT_PASS.init(modelParts, entity);
        this.MOVEMENT_PASS.init(modelParts, entity);
    }

    public void performAnimations(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks)
    {
        this.DEFAULT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
        this.MOVEMENT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
    }

    private AdvancedModelRenderer[] getModelParts(DinosaurModel model)
    {
        String[] identifiers = model.getCubeIdentifierArray();

        AdvancedModelRenderer[] modelParts = new AdvancedModelRenderer[identifiers.length];

        for (int i = 0; i < modelParts.length; i++)
        {
            modelParts[i] = model.getCubeByIdentifier(identifiers[i]);
        }

        return modelParts;
    }

    public DinosaurModel loadModel(String tabulaModel)
    {
        return loadModel(tabulaModel, 0);
    }
}
