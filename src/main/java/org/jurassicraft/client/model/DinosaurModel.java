package org.jurassicraft.client.model;

import net.ilexiconn.llibrary.client.model.tabula.ITabulaModelAnimator;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.container.TabulaModelContainer;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class DinosaurModel extends TabulaModel
{
    public DinosaurModel(TabulaModelContainer model)
    {
        this(model, null);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
    {
        DinosaurEntity dinosaur = (DinosaurEntity) entity;

        this.setMovementScale(dinosaur.isSleeping() ? 0.5F : 1.0F);

        super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
    }

    public DinosaurModel(TabulaModelContainer model, ITabulaModelAnimator animator)
    {
        super(model, animator);
    }

    public String[] getCubeNamesArray()
    {
        String[] cubeNamesArray = new String[cubes.size()];
        int index = 0;

        Set<String> names = cubes.keySet();

        for (String name : names)
        {
            cubeNamesArray[index] = name;
            index++;
        }

        return cubeNamesArray;
    }
}