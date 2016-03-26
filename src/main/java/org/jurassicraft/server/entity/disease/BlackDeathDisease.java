package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

/*
 * Testing disease for contagious. Will probably be removed when out of prototyping
 */
public class BlackDeathDisease extends Disease
{    
    private DinosaurEntity dinosaur;
    
    public BlackDeathDisease(DinosaurEntity dinosaur)
    {
        super("Mycobacteriosis");
        
        this.setTerminal();
        this.setContagious(true);
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
