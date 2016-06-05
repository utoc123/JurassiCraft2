package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.entity.dinosaur.VelociraptorBlueEntity;

public class VelociraptorBlueDinosaur extends VelociraptorDinosaur implements Hybrid
{
    public VelociraptorBlueDinosaur()
    {
        super();

        this.setName("Blue");
        this.setDinosaurClass(VelociraptorBlueEntity.class);
        this.setEggColorMale(0x5A5752, 0x32D3E55);
        this.setEggColorFemale(0x5A5752, 0x32D3E55);
        this.setOverlayCount(0);
        this.disableRegistry();
    }

    @Override
    public Class[] getDinosaurs()
    {
        return new Class[] { VelociraptorDinosaur.class };
    }
}
