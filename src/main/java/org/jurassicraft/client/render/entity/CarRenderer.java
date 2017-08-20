package org.jurassicraft.client.render.entity;

import java.util.stream.IntStream;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.CarEntity;

@SideOnly(Side.CLIENT)
public abstract class CarRenderer<E extends CarEntity> extends Render<E> {
    private static final ResourceLocation[] DESTROY_STAGES = IntStream.range(0, 10)
        .mapToObj(n -> new ResourceLocation(String.format("textures/blocks/destroy_stage_%d.png", n)))
        .toArray(ResourceLocation[]::new);

    protected CarRenderer(RenderManager renderManager) {
        super(renderManager);
    }
}
