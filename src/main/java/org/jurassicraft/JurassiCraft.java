package org.jurassicraft;

import net.minecraft.world.GameRules;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import org.jurassicraft.client.animation.CommandForceAnimation;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.configuration.JCConfigurations;
import org.jurassicraft.server.food.FoodHelper;
import org.jurassicraft.server.message.ChangeTemperatureMessage;
import org.jurassicraft.server.message.HelicopterDirectionMessage;
import org.jurassicraft.server.message.HelicopterEngineMessage;
import org.jurassicraft.server.message.HelicopterModulesMessage;
import org.jurassicraft.server.message.PlacePaddockSignMessage;
import org.jurassicraft.server.message.SwitchHybridizerCombinatorMode;
import org.jurassicraft.server.message.SyncPaleoPadMessage;
import org.jurassicraft.server.proxy.ServerProxy;

import java.io.File;

@Mod(modid = JurassiCraft.MODID, name = JurassiCraft.MODNAME, version = JurassiCraft.VERSION, guiFactory = "org.jurassicraft.client.gui.config.GUIFactory", dependencies = "required-after:llibrary@[0.0.0-develop,)")
public class JurassiCraft
{
    @SidedProxy(serverSide = "org.jurassicraft.server.proxy.ServerProxy", clientSide = "org.jurassicraft.client.proxy.ClientProxy")
    public static ServerProxy proxy;

    public static final String MODID = "jurassicraft";
    public static final String MODNAME = "JurassiCraft";
    public static final String VERSION = "2.0.0-pre";

    @Instance(JurassiCraft.MODID)
    public static JurassiCraft instance;
    public static long timerTicks;
    public static SimpleNetworkWrapper networkWrapper;

    private Logger logger;

    public static JCBlockRegistry blockRegistry;
    public static JCConfigurations configurations = new JCConfigurations();

    // set up configuration properties (will be read from config file in preInit)
    public static File configFile;
    public static Configuration config;

//    public static BookWiki bookWiki;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("Loading JurassiCraft...");

        int id = 0;
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("jurassicraft");
        networkWrapper.registerMessage(SyncPaleoPadMessage.class, SyncPaleoPadMessage.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(SyncPaleoPadMessage.class, SyncPaleoPadMessage.class, id++, Side.SERVER);
        networkWrapper.registerMessage(PlacePaddockSignMessage.class, PlacePaddockSignMessage.class, id++, Side.SERVER);
        networkWrapper.registerMessage(ChangeTemperatureMessage.class, ChangeTemperatureMessage.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(ChangeTemperatureMessage.class, ChangeTemperatureMessage.class, id++, Side.SERVER);
        networkWrapper.registerMessage(HelicopterEngineMessage.class, HelicopterEngineMessage.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(HelicopterEngineMessage.class, HelicopterEngineMessage.class, id++, Side.SERVER);
        networkWrapper.registerMessage(HelicopterDirectionMessage.class, HelicopterDirectionMessage.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(HelicopterDirectionMessage.class, HelicopterDirectionMessage.class, id++, Side.SERVER);
        networkWrapper.registerMessage(HelicopterModulesMessage.class, HelicopterModulesMessage.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(HelicopterModulesMessage.class, HelicopterModulesMessage.class, id++, Side.SERVER);
        networkWrapper.registerMessage(SwitchHybridizerCombinatorMode.class, SwitchHybridizerCombinatorMode.class, id++, Side.CLIENT);
        networkWrapper.registerMessage(SwitchHybridizerCombinatorMode.class, SwitchHybridizerCombinatorMode.class, id++, Side.SERVER);

        proxy.preInit(event);
        logger.debug("Finished pre-initialization for JurassiCraft!");

        FoodHelper.init();

//        bookWiki = BookWiki.create(instance, new InputStreamReader(JurassiCraft.class.getResourceAsStream("/assets/jurassicraft/bookwiki/bookwiki.json")));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        logger.debug("Finished initialization for JurassiCraft");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
        logger.info("Finished loading JurassiCraft");
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandForceAnimation());
    }

    public Logger getLogger()
    {
        return logger;
    }
}
