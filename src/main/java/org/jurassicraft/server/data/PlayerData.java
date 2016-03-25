package org.jurassicraft.server.data;

import net.ilexiconn.llibrary.server.capability.EntityDataHandler;
import net.ilexiconn.llibrary.server.capability.IEntityData;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import org.jurassicraft.server.paleopad.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerData implements IEntityData
{
    public static final String IDENTIFIER = "jurassicraft:playerdata";

    private Map<String, NBTTagCompound> appdata = new HashMap<String, NBTTagCompound>();
    private List<App> openApps = new ArrayList<App>();

    @Override
    public void writeToNBT(NBTTagCompound nbt)
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
    public void readFromNBT(NBTTagCompound nbt)
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
    public String getIdentifier()
    {
        return IDENTIFIER;
    }

    public List<App> getOpenApps()
    {
        return openApps;
    }

    public Map<String, NBTTagCompound> getAppdata()
    {
        return appdata;
    }

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

    public void closeApp(App app)
    {
        NBTTagCompound data = new NBTTagCompound();
        app.writeAppToNBT(data);

        appdata.put(app.getName(), data);
        openApps.remove(app);
    }

    public static PlayerData get(Entity entity)
    {
        return ((PlayerData) EntityDataHandler.getManager(entity, IDENTIFIER));
    }
}
