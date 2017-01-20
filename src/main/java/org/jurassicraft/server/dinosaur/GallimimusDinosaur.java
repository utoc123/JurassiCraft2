package org.jurassicraft.server.dinosaur;

import net.minecraftforge.common.BiomeDictionary;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.dinosaur.GallimimusEntity;
import org.jurassicraft.server.food.FoodType;
import org.jurassicraft.server.period.TimePeriod;

public class GallimimusDinosaur extends Dinosaur {
    public GallimimusDinosaur() {
        super();

        this.setName("Gallimimus");
        this.setDinosaurClass(GallimimusEntity.class);
        this.setDinosaurType(DinosaurType.SCARED);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD5BA86, 0xD16918);
        this.setEggColorFemale(0xCCBA94, 0xAB733D);
        this.setHealth(5, 30);
        this.setSpeed(0.3, 0.40);
        this.setStrength(1, 5);
        this.setMaximumAge(this.fromDays(35));
        this.setEyeHeight(0.58F, 2.7F);
        this.setSizeX(0.3F, 1.2F);
        this.setSizeY(0.55F, 2.25F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE.get().withModule(new Diet.DietModule(FoodType.INSECT).withCondition(entity -> entity.getAgePercentage() < 25)));
        this.setBones("skull", "tail_vertebrae", "shoulder", "ribcage", "pelvis", "neck_vertebrae", "leg_bones", "foot_bones", "arm_bones");
        this.setHeadCubeName("Head Base");
        this.setScale(0.85F, 0.2F);
        this.setImprintable(true);
        this.setFlee(true);
        this.setFlockSpeed(1.5F);
        this.setSpawn(25, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.PLAINS), BiomeDictionary.getBiomesForType(BiomeDictionary.Type.DRY));
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setJumpHeight(3);
    }
}
