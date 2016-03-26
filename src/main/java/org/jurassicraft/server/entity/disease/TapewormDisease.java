package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

public class TapewormDisease extends Disease
{
    private DinosaurEntity dinosaur;
    
    public TapewormDisease(DinosaurEntity dinosaur)
    {
        super("Tapeworm");
        
        setTerminal(false);
        this.dinosaur = dinosaur;
    }

    public void affects()
    {
        
    }

    public DinosaurEntity getDinosaur()
    {
        return dinosaur;
    }
}
