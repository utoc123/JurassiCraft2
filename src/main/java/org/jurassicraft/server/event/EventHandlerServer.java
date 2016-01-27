package org.jurassicraft.server.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.jurassicraft.server.achievements.JCAchievements;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.data.JCPlayerData;
import org.jurassicraft.server.item.JCItemRegistry;

import java.util.Random;

public class EventHandlerServer
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
            player.addStat(JCAchievements.jurassicraft, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onItemPickup(PlayerEvent.ItemPickupEvent event)
    {
        if (event.pickedUp.getEntityItem().getItem() == JCItemRegistry.amber)
        {
            event.player.addStat(JCAchievements.amber, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onCraft(PlayerEvent.ItemCraftedEvent event)
    {
        Item item = event.crafting.getItem();

        if (item == JCItemRegistry.plaster_and_bandage)
        {
            event.player.addStat(JCAchievements.paleontology, 1);
        }
        else if (item == Item.getItemFromBlock(JCBlockRegistry.cleaning_station))
        {
            event.player.addStat(JCAchievements.cleaningStation, 1);
        }
        else if (item == Item.getItemFromBlock(JCBlockRegistry.fossil_grinder))
        {
            event.player.addStat(JCAchievements.fossilGrinder, 1);
        }
        else if (item == Item.getItemFromBlock(JCBlockRegistry.reinforced_stone))
        {
            event.player.addStat(JCAchievements.reinforcedStone, 1);
        }
        else if (item == Item.getItemFromBlock(JCBlockRegistry.reinforced_bricks))
        {
            event.player.addStat(JCAchievements.reinforcedStone, 1);
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void livingHurt(LivingHurtEvent event)
    {
        if (event.entityLiving instanceof DinosaurEntity)
        {
            DinosaurEntity dino = (DinosaurEntity) event.entityLiving;

            if (!dino.isCarcass() && dino.getHealth() - event.ammount <= 0)
            {
                event.setCanceled(true);
                event.ammount = 0;
                dino.setCarcass(true);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void decorate(DecorateBiomeEvent.Pre event)
    {
        World world = event.world;
        BlockPos pos = event.pos;
        Random rand = event.rand;

        BiomeGenBase biome = world.getBiomeGenForCoords(pos);

        if (biome == BiomeGenBase.forest || biome == BiomeGenBase.birchForest || biome == BiomeGenBase.taiga || biome == BiomeGenBase.megaTaiga || biome == BiomeGenBase.swampland || biome == BiomeGenBase.jungle)
        {
            if (rand.nextInt(8) == 0)
            {
                BlockPos topBlock = world.getTopSolidOrLiquidBlock(pos);
                IBlockState state = world.getBlockState(topBlock.down());

                if (state != null && state.getBlock().isOpaqueCube())
                {
                    world.setBlockState(topBlock, JCBlockRegistry.moss.getDefaultState(), 2);
                }
            }
        }
    }
}
