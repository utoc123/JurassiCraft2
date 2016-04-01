package org.jurassicraft.server.event;

import net.ilexiconn.llibrary.server.capability.EntityDataHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jurassicraft.server.achievements.AchievementHandler;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.data.PlayerData;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.world.WorldGenCoal;

import java.util.Random;

public class ServerEventHandler
{
    @SubscribeEvent
    public void onEntityConstruct(EntityEvent.EntityConstructing event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityDataHandler.INSTANCE.registerExtendedEntityData(event.getEntity(), new PlayerData());
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        GameRules gameRules = event.getWorld().getGameRules();

        registerGameRule(gameRules, "dinoMetabolism", true);
        registerGameRule(gameRules, "dinoGrowth", true);
        registerGameRule(gameRules, "dinoHerding", false);
    }

    private void registerGameRule(GameRules gameRules, String name, boolean value)
    {
        if (!gameRules.hasRule(name))
        {
            gameRules.addGameRule(name, value + "", GameRules.ValueType.BOOLEAN_VALUE);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            player.addStat(AchievementHandler.INSTANCE.jurassicraft, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        if (event.pickedUp.getEntityItem().getItem() == ItemHandler.INSTANCE.amber)
        {
            event.player.addStat(AchievementHandler.INSTANCE.amber, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onCraft(PlayerEvent.ItemCraftedEvent event)
    {
        Item item = event.crafting.getItem();

        if (item == ItemHandler.INSTANCE.plaster_and_bandage)
        {
            event.player.addStat(AchievementHandler.INSTANCE.paleontology, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.cleaning_station))
        {
            event.player.addStat(AchievementHandler.INSTANCE.cleaningStation, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.fossil_grinder))
        {
            event.player.addStat(AchievementHandler.INSTANCE.fossilGrinder, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.reinforced_stone))
        {
            event.player.addStat(AchievementHandler.INSTANCE.reinforcedStone, 1);
        }
        else if (item == Item.getItemFromBlock(BlockHandler.INSTANCE.reinforced_bricks))
        {
            event.player.addStat(AchievementHandler.INSTANCE.reinforcedStone, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void decorate(DecorateBiomeEvent.Pre event)
    {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        Random rand = event.getRand();

        for (BiomeGenBase biome : BiomeGenBase.biomeRegistry)
        {
            BiomeDecorator decorator = biome.theBiomeDecorator;

            if (decorator != null && decorator.chunkProviderSettings != null && !(decorator.coalGen instanceof WorldGenCoal))
            {
                decorator.coalGen = new WorldGenCoal(Blocks.coal_ore.getDefaultState(), decorator.chunkProviderSettings.coalSize);
            }
        }

        BiomeGenBase biome = world.getBiomeGenForCoords(pos);

        if (biome == Biomes.forest || biome == Biomes.birchForest || biome == Biomes.taiga || biome == Biomes.megaTaiga || biome == Biomes.swampland || biome == Biomes.jungle)
        {
            if (rand.nextInt(8) == 0)
            {
                BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                IBlockState state = world.getBlockState(topBlock.down());

                if (state != null && state.getBlock().isOpaqueCube(state))
                {
                    world.setBlockState(topBlock, BlockHandler.INSTANCE.moss.getDefaultState(), 2);
                }
            }
        }
    }
}
