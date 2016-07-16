package org.jurassicraft.server.achievements;

import net.minecraftforge.common.AchievementPage;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.item.ItemHandler;

public class AchievementHandler
{
    public static final JCAchievement PALEONTOLOGY = (JCAchievement) new JCAchievement("paleontology", 2, 1, ItemHandler.PLASTER_AND_BANDAGE, null).initIndependentStat();
    public static final JCAchievement FOSSILS = new JCAchievement("fossils", 3, 3, BlockHandler.ENCASED_FOSSILS.get(0), PALEONTOLOGY);
    public static final JCAchievement AMBER = new JCAchievement("amber", 2, -2, ItemHandler.AMBER, PALEONTOLOGY);
    public static final JCAchievement CLEANING_STATION = new JCAchievement("cleaningStation", -1, 2, BlockHandler.CLEANING_STATION, PALEONTOLOGY);
    public static final JCAchievement FOSSIL_GRINDER = new JCAchievement("fossilGrinder", -2, -1, BlockHandler.FOSSIL_GRINDER, PALEONTOLOGY);
    public static final JCAchievement REINFORCED_STONE = new JCAchievement("reinforcedStone", 4, -1, BlockHandler.REINFORCED_STONE, PALEONTOLOGY);

    public static final AchievementPage JURASSICRAFT_PAGE = new AchievementPage("JurassiCraft", PALEONTOLOGY, FOSSILS, AMBER, CLEANING_STATION, FOSSIL_GRINDER, REINFORCED_STONE);

    public static void init()
    {
        AchievementPage.registerAchievementPage(JURASSICRAFT_PAGE);
    }
}
