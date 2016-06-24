package org.jurassicraft.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.modules.SeatEntity;

@SideOnly(Side.CLIENT)
public class SeatRenderer implements IRenderFactory<SeatEntity>
{
    @Override
    public Render<? super SeatEntity> createRenderFor(RenderManager manager)
    {
        return new Renderer(manager);
    }

    public static class Renderer extends Render<SeatEntity>
    {
        public Renderer(RenderManager manager)
        {
            super(manager);
        }

        @Override
        public void doRender(SeatEntity entity, double x, double y, double z, float yaw, float partialTicks)
        {
        }

        @Override
        protected ResourceLocation getEntityTexture(SeatEntity entity)
        {
            return null;
        }
    }
}