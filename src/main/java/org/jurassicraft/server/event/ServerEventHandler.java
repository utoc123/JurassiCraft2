package org.jurassicraft.server.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jurassicraft.server.achievements.AchievementHandler;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.entity.data.JCPlayerData;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.world.WorldGenCoal;

import java.util.Random;

public class ServerEventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void entityConstructing(EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            event.entity.registerExtendedProperties(JCPlayerData.identifier, new JCPlayerData());
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.entity instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.entity;
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
        World world = event.world;
        BlockPos pos = event.pos;
        Random rand = event.rand;

        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
        {
            if (biome != null)
            {
                BiomeDecorator decorator = biome.theBiomeDecorator;

                if (decorator != null && decorator.chunkProviderSettings != null && !(decorator.coalGen instanceof WorldGenCoal))
                {
                    decorator.coalGen = new WorldGenCoal(Blocks.coal_ore.getDefaultState(), decorator.chunkProviderSettings.coalSize);
                }
            }
        }

        BiomeGenBase biome = world.getBiomeGenForCoords(pos);

        if (biome == BiomeGenBase.forest || biome == BiomeGenBase.birchForest || biome == BiomeGenBase.taiga || biome == BiomeGenBase.megaTaiga || biome == BiomeGenBase.swampland || biome == BiomeGenBase.jungle)
        {
            if (rand.nextInt(8) == 0)
            {
                BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                IBlockState state = world.getBlockState(topBlock.down());

                if (state != null && state.getBlock().isOpaqueCube())
                {
                    world.setBlockState(topBlock, BlockHandler.INSTANCE.moss.getDefaultState(), 2);
                }
            }
        }
    }
}
