package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.entity.dinosaur.VelociraptorDeltaEntity;

public class VelociraptorDeltaDinosaur extends VelociraptorDinosaur implements Hybrid
{
    public VelociraptorDeltaDinosaur()
    {
        super();

        this.setName("Delta");
        this.setDinosaurClass(VelociraptorDeltaEntity.class);
        this.setEggColorMale(0x526353, 0x3D4F40);
        this.setEggColorFemale(0x526353, 0x3D4F40);
        this.setOverlayCount(0);
        this.disableRegistry();
    }

    @Override
    public Class[] getDinosaurs()
    {
        return new Class[] { VelociraptorDinosaur.class };
    }
}
