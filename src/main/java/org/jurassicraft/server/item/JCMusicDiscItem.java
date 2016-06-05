package org.jurassicraft.server.item;

import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import org.jurassicraft.JurassiCraft;

public class JCMusicDiscItem extends ItemRecord
{
    public JCMusicDiscItem(String name)
    {
        super(name, new SoundEvent(new ResourceLocation(JurassiCraft.MODID, name)));
    }
}