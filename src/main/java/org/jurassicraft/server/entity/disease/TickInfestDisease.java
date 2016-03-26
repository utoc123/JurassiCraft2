package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

public class TickInfestDisease extends Disease
{
    private DinosaurEntity dinosaur;
    
    public TickInfestDisease(DinosaurEntity dinosaur)
    {
        super("Tick Infestation");
        
        setTerminal(false);
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
