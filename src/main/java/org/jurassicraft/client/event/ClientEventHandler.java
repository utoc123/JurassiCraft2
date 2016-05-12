package org.jurassicraft.client.event;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.jurassicraft.client.render.entity.IDinosaurRenderer;
import org.jurassicraft.client.render.entity.IndominusRenderer;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class ClientEventHandler
{
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
