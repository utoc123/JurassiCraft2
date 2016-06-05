package org.jurassicraft.client.event;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.render.entity.IDinosaurRenderer;
import org.jurassicraft.client.render.entity.IndominusRenderer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class ClientEventHandler
{
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void preRender(RenderLivingEvent.Pre event)
    {
        if (event.getEntity() instanceof DinosaurEntity && event.getRenderer() instanceof IDinosaurRenderer)
        {
            IDinosaurRenderer dinoRenderer = (IDinosaurRenderer) event.getRenderer();
            DinosaurEntity entityDinosaur = (DinosaurEntity) event.getEntity();

            dinoRenderer.setModel(dinoRenderer.getRenderDef().getModel(entityDinosaur.getGrowthStage()));
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public void postRender(RenderLivingEvent.Post event)
    {
        if (event.getEntity() instanceof DinosaurEntity && event.getRenderer() instanceof IDinosaurRenderer && !(event.getRenderer() instanceof IndominusRenderer))
        {
            GlStateManager.color(1.0F, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event)
    {
        JurassiCraft.timerTicks++;
    }
}
