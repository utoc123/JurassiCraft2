package org.jurassicraft.client.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import org.jurassicraft.server.block.entity.DisplayBlockEntity;

public class DisplayBlockRenderer extends TileEntitySpecialRenderer<DisplayBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void renderTileEntityAt(DisplayBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(x + 0.5, y, z + 0.5);

        GlStateManager.rotate(tileEntity.getRot(), 0.0F, 1.0F, 0.0F);
        //System.out.println(tileEntity.dinosaur);

        double scale = tileEntity.getEntity().isSkeleton() ? 1.0 : 0.15;
        GlStateManager.scale(scale, scale, scale);

        if (tileEntity.entity != null) {
            this.mc.getRenderManager().doRenderEntity(tileEntity.entity, 0, 0, 0, 0, 0, false);
        } else {
            tileEntity.updateEntity();
        }

        GlStateManager.popMatrix();
    }
}
