package org.jurassicraft.client.render.entity.dinosaur;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import org.jurassicraft.client.model.animation.entity.IndominusAnimator;
import org.jurassicraft.client.render.entity.IndominusRenderer;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;

public class IndominusRenderDefinition extends RenderDinosaurDefinition
{
    public IndominusRenderDefinition(float parShadowSize)
    {
        super(EntityHandler.INSTANCE.indominus, new IndominusAnimator(), parShadowSize);
    }

    @Override
    public Render<? super DinosaurEntity> createRenderFor(RenderManager manager)
    {
        return new IndominusRenderer(this, manager);
    }
}
