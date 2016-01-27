package org.jurassicraft;

import net.ilexiconn.llibrary.common.book.BookWiki;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;
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
import org.jurassicraft.server.message.RequestFileMessage;
import org.jurassicraft.server.message.SendFileMessage;
import org.jurassicraft.server.message.SyncPaleoPadMessage;
import org.jurassicraft.server.proxy.ProxyServer;
import org.jurassicraft.server.world.islanublar.IslaNublarWorldType;

import java.io.File;
import java.io.InputStreamReader;

@Mod(modid = JurassiCraft.MODID, name = JurassiCraft.MODNAME, version = JurassiCraft.VERSION, guiFactory = "org.jurassicraft.client.gui.config.GUIFactory", dependencies = "required-after:llibrary@[0.8.1,)")
public class JurassiCraft
{
    @SidedProxy(serverSide = "org.jurassicraft.server.proxy.ProxyServer", clientSide = "org.jurassicraft.client.proxy.ProxyClient")
    public static ProxyServer proxy;

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

    public static IslaNublarWorldType worldTypeIslaNublar = new IslaNublarWorldType();

    // set up configuration properties (will be read from config file in preInit)
    public static File configFile;
    public static Configuration config;

    public static BookWiki bookWiki;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        logger.info("Loading JurassiCraft...");

        int id = 0;
        networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("jurassicraft");
        AbstractMessage.registerMessage(networkWrapper, SyncPaleoPadMessage.class, id++, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, SyncPaleoPadMessage.class, id++, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, RequestFileMessage.class, id++, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, RequestFileMessage.class, id++, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, SendFileMessage.class, id++, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, SendFileMessage.class, id++, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, PlacePaddockSignMessage.class, id++, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, ChangeTemperatureMessage.class, id++, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, ChangeTemperatureMessage.class, id++, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, HelicopterEngineMessage.class, id++, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, HelicopterEngineMessage.class, id++, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, HelicopterDirectionMessage.class, id++, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, HelicopterDirectionMessage.class, id++, Side.SERVER);
        AbstractMessage.registerMessage(networkWrapper, HelicopterModulesMessage.class, id++, Side.CLIENT);
        AbstractMessage.registerMessage(networkWrapper, HelicopterModulesMessage.class, id++, Side.SERVER);

        proxy.preInit(event);
        logger.debug("Finished pre-initialization for JurassiCraft!");

        FoodHelper.init();

        bookWiki = BookWiki.create(instance, new InputStreamReader(JurassiCraft.class.getResourceAsStream("/assets/jurassicraft/bookwiki/bookwiki.json")));
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

    @Mod.EventHandler
    public void serverStart(FMLServerStartedEvent event)
    {
        GameRules gameRules = MinecraftServer.getServer().worldServerForDimension(0).getGameRules();

        if (!gameRules.hasRule("dinoMetabolism"))
        {
            gameRules.addGameRule("dinoMetabolism", "true", GameRules.ValueType.BOOLEAN_VALUE);
        }

        if (!gameRules.hasRule("dinoGrowth"))
        {
            gameRules.addGameRule("dinoGrowth", "true", GameRules.ValueType.BOOLEAN_VALUE);
        }

        if (!gameRules.hasRule("dinoHerding"))
        {
            gameRules.addGameRule("dinoHerding", "false", GameRules.ValueType.BOOLEAN_VALUE);
        }
    }

    public Logger getLogger()
    {
        return logger;
    }
}
