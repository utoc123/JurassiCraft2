package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.api.IHybrid;
import org.jurassicraft.common.entity.EntityVelociraptorCharlie;

public class DinosaurVelociraptorCharlie extends DinosaurVelociraptor implements IHybrid
{
    public DinosaurVelociraptorCharlie()
    {
        super();

        this.setName("Charlie");
        this.setDinosaurClass(EntityVelociraptorCharlie.class);
        this.setEggColorMale(0x525637, 0x2C2F24);
        this.setEggColorFemale(0x525637, 0x2C2F24);
    }

    @Override
    public Class[] getBaseGenes()
    {
        return new Class[] { DinosaurVelociraptor.class }; // TODO
    }

    @Override
    public Class[] getExtraGenes()
    {
        return new Class[] { DinosaurVelociraptor.class };
    }
}
