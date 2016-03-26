package org.jurassicraft.server.paleopad;

import java.util.ArrayList;
import java.util.List;

public class AppRegistry
{
    private static List<App> registeredApps = new ArrayList<App>();
    public static App flappy_dino;
    public static App minimap;

    private static void registerApp(App app)
    {
        registeredApps.add(app);
    }

    public static void init()
    {
        flappy_dino = new FlappyDinoApp();
        minimap = new MinimapApp();

        registerApp(flappy_dino);
        registerApp(minimap);
    }

    public static List<App> getApps()
    {
        return registeredApps;
    }
}
