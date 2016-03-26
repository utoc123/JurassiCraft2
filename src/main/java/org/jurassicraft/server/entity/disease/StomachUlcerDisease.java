package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

public class StomachUlcerDisease extends Disease
{
    private DinosaurEntity dinosaur;

    public StomachUlcerDisease(DinosaurEntity dinosaur)
    {
        super("Stomach Ulcers");
        
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
