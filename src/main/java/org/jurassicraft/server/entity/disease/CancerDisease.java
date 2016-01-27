package org.jurassicraft.server.entity.disease;

public class CancerDisease extends Disease
{

    public CancerDisease()
    {
        super("Cancer");
        this.setTerminal();
    }

    @Override
    public void affects()
    {

    }
}
