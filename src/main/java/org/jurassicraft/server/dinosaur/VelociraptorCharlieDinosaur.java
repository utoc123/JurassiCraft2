package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.api.IHybrid;
import org.jurassicraft.server.entity.VelociraptorCharlieEntity;

public class VelociraptorCharlieDinosaur extends VelociraptorDinosaur implements IHybrid
{
    public VelociraptorCharlieDinosaur()
    {
        super();

        this.setName("Charlie");
        this.setDinosaurClass(VelociraptorCharlieEntity.class);
        this.setEggColorMale(0x525637, 0x2C2F24);
        this.setEggColorFemale(0x525637, 0x2C2F24);
        this.setOverlayCount(0);
        this.disableRegistry();
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
