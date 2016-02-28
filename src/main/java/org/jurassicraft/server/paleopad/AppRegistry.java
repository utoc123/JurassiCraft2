package org.jurassicraft.server.paleopad;

import net.ilexiconn.llibrary.common.content.IContentHandler;

import java.util.ArrayList;
import java.util.List;

public class AppRegistry implements IContentHandler
{
    private static List<App> registeredApps = new ArrayList<App>();
    public static App flappy_dino;
    public static App minimap;

    public void registerApp(App app)
    {
        registeredApps.add(app);
    }

    @Override
    public void init()
    {
        flappy_dino = new FlappyDinoApp();
        minimap = new MinimapApp();
    }

    @Override
    public void gameRegistry() throws Exception
    {
        registerApp(flappy_dino);
        registerApp(minimap);
    }

    public static List<App> getApps()
    {
        return registeredApps;
    }
}
