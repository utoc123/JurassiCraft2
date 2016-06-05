package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.entity.base.Diet;
import org.jurassicraft.server.entity.base.SleepingSchedule;
import org.jurassicraft.server.entity.dinosaur.CarnotaurusEntity;
import org.jurassicraft.server.period.TimePeriod;

public class CarnotaurusDinosaur extends Dinosaur
{
    public CarnotaurusDinosaur()
    {
        super();

        this.setName("Carnotaurus");
        this.setDinosaurClass(CarnotaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xA2996E, 0x545338);
        this.setEggColorFemale(0x9C8E6A, 0x635639);
        this.setHealth(10, 30);
        this.setSpeed(0.42, 0.30);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.4F, 2.4F);
        this.setSizeX(0.45F, 2.25F);
        this.setSizeY(0.6F, 2.8F);
        this.setStorage(36);
        this.setDiet(Diet.CARNIVORE);
        this.setSleepingSchedule(SleepingSchedule.CREPUSCULAR);
        this.setBones("skull", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.25F);
        this.disableRegistry();
    }
}
