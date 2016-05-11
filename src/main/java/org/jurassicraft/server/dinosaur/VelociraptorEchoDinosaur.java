package org.jurassicraft.server.dinosaur;

import org.jurassicraft.server.api.IHybrid;
import org.jurassicraft.server.entity.VelociraptorEchoEntity;

public class VelociraptorEchoDinosaur extends VelociraptorDinosaur implements IHybrid
{
    public VelociraptorEchoDinosaur()
    {
        super();

        this.setName("Echo");
        this.setDinosaurClass(VelociraptorEchoEntity.class);
        this.setEggColorMale(0x665941, 0x363E43);
        this.setEggColorFemale(0x665941, 0x363E43);
        this.setOverlayCount(0);
        this.disableRegistry();
    }

    @Override
    public Class[] getDinosaurs()
    {
        return new Class[] { VelociraptorDinosaur.class };
    }
}
