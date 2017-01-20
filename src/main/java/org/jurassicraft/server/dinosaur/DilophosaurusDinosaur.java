package org.jurassicraft.server.dinosaur;

import net.minecraftforge.common.BiomeDictionary;
import org.jurassicraft.server.entity.Diet;
import org.jurassicraft.server.entity.SleepTime;
import org.jurassicraft.server.entity.dinosaur.DilophosaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class DilophosaurusDinosaur extends Dinosaur {
    public DilophosaurusDinosaur() {
        super();

        this.setName("Dilophosaurus");
        this.setDinosaurClass(DilophosaurusEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x6B7834, 0x2A2E30);
        this.setEggColorFemale(0x62712E, 0x30241C);
        this.setHealth(10, 30);
        this.setSpeed(0.35, 0.40);
        this.setStrength(2, 6);
        this.setMaximumAge(this.fromDays(40));
        this.setEyeHeight(0.35F, 1.8F);
        this.setSizeX(0.2F, 1.0F);
        this.setSizeY(0.3F, 1.7F);
        this.setStorage(27);
        this.setDiet(Diet.CARNIVORE.get());
        this.setSleepTime(SleepTime.CREPUSCULAR);
        this.setBones("skull", "tooth", "arm_bones", "leg_bones", "neck", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(0.95F, 0.22F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMaxHerdSize(10);
        this.setAttackBias(1200.0);
        this.setSpawn(10, BiomeDictionary.getBiomesForType(BiomeDictionary.Type.FOREST));
        this.setBreeding(false, 2, 4, 24, false, true);
    }
}
