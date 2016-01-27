package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.api.IHybrid;
import org.jurassicraft.server.entity.VelociraptorDeltaEntity;

public class VelociraptorDeltaDinosaur extends VelociraptorDinosaur implements IHybrid
{
    public VelociraptorDeltaDinosaur()
    {
        super();

        this.setName("Delta");
        this.setDinosaurClass(VelociraptorDeltaEntity.class);
        this.setEggColorMale(0x526353, 0x3D4F40);
        this.setEggColorFemale(0x526353, 0x3D4F40);
        this.setOverlayCount(0);
    }

    @Override
    public Class[] getBaseGenes()
    {
        return new Class[] { VelociraptorDinosaur.class }; // TODO
    }

    @Override
    public Class[] getExtraGenes()
    {
        return new Class[] { VelociraptorDinosaur.class };
    }

    @Override
    public boolean useAllGrowthStages()
    {
        return false;
    }
}
