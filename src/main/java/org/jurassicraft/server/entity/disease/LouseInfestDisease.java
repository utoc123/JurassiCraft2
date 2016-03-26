package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

public class LouseInfestDisease extends Disease
{
    private DinosaurEntity dinosaur;
    
    public LouseInfestDisease(DinosaurEntity dinosaur) {
        super("Sea Louse Infestation");
        
        this.dinosaur = dinosaur;

        setTerminal(false);
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
