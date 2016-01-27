package org.jurassicraft.client.render.renderdef;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import org.jurassicraft.client.model.animation.IndominusAnimator;
import org.jurassicraft.client.render.entity.IndominusRenderer;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.JCEntityRegistry;

public class IndominusRenderDef extends RenderDinosaurDefinition
{
    public IndominusRenderDef(float parShadowSize)
    {
        super(JCEntityRegistry.indominus, new IndominusAnimator(), parShadowSize);
    }

    @Override
    public Render<? super DinosaurEntity> createRenderFor(RenderManager manager)
    {
        return new IndominusRenderer(this);
    }
}
