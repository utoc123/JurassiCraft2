package org.jurassicraft.common.dinosaur;

import net.minecraft.util.ResourceLocation;
import org.jurassicraft.common.api.IHybrid;
import org.jurassicraft.common.entity.EntityIndominus;
import org.jurassicraft.common.entity.base.EnumDiet;
import org.jurassicraft.common.entity.base.EnumGrowthStage;
import org.jurassicraft.common.period.EnumTimePeriod;

public class DinosaurIndominus extends Dinosaur implements IHybrid
{
    private ResourceLocation texture;

    private ResourceLocation overlayTexture;

    private Class[] baseGenes;
    private Class[] extraGenes;

    public DinosaurIndominus()
    {
        super();

        this.setName("Indominus");
        this.setDinosaurClass(EntityIndominus.class);
        this.setTimePeriod(EnumTimePeriod.CRETACEOUS); //TODO, it's a hybrid, what do you do here?
        this.setEggColorMale(0xBEBABB, 0x95949A);
        this.setEggColorFemale(0xBEBABB, 0x95949A);
        this.setHealth(20, 100);
        this.setSpeed(0.45, 0.40);
        this.setStrength(5, 40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.55F, 5.4F);
        this.setSizeX(0.4F, 4.0F);
        this.setSizeY(0.7F, 6.0F);
        this.setStorage(54);
        this.setDiet(EnumDiet.CARNIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "foot_bones", "arm_bones");

        this.texture = new ResourceLocation(getDinosaurTexture("camouflage"));
        this.overlayTexture = new ResourceLocation(getDinosaurTexture(""));

        this.baseGenes = new Class[] { DinosaurTyrannosaurus.class, DinosaurVelociraptor.class };
        this.extraGenes = new Class[] { DinosaurGiganotosaurus.class, DinosaurRugops.class, DinosaurMajungasaurus.class, DinosaurCarnotaurus.class }; // TODO therizino
    }

    @Override
    public Class[] getBaseGenes()
    {
        return baseGenes;
    }

    @Override
    public Class[] getExtraGenes()
    {
        return extraGenes;
    }

    @Override
    public ResourceLocation getMaleTexture(EnumGrowthStage stage)
    {
        return texture;
    }

    @Override
    public ResourceLocation getFemaleTexture(EnumGrowthStage stage)
    {
        return texture;
    }

    public ResourceLocation getCamoTexture(EnumGrowthStage stage)
    {
        return overlayTexture;
    }
}
