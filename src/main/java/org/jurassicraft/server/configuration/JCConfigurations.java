package org.jurassicraft.server.configuration;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jurassicraft.JurassiCraft;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jabelar
 */
public class JCConfigurations
{
    private static boolean hasInitialized = false;
    private static Property spawnJurassiCraftMobsNaturally;
    private static Property spawnVanillaMobsNaturally;
    private static Property spawnModMobsNaturally;

    private static void checkInitialized()
    {
        if (!hasInitialized)
        {
            throw new IllegalStateException("Configuration not yet initialized.");
        }
    }

    public static boolean shouldSpawnJurassiCraftMobs()
    {
        checkInitialized();
        return spawnJurassiCraftMobsNaturally.getBoolean(false);
    }

    public static boolean shouldSpawnVanillaMobs()
    {
        checkInitialized();
        return spawnVanillaMobsNaturally.getBoolean(true);
    }

    public static boolean shouldSpawnModMobs()
    {
        checkInitialized();
        return spawnModMobsNaturally.getBoolean(true);
    }

    public static List<IConfigElement> getAllConfigurableOptions()
    {
        List<IConfigElement> configElements = new ArrayList<>();
        configElements.add(new ConfigElement(spawnJurassiCraftMobsNaturally));
        configElements.add(new ConfigElement(spawnVanillaMobsNaturally));
        configElements.add(new ConfigElement(spawnModMobsNaturally));
        return configElements;
    }

    public void initConfig(FMLPreInitializationEvent event)
    {
        JurassiCraft.CONFIG_FILE = event.getSuggestedConfigurationFile();
        JurassiCraft.INSTANCE.getLogger().debug(JurassiCraft.MODNAME + " config path = " + JurassiCraft.CONFIG_FILE.getAbsolutePath());
        JurassiCraft.INSTANCE.getLogger().debug("Config file exists = " + JurassiCraft.CONFIG_FILE.canRead());

        JurassiCraft.CONFIG = new Configuration(JurassiCraft.CONFIG_FILE);
        JurassiCraft.CONFIG.load();

        syncConfig();
        hasInitialized = true;
    }

    public void syncConfig()
    {
        spawnJurassiCraftMobsNaturally = JurassiCraft.CONFIG.get(Configuration.CATEGORY_GENERAL, "JurassiCraft Creatures Spawn Naturally", false, "Allow JurassiCraft entities to spawn naturally during world generation");
        spawnJurassiCraftMobsNaturally.getBoolean(false); // Init
        spawnJurassiCraftMobsNaturally.setRequiresMcRestart(true);
        spawnVanillaMobsNaturally = JurassiCraft.CONFIG.get(Configuration.CATEGORY_GENERAL, "Vanilla Mobs Spawn Naturally", true, "Allow vanilla mobs to spawn naturally during world generation");
        spawnVanillaMobsNaturally.getBoolean(true); // Init
        spawnVanillaMobsNaturally.setRequiresMcRestart(true);
        spawnModMobsNaturally = JurassiCraft.CONFIG.get(Configuration.CATEGORY_GENERAL, "Other Mods' Mobs Spawn Naturally", true, "Allow mobs from other mods to spawn naturally during world generation");
        spawnModMobsNaturally.getBoolean(true); // Init
        spawnModMobsNaturally.setRequiresMcRestart(true);

        if (JurassiCraft.CONFIG.hasChanged())
        {
            JurassiCraft.CONFIG.save();
        }
    }

    @SubscribeEvent
    public void onConfigChange(OnConfigChangedEvent event)
    {
        if (!event.getModID().equals(JurassiCraft.MODID))
        {
            return;
        }

        syncConfig();
    }
}
