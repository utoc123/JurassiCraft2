package org.jurassicraft.server.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
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
import org.jurassicraft.server.container.CleaningStationContainer;
import org.jurassicraft.server.container.CultivateContainer;
import org.jurassicraft.server.container.DNACombinatorHybridizerContainer;
import org.jurassicraft.server.container.DNAExtractorContainer;
import org.jurassicraft.server.container.DNASequencerContainer;
import org.jurassicraft.server.container.DNASynthesizerContainer;
import org.jurassicraft.server.container.EmbryoCalcificationMachineContainer;
import org.jurassicraft.server.container.EmbryonicMachineContainer;
import org.jurassicraft.server.container.FeederContainer;
import org.jurassicraft.server.container.FossilGrinderContainer;
import org.jurassicraft.server.container.IncubatorContainer;
import org.jurassicraft.server.entity.base.DinosaurEntity;
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

public class GuiHandler implements IGuiHandler
{
    public static final int CLEANING_STATION_ID = 0;
    public static final int FOSSIL_GRINDER_ID = 1;
    public static final int DNA_SEQUENCER_ID = 2;
    public static final int EMBRYONIC_MACHINE_ID = 3;
    public static final int EMBRYO_CALCIFICATION_MACHINE_ID = 4;
    public static final int DNA_SYNTHESIZER_ID = 5;
    public static final int INCUBATOR_ID = 6;
    public static final int DNA_COMBINATOR_HYBRIDIZER_ID = 7;
    public static final int DNA_EXTRACTOR_ID = 8;
    public static final int CULTIVATOR_ID = 9;
    public static final int FEEDER_ID = 10;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null)
        {
            if (tileEntity instanceof CleaningStationTile && id == CLEANING_STATION_ID)
            {
                return new CleaningStationContainer(player.inventory, (CleaningStationTile) tileEntity);
            }
            else if (tileEntity instanceof FossilGrinderTile && id == FOSSIL_GRINDER_ID)
            {
                return new FossilGrinderContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNASequencerTile && id == DNA_SEQUENCER_ID)
            {
                return new DNASequencerContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof EmbryonicMachineTile && id == EMBRYONIC_MACHINE_ID)
            {
                return new EmbryonicMachineContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof EmbryoCalcificationMachineTile && id == EMBRYO_CALCIFICATION_MACHINE_ID)
            {
                return new EmbryoCalcificationMachineContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNASynthesizerTile && id == DNA_SYNTHESIZER_ID)
            {
                return new DNASynthesizerContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof IncubatorTile && id == INCUBATOR_ID)
            {
                return new IncubatorContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNACombinatorHybridizerTile && id == DNA_COMBINATOR_HYBRIDIZER_ID)
            {
                return new DNACombinatorHybridizerContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNAExtractorTile && id == DNA_EXTRACTOR_ID)
            {
                return new DNAExtractorContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof CultivatorTile && id == CULTIVATOR_ID)
            {
                return new CultivateContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof FeederTile && id == FEEDER_ID)
            {
                return new FeederContainer(player.inventory, (FeederTile) tileEntity);
            }
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null)
        {
            if (tileEntity instanceof CleaningStationTile && id == CLEANING_STATION_ID)
            {
                return new CleaningStationGui(player.inventory, (CleaningStationTile) tileEntity);
            }
            else if (tileEntity instanceof FossilGrinderTile && id == FOSSIL_GRINDER_ID)
            {
                return new FossilGrinderGui(player.inventory, (FossilGrinderTile) tileEntity);
            }
            else if (tileEntity instanceof DNASequencerTile && id == DNA_SEQUENCER_ID)
            {
                return new DNASequencerGui(player.inventory, (DNASequencerTile) tileEntity);
            }
            else if (tileEntity instanceof EmbryonicMachineTile && id == EMBRYONIC_MACHINE_ID)
            {
                return new EmbryonicMachineGui(player.inventory, (EmbryonicMachineTile) tileEntity);
            }
            else if (tileEntity instanceof EmbryoCalcificationMachineTile && id == EMBRYO_CALCIFICATION_MACHINE_ID)
            {
                return new EmbryoCalcificationMachineGui(player.inventory, (EmbryoCalcificationMachineTile) tileEntity);
            }
            else if (tileEntity instanceof DNASynthesizerTile && id == DNA_SYNTHESIZER_ID)
            {
                return new DNASynthesizerGui(player.inventory, (DNASynthesizerTile) tileEntity);
            }
            else if (tileEntity instanceof IncubatorTile && id == INCUBATOR_ID)
            {
                return new IncubatorGui(player.inventory, (IncubatorTile) tileEntity);
            }
            else if (tileEntity instanceof DNACombinatorHybridizerTile && id == DNA_COMBINATOR_HYBRIDIZER_ID)
            {
                return new DNACombinatorHybridizerGui(player.inventory, (DNACombinatorHybridizerTile) tileEntity);
            }
            else if (tileEntity instanceof DNAExtractorTile && id == DNA_EXTRACTOR_ID)
            {
                return new DNAExtractorGui(player.inventory, (DNAExtractorTile) tileEntity);
            }
            else if (tileEntity instanceof CultivatorTile && id == CULTIVATOR_ID)
            {
                if (((CultivatorTile) tileEntity).isProcessing(0))
                {
                    return new CultivateProcessGui((CultivatorTile) tileEntity);
                }
                else
                {
                    return new CultivateGui(player.inventory, (CultivatorTile) tileEntity);
                }
            }
            else if (tileEntity instanceof FeederTile && id == FEEDER_ID)
            {
                return new FeederGui(player.inventory, (FeederTile) tileEntity);
            }
        }

        return null;
    }

    public static void openSelectDino(BlockPos pos, EnumFacing facing, EnumHand hand)
    {
        Minecraft.getMinecraft().displayGuiScreen(new SelectDinoGui(pos, facing, hand));
    }

    public static void openOrderGui(DinosaurEntity entity)
    {
        Minecraft.getMinecraft().displayGuiScreen(new OrderDinosaurGui(entity));
    }

    public static void openFieldGuide(DinosaurEntity entity)
    {
        Minecraft.getMinecraft().displayGuiScreen(new FieldGuideGui(entity));
    }
}
