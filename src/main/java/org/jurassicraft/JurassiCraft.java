package org.jurassicraft;

import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.server.animation.ForceAnimationCommand;
import org.jurassicraft.server.configuration.JCConfigurations;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.message.ChangeTemperatureMessage;
import org.jurassicraft.server.message.HelicopterDirectionMessage;
import org.jurassicraft.server.message.HelicopterEngineMessage;
import org.jurassicraft.server.message.HelicopterModulesMessage;
import org.jurassicraft.server.message.OpenFieldGuideGuiMessage;
import org.jurassicraft.server.message.PlacePaddockSignMessage;
import org.jurassicraft.server.message.SetOrderMessage;
import org.jurassicraft.server.message.SwitchHybridizerCombinatorMode;
import org.jurassicraft.server.message.UpdateCarControlMessage;
import org.jurassicraft.server.proxy.ServerProxy;

import java.io.File;

@Mod(modid = JurassiCraft.MODID, name = JurassiCraft.MODNAME, version = JurassiCraft.VERSION, guiFactory = "org.jurassicraft.client.gui.config.GUIFactory", dependencies = "required-after:llibrary@[" + JurassiCraft.LLIBRARY_VERSION + ",)")
public class JurassiCraft
{
    @SidedProxy(serverSide = "org.jurassicraft.server.proxy.ServerProxy", clientSide = "org.jurassicraft.client.proxy.ClientProxy")
    public static ServerProxy PROXY;

    public static final String MODID = "jurassicraft";
    public static final String MODNAME = "JurassiCraft";
    public static final String VERSION = "2.0.0-dev";
    public static final String LLIBRARY_VERSION = "1.4.0";

    @Instance(JurassiCraft.MODID)
    public static JurassiCraft INSTANCE;

    public static long timerTicks;

    @NetworkWrapper({ PlacePaddockSignMessage.class, ChangeTemperatureMessage.class, HelicopterEngineMessage.class, HelicopterDirectionMessage.class, HelicopterModulesMessage.class, SwitchHybridizerCombinatorMode.class, SetOrderMessage.class, OpenFieldGuideGuiMessage.class, UpdateCarControlMessage.class })
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    private Logger LOGGER;

    public static JCConfigurations configurations = new JCConfigurations();

    public static File configFile;
    public static Configuration CONFIG;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        LOGGER.info("Loading JurassiCraft...");

        PROXY.preInit(event);
        LOGGER.debug("Finished pre-initialization for JurassiCraft!");

        FoodHelper.INSTANCE.init();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        PROXY.init(event);
        LOGGER.debug("Finished initialization for JurassiCraft");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        PROXY.postInit(event);
        LOGGER.info("Finished loading JurassiCraft");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new ForceAnimationCommand());
    }

    public Logger getLogger()
    {
        return LOGGER;
    }
}
