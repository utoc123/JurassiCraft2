package org.jurassicraft.client.model;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.DinosaurEntity;

import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class DinosaurModel extends TabulaModel {
    public DinosaurModel(TabulaModelContainer model) {
        this(model, null);
    }

    public DinosaurModel(TabulaModelContainer model, ITabulaModelAnimator animator) {
        super(model, animator);
    }

    @Override
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity) {
        DinosaurEntity dinosaur = (DinosaurEntity) entity;

        if (dinosaur.isCarcass()) {
            this.setMovementScale(0.0F);
        } else {
            this.setMovementScale(dinosaur.isSleeping() ? 0.5F : 1.0F);
        }

        super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
    }

    public String[] getCubeIdentifierArray() {
        String[] cubeIdentifiers = new String[this.identifierMap.size()];
        int index = 0;

        Set<String> identifiers = this.identifierMap.keySet();

        for (String identifier : identifiers) {
            cubeIdentifiers[index] = identifier;
            index++;
        }

        return cubeIdentifiers;
    }

    public Map<String, AdvancedModelRenderer> getIdentifierCubes() {
        return this.identifierMap;
    }

    @Override
    public void faceTarget(float yaw, float pitch, float rotationDivisor, AdvancedModelRenderer... boxes) {
        float actualRotationDivisor = rotationDivisor * boxes.length;
        float yawAmount = yaw / (180.0F / (float) Math.PI) / actualRotationDivisor;
        float pitchAmount = pitch / (180.0F / (float) Math.PI) / actualRotationDivisor;

        for (AdvancedModelRenderer box : boxes) {
            box.rotateAngleY += yawAmount;
            box.rotateAngleX += pitchAmount;
        }
    }
}