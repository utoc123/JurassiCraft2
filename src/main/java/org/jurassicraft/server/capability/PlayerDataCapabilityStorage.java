package org.jurassicraft.server.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerDataCapabilityStorage implements Capability.IStorage<PlayerDataCapability>
{
    @Override
    public NBTBase writeNBT(Capability<PlayerDataCapability> capability, PlayerDataCapability instance, EnumFacing side)
    {
        NBTTagCompound nbt = new NBTTagCompound();
        instance.save(nbt);
        return nbt;
    }

    @Override
    public void readNBT(Capability<PlayerDataCapability> capability, PlayerDataCapability instance, EnumFacing side, NBTBase nbt)
    {
        instance.load((NBTTagCompound) nbt);
    }
}
