package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

public class BumblefootDisease extends Disease
{
    private DinosaurEntity dinosaur;
    
    public BumblefootDisease(DinosaurEntity dinosaur)
    {
        super("Bumblefoot");
        
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
