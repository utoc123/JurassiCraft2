package org.jurassicraft.client.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.client.event.ClientEventHandler;
import org.jurassicraft.client.render.RenderingHandler;
import org.jurassicraft.server.proxy.ServerProxy;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    private static final Minecraft MC = Minecraft.getMinecraft();

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);

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
}
