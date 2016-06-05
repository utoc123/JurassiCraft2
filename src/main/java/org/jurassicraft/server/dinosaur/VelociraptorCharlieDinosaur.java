package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.entity.dinosaur.VelociraptorCharlieEntity;

public class VelociraptorCharlieDinosaur extends VelociraptorDinosaur implements Hybrid
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
    public Class[] getDinosaurs()
    {
        return new Class[] { VelociraptorDinosaur.class };
    }
}
