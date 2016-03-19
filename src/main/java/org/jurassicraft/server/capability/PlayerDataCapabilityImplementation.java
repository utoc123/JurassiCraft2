package org.jurassicraft.server.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.paleopad.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDataCapabilityImplementation implements PlayerDataCapability
{
    private Map<String, NBTTagCompound> appdata = new HashMap<String, NBTTagCompound>();
    private List<App> openApps = new ArrayList<App>();

    @Override
    public void load(NBTTagCompound nbt)
    {
        appdata.clear();

        NBTTagList appDataList = nbt.getTagList("JCAppData", 10);

        for (int i = 0; i < appDataList.tagCount(); i++)
        {
            NBTTagCompound appData = (NBTTagCompound) appDataList.get(i);

            this.appdata.put(appData.getString("Name"), appData.getCompoundTag("Data"));
        }
    }

    @Override
    public void save(NBTTagCompound nbt)
    {
        NBTTagList appDataList = new NBTTagList();

        for (Map.Entry<String, NBTTagCompound> data : appdata.entrySet())
        {
            NBTTagCompound appData = new NBTTagCompound();

            appData.setString("Name", data.getKey());
            appData.setTag("Data", data.getValue());

            appDataList.appendTag(appData);
        }

        nbt.setTag("JCAppData", appDataList);
    }

    @Override
    public List<App> getOpenApps()
    {
        return openApps;
    }

    @Override
    public Map<String, NBTTagCompound> getAppdata()
    {
        return appdata;
    }

    @Override
    public void openApp(App app)
    {
        if (appdata.containsKey(app.getName()))
        {
            app.readAppFromNBT(appdata.get(app.getName()));
        }

        app.init();
        app.open();

        openApps.add(app);
    }

    @Override
    public void closeApp(App app)
    {
        NBTTagCompound data = new NBTTagCompound();
        app.writeAppToNBT(data);

        appdata.put(app.getName(), data);
        openApps.remove(app);
    }

    public static PlayerDataCapability get(EntityPlayer player)
    {
        return player.getCapability(JurassiCraft.PLAYER_DATA_CAPABILITY, null);
    }
}
