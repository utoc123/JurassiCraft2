package org.jurassicraft.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.gui.PaleoPadGui;
import org.jurassicraft.client.gui.app.GuiApp;
import org.jurassicraft.client.render.entity.IDinosaurRenderer;
import org.jurassicraft.client.render.entity.IndominusRenderer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class EventHandlerClient
{
    private static final Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void tick(TickEvent.ClientTickEvent event)
    {
        JurassiCraft.timerTicks++;

        if (mc.currentScreen instanceof PaleoPadGui)
        {
            PaleoPadGui tab = (PaleoPadGui) mc.currentScreen;

            GuiApp focus = tab.focus;

            if (focus != null)
            {
                focus.update();
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void preRender(RenderLivingEvent.Pre event)
    {
        if (event.entity instanceof DinosaurEntity && event.renderer instanceof IDinosaurRenderer)
        {
            IDinosaurRenderer dinoRenderer = (IDinosaurRenderer) event.renderer;
            DinosaurEntity entityDinosaur = (DinosaurEntity) event.entity;

            dinoRenderer.setModel(dinoRenderer.getRenderDef().getModel(entityDinosaur.getGrowthStage()));
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void postRender(RenderLivingEvent.Post event)
    {
        if (event.entity instanceof DinosaurEntity && event.renderer instanceof IDinosaurRenderer && !(event.renderer instanceof IndominusRenderer))
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }
}
