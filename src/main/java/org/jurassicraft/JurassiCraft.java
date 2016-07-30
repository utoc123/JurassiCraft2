package org.jurassicraft;

import net.ilexiconn.llibrary.server.config.Config;
import net.ilexiconn.llibrary.server.network.NetworkWrapper;
import net.ilexiconn.llibrary.server.update.UpdateHandler;
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
import org.jurassicraft.server.config.JurassiCraftConfig;
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

@Mod(modid = JurassiCraft.MODID, name = JurassiCraft.NAME, version = JurassiCraft.VERSION, guiFactory = "org.jurassicraft.client.gui.JurassiCraftGUIFactory", dependencies = "required-after:llibrary@[" + JurassiCraft.LLIBRARY_VERSION + ",)")
public class JurassiCraft {
    public static final String MODID = "jurassicraft";
    public static final String NAME = "JurassiCraft";
    public static final String VERSION = "2.0.1";

    public static final String LLIBRARY_VERSION = "1.5.1";
    @SidedProxy(serverSide = "org.jurassicraft.server.proxy.ServerProxy", clientSide = "org.jurassicraft.client.proxy.ClientProxy")
    public static ServerProxy PROXY;

    @Instance(JurassiCraft.MODID)
    public static JurassiCraft INSTANCE;

    @Config
    public static JurassiCraftConfig CONFIG;

    public static long timerTicks;

    @NetworkWrapper({ PlacePaddockSignMessage.class, ChangeTemperatureMessage.class, HelicopterEngineMessage.class, HelicopterDirectionMessage.class, HelicopterModulesMessage.class, SwitchHybridizerCombinatorMode.class, SetOrderMessage.class, OpenFieldGuideGuiMessage.class, UpdateCarControlMessage.class })
    public static SimpleNetworkWrapper NETWORK_WRAPPER;

    private Logger LOGGER;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.LOGGER = event.getModLog();

        UpdateHandler.INSTANCE.registerUpdateChecker(this, "http://pastebin.com/raw/Rb96SNWb");

        PROXY.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        PROXY.postInit(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ForceAnimationCommand());
    }

    public Logger getLogger() {
        return this.LOGGER;
    }
}
