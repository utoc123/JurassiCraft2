package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

/*
 * But ABMK! Birds don't get rabies, only mammals!
 *... well this is super rabies. Same goes for the rest of the diseases...
 */
public class RabiesDisease extends Disease
{
    private DinosaurEntity dinosaur;
    
    public RabiesDisease(DinosaurEntity dinosaur)
    {
        super("Rabies");
        
        this.dinosaur = dinosaur;
    }

    @Override
    public void affects()
    {
        
    }
    
    public DinosaurEntity getDinosaur()
    {
        return dinosaur;
    }
}
