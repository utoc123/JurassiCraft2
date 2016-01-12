package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.api.IHybrid;
import org.jurassicraft.common.entity.EntityVelociraptorDelta;

public class DinosaurVelociraptorDelta extends DinosaurVelociraptor implements IHybrid
{
    public DinosaurVelociraptorDelta()
    {
        super();

        this.setName("Delta");
        this.setDinosaurClass(EntityVelociraptorDelta.class);
        this.setEggColorMale(0x526353, 0x3D4F40);
        this.setEggColorFemale(0x526353, 0x3D4F40);
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
