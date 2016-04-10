package org.jurassicraft.server.proxy;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.achievements.AchievementHandler;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.configuration.JCConfigurations;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.event.ServerEventHandler;
import org.jurassicraft.server.handler.JCGuiHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.item.bones.FossilItem;
import org.jurassicraft.server.paleopad.AppHandler;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.recipe.RecipeHandler;
import org.jurassicraft.server.storagedisc.StorageTypeHandler;
import org.jurassicraft.server.world.WorldGenerator;

import java.util.Map;

public class ServerProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        JurassiCraft.configurations.initConfig(event);

        EntityHandler.INSTANCE.init();
        FossilItem.init();
        PlantHandler.INSTANCE.init();
        TabHandler.INSTANCE.init();
        BlockHandler.INSTANCE.init();
        ItemHandler.INSTANCE.init();
        RecipeHandler.INSTANCE.init();
        AppHandler.INSTANCE.init();
        AchievementHandler.INSTANCE.init();
        StorageTypeHandler.INSTANCE.init();

        // addChestGenItems();

        GameRegistry.registerWorldGenerator(WorldGenerator.INSTANCE, 0);

        NetworkRegistry.INSTANCE.registerGuiHandler(JurassiCraft.INSTANCE, new JCGuiHandler());

        ServerEventHandler eventHandler = new ServerEventHandler();

        MinecraftForge.EVENT_BUS.register(JurassiCraft.configurations);
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

//    private void addChestGenItems()
//    {
//        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(ItemHandler.INSTANCE.amber, 1, 0), 1, 2, 30));
//        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(new ItemStack(ItemHandler.INSTANCE.amber, 1, 1), 1, 2, 30));
//
//        List<Dinosaur> dinosaurs = new ArrayList<Dinosaur>(EntityHandler.INSTANCE.getDinosaurs());
//
//        Map<Dinosaur, Integer> ids = new HashMap<Dinosaur, Integer>();
//
//        int id = 0;
//
//        for (Dinosaur dino : dinosaurs)
//        {
//            ids.put(dino, id);
//
//            id++;
//        }
//
//        Collections.sort(dinosaurs);
//
//        for (Dinosaur dino : dinosaurs)
//        {
//            if (dino.shouldRegister() && !(dino instanceof IHybrid))
//            {
//                ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(new ItemStack(ItemHandler.INSTANCE.skull, 1, ids.get(dino)), 1, 6, 80));
//            }
//        }
//    }

    public void postInit(FMLPostInitializationEvent event)
    {
        /*
         * Remove null entries from biomeList.
         */
        BiomeGenBase[] allBiomes = Iterators.toArray(Iterators.filter(Iterators.forArray(BiomeGenBase.getBiomeGenArray()), Predicates.notNull()), BiomeGenBase.class);

        for (Object object : EntityList.classToStringMapping.entrySet())
        {
            Map.Entry<Class, String> entry = (Map.Entry<Class, String>) object;

            Class entityClass = entry.getKey();
            if (entityClass == null)
            {
                continue; // Avoid potential NPE if entityClass is null
            }

            String clazzName = entityClass.toString();

            if (!clazzName.contains("jurassicraft"))
            {
                if (clazzName.contains("minecraft"))
                {
                    if (!JCConfigurations.spawnVanillaMobsNaturally())
                    {
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.AMBIENT, allBiomes);
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.CREATURE, allBiomes);
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.MONSTER, allBiomes);
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.WATER_CREATURE, allBiomes);
                    }
                }
                else
                {
                    if (!JCConfigurations.spawnOtherMobsModsNaturally())
                    {
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.AMBIENT, allBiomes);
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.CREATURE, allBiomes);
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.MONSTER, allBiomes);
                        EntityRegistry.removeSpawn(entityClass, EnumCreatureType.WATER_CREATURE, allBiomes);
                    }
                }
            }
        }
    }

    public void init(FMLInitializationEvent event)
    {

    }

    public EntityPlayer getPlayer()
    {
        return null;
    }

    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        return ctx.getServerHandler().playerEntity;
    }

    public void registerRenderSubBlock(Block block)
    {

    }

    public void scheduleTask(MessageContext ctx, Runnable runnable)
    {
        WorldServer worldObj = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        worldObj.addScheduledTask(runnable);
    }
}
