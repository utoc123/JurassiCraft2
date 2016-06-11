package org.jurassicraft.server.dinosaur.disabled;

import net.minecraft.util.ResourceLocation;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.dinosaur.TyrannosaurusDinosaur;
import org.jurassicraft.server.dinosaur.VelociraptorDinosaur;
import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.base.GrowthStage;
import org.jurassicraft.server.entity.dinosaur.disabled.IndominusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class IndominusDinosaur extends Dinosaur implements Hybrid
{
    private ResourceLocation texture;

    private ResourceLocation overlayTexture;

    public IndominusDinosaur()
    {
        super();

        this.setName("Indominus");
        this.setDinosaurClass(IndominusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS); //TODO, it's a hybrid, what do you do here?
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
        this.setDiet(Diet.CARNIVORE);
        this.setBones("skull", "tooth", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "foot_bones", "arm_bones");
        this.setHeadCubeName("Head");
        this.setScale(3.4F, 0.4F);

        this.texture = new ResourceLocation(getDinosaurTexture("camouflage"));
        this.overlayTexture = new ResourceLocation(getDinosaurTexture(""));

        this.disableRegistry();
    }

    @Override
    public Class<? extends Dinosaur>[] getDinosaurs()
    {
        return new Class[] { TyrannosaurusDinosaur.class, VelociraptorDinosaur.class, GiganotosaurusDinosaur.class, RugopsDinosaur.class, MajungasaurusDinosaur.class, CarnotaurusDinosaur.class, TherizinosaurusDinosaur.class };
    }

    @Override
    public ResourceLocation getMaleTexture(GrowthStage stage)
    {
        return texture;
    }

    @Override
    public ResourceLocation getFemaleTexture(GrowthStage stage)
    {
        return texture;
    }

    public ResourceLocation getCamoTexture(GrowthStage stage)
    {
        return overlayTexture;
    }
}
