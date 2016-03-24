package org.jurassicraft.server.capability;

import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.server.paleopad.App;

import java.util.List;
import java.util.Map;

public interface PlayerDataCapability
{
    void load(NBTTagCompound nbt);

    void save(NBTTagCompound nbt);

    List<App> getOpenApps();

    Map<String, NBTTagCompound> getAppdata();

    void openApp(App app);

    void closeApp(App app);
}
