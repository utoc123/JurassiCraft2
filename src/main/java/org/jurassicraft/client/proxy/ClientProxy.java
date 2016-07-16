package org.jurassicraft.client.proxy;

import net.ilexiconn.llibrary.client.lang.LanguageHandler;
import net.ilexiconn.llibrary.server.util.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.event.ClientEventHandler;
import org.jurassicraft.client.gui.CleaningStationGui;
import org.jurassicraft.client.gui.CultivateGui;
import org.jurassicraft.client.gui.CultivateProcessGui;
import org.jurassicraft.client.gui.DNACombinatorHybridizerGui;
import org.jurassicraft.client.gui.DNAExtractorGui;
import org.jurassicraft.client.gui.DNASequencerGui;
import org.jurassicraft.client.gui.DNASynthesizerGui;
import org.jurassicraft.client.gui.EmbryoCalcificationMachineGui;
import org.jurassicraft.client.gui.EmbryonicMachineGui;
import org.jurassicraft.client.gui.FeederGui;
import org.jurassicraft.client.gui.FieldGuideGui;
import org.jurassicraft.client.gui.FossilGrinderGui;
import org.jurassicraft.client.gui.IncubatorGui;
import org.jurassicraft.client.gui.OrderDinosaurGui;
import org.jurassicraft.client.gui.SelectDinoGui;
import org.jurassicraft.client.render.RenderingHandler;
import org.jurassicraft.server.entity.VenomEntity;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.particle.VenomParticle;
import org.jurassicraft.server.entity.vehicle.CarEntity;
import org.jurassicraft.server.proxy.ServerProxy;
import org.jurassicraft.server.tile.CleaningStationTile;
import org.jurassicraft.server.tile.CultivatorTile;
import org.jurassicraft.server.tile.DNACombinatorHybridizerTile;
import org.jurassicraft.server.tile.DNAExtractorTile;
import org.jurassicraft.server.tile.DNASequencerTile;
import org.jurassicraft.server.tile.DNASynthesizerTile;
import org.jurassicraft.server.tile.EmbryoCalcificationMachineTile;
import org.jurassicraft.server.tile.EmbryonicMachineTile;
import org.jurassicraft.server.tile.FeederTile;
import org.jurassicraft.server.tile.FossilGrinderTile;
import org.jurassicraft.server.tile.IncubatorTile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    private static final Minecraft MC = Minecraft.getMinecraft();

    public static final List<UUID> PATRONS = new ArrayList<>();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

        try
        {
            LanguageHandler.INSTANCE.loadRemoteLocalization(JurassiCraft.MODID);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ClientEventHandler eventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);

        RenderingHandler.INSTANCE.preInit();
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);

        RenderingHandler.INSTANCE.init();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);

        RenderingHandler.INSTANCE.postInit();

        new Thread(() ->
        {
            List<String> patrons = WebUtils.readPastebinAsList("fgJQkCMa");

            if (patrons != null)
            {
                for (String patron : patrons)
                {
                    PATRONS.add(UUID.fromString(patron));
                }
            }
        }).start();
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return MC.thePlayer;
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx)
    {
        return (ctx.side.isClient() ? getPlayer() : super.getPlayerEntityFromContext(ctx));
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null)
        {
            if (tileEntity instanceof CleaningStationTile && id == GUI_CLEANING_STATION_ID)
            {
                return new CleaningStationGui(player.inventory, (CleaningStationTile) tileEntity);
            }
            else if (tileEntity instanceof FossilGrinderTile && id == GUI_FOSSIL_GRINDER_ID)
            {
                return new FossilGrinderGui(player.inventory, (FossilGrinderTile) tileEntity);
            }
            else if (tileEntity instanceof DNASequencerTile && id == GUI_DNA_SEQUENCER_ID)
            {
                return new DNASequencerGui(player.inventory, (DNASequencerTile) tileEntity);
            }
            else if (tileEntity instanceof EmbryonicMachineTile && id == GUI_EMBRYONIC_MACHINE_ID)
            {
                return new EmbryonicMachineGui(player.inventory, (EmbryonicMachineTile) tileEntity);
            }
            else if (tileEntity instanceof EmbryoCalcificationMachineTile && id == GUI_EMBRYO_CALCIFICATION_MACHINE_ID)
            {
                return new EmbryoCalcificationMachineGui(player.inventory, (EmbryoCalcificationMachineTile) tileEntity);
            }
            else if (tileEntity instanceof DNASynthesizerTile && id == GUI_DNA_SYNTHESIZER_ID)
            {
                return new DNASynthesizerGui(player.inventory, (DNASynthesizerTile) tileEntity);
            }
            else if (tileEntity instanceof IncubatorTile && id == GUI_INCUBATOR_ID)
            {
                return new IncubatorGui(player.inventory, (IncubatorTile) tileEntity);
            }
            else if (tileEntity instanceof DNACombinatorHybridizerTile && id == GUI_DNA_COMBINATOR_HYBRIDIZER_ID)
            {
                return new DNACombinatorHybridizerGui(player.inventory, (DNACombinatorHybridizerTile) tileEntity);
            }
            else if (tileEntity instanceof DNAExtractorTile && id == GUI_DNA_EXTRACTOR_ID)
            {
                return new DNAExtractorGui(player.inventory, (DNAExtractorTile) tileEntity);
            }
            else if (tileEntity instanceof CultivatorTile && id == GUI_CULTIVATOR_ID)
            {
                CultivatorTile cultivator = (CultivatorTile) tileEntity;

                if (cultivator.isProcessing(0))
                {
                    return new CultivateProcessGui(cultivator);
                }
                else
                {
                    return new CultivateGui(player.inventory, cultivator);
                }
            }
            else if (tileEntity instanceof FeederTile && id == GUI_FEEDER_ID)
            {
                return new FeederGui(player.inventory, (FeederTile) tileEntity);
            }
        }

        return null;
    }

    @Override
    public void openSelectDino(BlockPos pos, EnumFacing facing, EnumHand hand)
    {
        MC.displayGuiScreen(new SelectDinoGui(pos, facing, hand));
    }

    @Override
    public void openOrderGui(DinosaurEntity entity)
    {
        MC.displayGuiScreen(new OrderDinosaurGui(entity));
    }

    @Override
    public void openFieldGuide(DinosaurEntity entity)
    {
        MC.displayGuiScreen(new FieldGuideGui(entity));
    }

    public static void playSound(ISound sound)
    {
        MC.getSoundHandler().playSound(sound);
    }

    public static void stopSound(ISound sound)
    {
        MC.getSoundHandler().stopSound(sound);
    }

    public static void playSound(CarEntity entity)
    {
        playSound(entity.sound);
    }

    public static void stopSound(CarEntity entity)
    {
        stopSound(entity.sound);
    }

    public static void spawnVenomParticles(VenomEntity entity)
    {
        ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;

        float size = 0.35F;

        for (int i = 0; i < 16; ++i)
        {
            particleManager.addEffect(new VenomParticle(entity.worldObj, size * Math.random() - size / 2, size * Math.random() - size / 2, size * Math.random() - size / 2, 0.0F, 0.0F, 0.0F, 1.0F, entity));
        }
    }
}
