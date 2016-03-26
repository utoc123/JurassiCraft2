package org.jurassicraft.server.paleopad;

import java.util.ArrayList;
import java.util.List;

public enum AppHandler
{
    INSTANCE;

    private List<App> registeredApps = new ArrayList<App>();
    public App flappy_dino;
    public App minimap;

    private void registerApp(App app)
    {
        registeredApps.add(app);
    }

    public void init()
    {
        flappy_dino = new FlappyDinoApp();
        minimap = new MinimapApp();

        registerApp(flappy_dino);
        registerApp(minimap);
    }

    public List<App> getApps()
    {
        return registeredApps;
    }
}
