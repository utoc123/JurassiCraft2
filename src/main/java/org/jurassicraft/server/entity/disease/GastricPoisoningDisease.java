package org.jurassicraft.server.entity.disease;

import org.jurassicraft.server.entity.base.DinosaurEntity;

public class GastricPoisoningDisease extends Disease
{
    private DinosaurEntity dinosaur;
    
    public GastricPoisoningDisease(DinosaurEntity dinosaur)
    {
        super("Gastric Poisoning");
        
        this.setTerminal();
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
