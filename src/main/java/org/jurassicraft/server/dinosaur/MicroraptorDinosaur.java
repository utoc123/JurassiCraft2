package org.jurassicraft.server.dinosaur;

import net.minecraftforge.common.BiomeDictionary;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.dinosaur.MicroraptorEntity;
import org.jurassicraft.server.period.TimePeriod;

public class MicroraptorDinosaur extends Dinosaur {
    public MicroraptorDinosaur() {
        super();
        this.setName("Microraptor");
        this.setDinosaurClass(MicroraptorEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x142146, 0x101625);
        this.setEggColorFemale(0x0E1423, 0x121827);
        this.setSpeed(0.25, 0.30);
        this.setAttackSpeed(1.3);
        this.setHealth(4, 20);
        this.setStrength(1, 4);
        this.setMaximumAge(this.fromDays(30));
        this.setEyeHeight(0.2F, 0.5F);
        this.setSizeX(0.2F, 0.7F);
        this.setSizeY(0.25F, 0.6F);
        this.setStorage(9);
        this.setDiet(Diet.INSECTIVORE_CARNIVORE);
        this.setBones("tooth", "arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(0.4F, 0.15F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMaxHerdSize(16);
        this.setAttackBias(400.0);
        this.setSpawn(10, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.JUNGLE), BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST), BiomeDictionary.getBiomesForType(BiomeDictionary.Type.DENSE));
    }
}
