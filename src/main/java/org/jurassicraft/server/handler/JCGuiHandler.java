package org.jurassicraft.server.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.gui.CleaningStationGui;
import org.jurassicraft.client.gui.CultivateGui;
import org.jurassicraft.client.gui.CultivateProcessGui;
import org.jurassicraft.client.gui.DNACombinatorGui;
import org.jurassicraft.client.gui.DNAExtractorGui;
import org.jurassicraft.client.gui.DNAHybridizerGui;
import org.jurassicraft.client.gui.DNASequencerGui;
import org.jurassicraft.client.gui.DNASynthesizerGui;
import org.jurassicraft.client.gui.EmbryoCalcificationMachineGui;
import org.jurassicraft.client.gui.EmbryonicMachineGui;
import org.jurassicraft.client.gui.FossilGrinderGui;
import org.jurassicraft.client.gui.IncubatorGui;
import org.jurassicraft.client.gui.PaleoPadGui;
import org.jurassicraft.client.gui.PaleoPadViewDinosaurGui;
import org.jurassicraft.client.gui.SelectDinoGui;
import org.jurassicraft.server.container.CleaningStationContainer;
import org.jurassicraft.server.container.CultivateContainer;
import org.jurassicraft.server.container.DNACombinatorContainer;
import org.jurassicraft.server.container.DNAExtractorContainer;
import org.jurassicraft.server.container.DNAHybridizerContainer;
import org.jurassicraft.server.container.DNASequencerContainer;
import org.jurassicraft.server.container.DNASynthesizerContainer;
import org.jurassicraft.server.container.EmbryoCalcificationMachineContainer;
import org.jurassicraft.server.container.EmbryonicMachineContainer;
import org.jurassicraft.server.container.FossilGrinderContainer;
import org.jurassicraft.server.container.IncubatorContainer;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.tileentity.CleaningStationTile;
import org.jurassicraft.server.tileentity.CultivatorTile;
import org.jurassicraft.server.tileentity.DNACombinatorTile;
import org.jurassicraft.server.tileentity.DNAExtractorTile;
import org.jurassicraft.server.tileentity.DNAHybridizerTile;
import org.jurassicraft.server.tileentity.DNASequencerTile;
import org.jurassicraft.server.tileentity.DNASynthesizerTile;
import org.jurassicraft.server.tileentity.EmbryoCalcificationMachineTile;
import org.jurassicraft.server.tileentity.EmbryonicMachineTile;
import org.jurassicraft.server.tileentity.FossilGrinderTile;
import org.jurassicraft.server.tileentity.IncubatorTile;

public class JCGuiHandler implements IGuiHandler
{
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tileEntity = world.getTileEntity(pos);

        if (tileEntity != null)
        {
            if (tileEntity instanceof CleaningStationTile && id == 0)
            {
                return new CleaningStationContainer(player.inventory, (CleaningStationTile) tileEntity);
            }
            else if (tileEntity instanceof FossilGrinderTile && id == 1)
            {
                return new FossilGrinderContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNASequencerTile && id == 2)
            {
                return new DNASequencerContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof EmbryonicMachineTile && id == 3)
            {
                return new EmbryonicMachineContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof EmbryoCalcificationMachineTile && id == 4)
            {
                return new EmbryoCalcificationMachineContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNASynthesizerTile && id == 5)
            {
                return new DNASynthesizerContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof IncubatorTile && id == 6)
            {
                return new IncubatorContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNAHybridizerTile && id == 7)
            {
                return new DNAHybridizerContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof DNACombinatorTile && id == 8)
            {
                return new DNACombinatorContainer(player.inventory, (DNACombinatorTile) tileEntity);
            }
            else if (tileEntity instanceof DNAExtractorTile && id == 9)
            {
                return new DNAExtractorContainer(player.inventory, tileEntity);
            }
            else if (tileEntity instanceof CultivatorTile && id == 10)
            {
                return new CultivateContainer(player.inventory, tileEntity);
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
            if (tileEntity instanceof CleaningStationTile && id == 0)
            {
                return new CleaningStationGui(player.inventory, (CleaningStationTile) tileEntity);
            }
            else if (tileEntity instanceof FossilGrinderTile && id == 1)
            {
                return new FossilGrinderGui(player.inventory, (FossilGrinderTile) tileEntity);
            }
            else if (tileEntity instanceof DNASequencerTile && id == 2)
            {
                return new DNASequencerGui(player.inventory, (DNASequencerTile) tileEntity);
            }
            else if (tileEntity instanceof EmbryonicMachineTile && id == 3)
            {
                return new EmbryonicMachineGui(player.inventory, (EmbryonicMachineTile) tileEntity);
            }
            else if (tileEntity instanceof EmbryoCalcificationMachineTile && id == 4)
            {
                return new EmbryoCalcificationMachineGui(player.inventory, (EmbryoCalcificationMachineTile) tileEntity);
            }
            else if (tileEntity instanceof DNASynthesizerTile && id == 5)
            {
                return new DNASynthesizerGui(player.inventory, (DNASynthesizerTile) tileEntity);
            }
            else if (tileEntity instanceof IncubatorTile && id == 6)
            {
                return new IncubatorGui(player.inventory, (IncubatorTile) tileEntity);
            }
            else if (tileEntity instanceof DNAHybridizerTile && id == 7)
            {
                return new DNAHybridizerGui(player.inventory, (DNAHybridizerTile) tileEntity);
            }
            else if (tileEntity instanceof DNACombinatorTile && id == 8)
            {
                return new DNACombinatorGui(player.inventory, (DNACombinatorTile) tileEntity);
            }
            else if (tileEntity instanceof DNAExtractorTile && id == 9)
            {
                return new DNAExtractorGui(player.inventory, (DNAExtractorTile) tileEntity);
            }
            else if (tileEntity instanceof CultivatorTile && id == 10)
            {
                if (((CultivatorTile) tileEntity).isCultivating())
                {
                    return new CultivateProcessGui((CultivatorTile) tileEntity);
                }
                else
                {
                    return new CultivateGui(player.inventory, (CultivatorTile) tileEntity);
                }
            }
        }

        return null;
    }

    public static void openPaleoPad(EntityPlayer player)
    {
        if (player.worldObj.isRemote)
        {
            displayPaleoPadGUIClient();
        }
        // else
        // JurassiCraft.networkWrapper.sendTo(new SyncPaleoPadMessage(player), (EntityPlayerMP) player);
    }

    public static void openSelectDino(EntityPlayer player, BlockPos pos, EnumFacing facing)
    {
        if (player.worldObj.isRemote)
        {
            displayOpenSelectDino(pos, facing);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void displayPaleoPadGUIClient()
    {
        Minecraft.getMinecraft().displayGuiScreen(new PaleoPadGui());
    }

    @SideOnly(Side.CLIENT)
    private static void displayOpenSelectDino(BlockPos pos, EnumFacing facing)
    {
        Minecraft.getMinecraft().displayGuiScreen(new SelectDinoGui(pos, facing));
    }

    public static void openViewDinosaur(DinosaurEntity dinosaur)
    {
        if (dinosaur.worldObj.isRemote)
        {
            displayViewDinosaurClient(dinosaur);
        }
    }

    @SideOnly(Side.CLIENT)
    private static void displayViewDinosaurClient(DinosaurEntity dinosaur)
    {
        Minecraft.getMinecraft().displayGuiScreen(new PaleoPadViewDinosaurGui(dinosaur));
    }
}
