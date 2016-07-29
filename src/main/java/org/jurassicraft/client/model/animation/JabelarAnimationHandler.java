package org.jurassicraft.client.model.animation;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.DinosaurModel;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.tabula.TabulaModelHelper;

import java.util.Map;

/**
 * @author jabelar
 *         This class handles per-entity animations.
 */
@SideOnly(Side.CLIENT)
public class JabelarAnimationHandler {
    private static final Minecraft MC = Minecraft.getMinecraft();

    private final AnimationPass DEFAULT_PASS;
    private final AnimationPass MOVEMENT_PASS;

    public JabelarAnimationHandler(DinosaurEntity entity, DinosaurModel model, PosedCuboid[][] poses, Map<Animation, float[][]> poseSequences, boolean useInertialTweens) {
        this.DEFAULT_PASS = new AnimationPass(poseSequences, poses, useInertialTweens);
        this.MOVEMENT_PASS = new MovementAnimationPass(poseSequences, poses, useInertialTweens);

        this.init(entity, model);
    }

    public static DinosaurModel loadModel(String model) {
        try {
            return new DinosaurModel(TabulaModelHelper.loadTabulaModel(model), null);
        } catch (Exception e) {
            JurassiCraft.INSTANCE.getLogger().error("Could not load Tabula model " + model, e);
        }
        return null;
    }

    private void init(DinosaurEntity entity, DinosaurModel model) {
        AdvancedModelRenderer[] parts = this.getParts(model);

        this.DEFAULT_PASS.init(parts, entity);
        this.MOVEMENT_PASS.init(parts, entity);
    }

    public void performAnimations(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float ticks) {
        this.DEFAULT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
        this.MOVEMENT_PASS.performAnimations(entity, limbSwing, limbSwingAmount, ticks);
    }

    private AdvancedModelRenderer[] getParts(DinosaurModel model) {
        AdvancedModelRenderer[] parts = new AdvancedModelRenderer[model.getIdentifierCubes().size()];
        int i = 0;
        for (Map.Entry<String, AdvancedModelRenderer> part : model.getIdentifierCubes().entrySet()) {
            parts[i++] = part.getValue();
        }
        return parts;
    }
}
