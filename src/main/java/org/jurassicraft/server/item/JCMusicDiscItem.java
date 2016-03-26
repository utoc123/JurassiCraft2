package org.jurassicraft.server.item;

import net.minecraft.item.ItemRecord;
import org.jurassicraft.JurassiCraft;

public class JCMusicDiscItem extends ItemRecord
{
    public JCMusicDiscItem(String name)
    {
        super(name);
    }

    /**
     * Retrieves the resource location of the sound to play for this record.
     *
     * @param name The name of the record to play
     * @return The resource location for the audio, null to use default.
     */
    @Override
    public net.minecraft.util.ResourceLocation getRecordResource(String name)
    {
        return new net.minecraft.util.ResourceLocation(JurassiCraft.MODID, name);
    }
}