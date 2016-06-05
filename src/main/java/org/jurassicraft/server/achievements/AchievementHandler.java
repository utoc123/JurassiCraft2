package org.jurassicraft.server.achievements;

import net.minecraftforge.common.AchievementPage;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.item.ItemHandler;

public enum AchievementHandler
{
    INSTANCE;

    public JCAchievement jurassicraft;
    public JCAchievement fossils;
    public JCAchievement paleontology;
    public JCAchievement amber;
    public JCAchievement cleaningStation;
    public JCAchievement fossilGrinder;
    public JCAchievement reinforcedStone;

    public AchievementPage jurassicraftPage;

    public void init()
    {
        jurassicraft = (JCAchievement) (new JCAchievement("mod", 0, 0, ItemHandler.INSTANCE.FOSSILS.get("skull"), null)).initIndependentStat();
        paleontology = new JCAchievement("paleontology", 2, 1, ItemHandler.INSTANCE.PLASTER_AND_BANDAGE, jurassicraft);
        fossils = new JCAchievement("fossils", 3, 3, BlockHandler.INSTANCE.ENCASED_FOSSILS.get(0), paleontology);
        amber = new JCAchievement("amber", 2, -2, ItemHandler.INSTANCE.AMBER, jurassicraft);
        cleaningStation = new JCAchievement("cleaningStation", -1, 2, BlockHandler.INSTANCE.CLEANING_STATION, jurassicraft);
        fossilGrinder = new JCAchievement("fossilGrinder", -2, -1, BlockHandler.INSTANCE.FOSSIL_GRINDER, jurassicraft);
        reinforcedStone = new JCAchievement("reinforcedStone", 4, -1, BlockHandler.INSTANCE.REINFORCED_STONE, jurassicraft);

        jurassicraftPage = new AchievementPage("JurassiCraft", jurassicraft, paleontology, fossils, amber, cleaningStation, fossilGrinder, reinforcedStone);

        AchievementPage.registerAchievementPage(jurassicraftPage);
    }
}
