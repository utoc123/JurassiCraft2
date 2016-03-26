package org.jurassicraft.client.gui.app;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.paleopad.App;
import org.jurassicraft.server.paleopad.AppHandler;

import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiAppRegistry
{
    private static Map<App, GuiApp> registeredApps = new HashMap<App, GuiApp>();

    public static void registerApp(GuiApp gui)
    {
        registeredApps.put(gui.app, gui);
    }

    public static void register()
    {
        registerApp(new FlappyDinoGuiApp(AppHandler.INSTANCE.flappy_dino));
        registerApp(new MinimapGuiApp(AppHandler.INSTANCE.minimap));
    }

    public static GuiApp getGui(App app)
    {
        return registeredApps.get(app);
    }
}
