package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.api.IHybrid;
import org.jurassicraft.common.entity.EntityVelociraptorBlue;

public class DinosaurVelociraptorBlue extends DinosaurVelociraptor implements IHybrid
{
    public DinosaurVelociraptorBlue()
    {
        super();

        this.setName("Blue");
        this.setDinosaurClass(EntityVelociraptorBlue.class);
        this.setEggColorMale(0x5A5752, 0x32D3E55);
        this.setEggColorFemale(0x5A5752, 0x32D3E55);
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
