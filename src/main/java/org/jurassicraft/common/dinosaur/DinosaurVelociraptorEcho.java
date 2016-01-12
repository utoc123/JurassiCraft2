package org.jurassicraft.common.dinosaur;

import org.jurassicraft.common.api.IHybrid;
import org.jurassicraft.common.entity.EntityVelociraptorEcho;

public class DinosaurVelociraptorEcho extends DinosaurVelociraptor implements IHybrid
{
    public DinosaurVelociraptorEcho()
    {
        super();

        this.setName("Echo");
        this.setDinosaurClass(EntityVelociraptorEcho.class);
        this.setEggColorMale(0x665941, 0x363E43);
        this.setEggColorFemale(0x665941, 0x363E43);
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
