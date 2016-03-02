package org.jurassicraft.server.message;

import io.netty.buffer.ByteBuf;
import net.ilexiconn.llibrary.common.message.AbstractMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.gui.DNACombinatorHybridizerGui;
import org.jurassicraft.server.container.DNACombinatorHybridizerContainer;
import org.jurassicraft.server.tileentity.DNACombinatorHybridizerTile;

public class SwitchHybridizerCombinatorMode extends AbstractMessage<SwitchHybridizerCombinatorMode>
{
    private BlockPos pos;
    private boolean hybridizer;

    public SwitchHybridizerCombinatorMode()
    {
    }

    public SwitchHybridizerCombinatorMode(BlockPos pos, boolean hybridizer)
    {
        this.hybridizer = hybridizer;
        this.pos = pos;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientMessage(SwitchHybridizerCombinatorMode message, EntityPlayer player)
    {
        DNACombinatorHybridizerTile tile = (DNACombinatorHybridizerTile) player.worldObj.getTileEntity(message.pos);
        tile.setMode(message.hybridizer);

        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (screen instanceof DNACombinatorHybridizerGui)
        {
            ((DNACombinatorHybridizerContainer) ((DNACombinatorHybridizerGui) screen).inventorySlots).updateSlots(message.hybridizer);
        }
    }

    @Override
    public void handleServerMessage(SwitchHybridizerCombinatorMode message, EntityPlayer player)
    {
        boolean mode = message.hybridizer;
        BlockPos pos = message.pos;

        DNACombinatorHybridizerTile tile = (DNACombinatorHybridizerTile) player.worldObj.getTileEntity(pos);

        tile.setMode(mode);
        JurassiCraft.networkWrapper.sendToAll(new SwitchHybridizerCombinatorMode(pos, mode));

        Container openContainer = player.openContainer;

        if (openContainer instanceof DNACombinatorHybridizerContainer)
        {
            ((DNACombinatorHybridizerContainer) openContainer).updateSlots(mode);
        }
    }

    @Override
    public void toBytes(ByteBuf buffer)
    {
        buffer.writeBoolean(hybridizer);
        buffer.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buffer)
    {
        hybridizer = buffer.readBoolean();
        pos = BlockPos.fromLong(buffer.readLong());
    }
}
