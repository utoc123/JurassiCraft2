package org.jurassicraft.client.model;

import net.ilexiconn.llibrary.client.model.entity.animation.IModelAnimator;
import net.ilexiconn.llibrary.client.model.modelbase.MowzieModelRenderer;
import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.ilexiconn.llibrary.common.animation.Animator;
import net.ilexiconn.llibrary.common.animation.IAnimated;
import net.ilexiconn.llibrary.common.json.container.JsonTabulaModel;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class DinosaurModel extends ModelJson
{
    public Field nameMapField;

    public DinosaurModel(JsonTabulaModel model)
    {
        this(model, null);
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotation, float rotationYaw, float rotationPitch, float partialTicks, Entity entity)
    {
        DinosaurEntity dinosaur = (DinosaurEntity) entity;

        this.setMovementScale(dinosaur.isSleeping() ? 0.5F : 1.0F);

        super.setRotationAngles(limbSwing, limbSwingAmount, rotation, rotationYaw, rotationPitch, partialTicks, entity);
    }

    public DinosaurModel(JsonTabulaModel model, IModelAnimator animator)
    {
        super(model, animator);

        try
        {
            this.nameMapField = ModelJson.class.getDeclaredField("nameMap");
            this.nameMapField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    public List<MowzieModelRenderer> getParts()
    {
        return super.parts;
    }

    public String[] getCubeNamesArray()
    {
        Map<String, MowzieModelRenderer> nameMap; //TODO: temp fix
        try
        {
            nameMap = (Map<String, MowzieModelRenderer>) nameMapField.get(this);
            String[] cubeNamesArray = new String[nameMap.size()];
            int index = 0;

            Set<String> names = nameMap.keySet();

            for (String name : names)
            {
                cubeNamesArray[index] = name;
                index++;
            }

            return cubeNamesArray;
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}