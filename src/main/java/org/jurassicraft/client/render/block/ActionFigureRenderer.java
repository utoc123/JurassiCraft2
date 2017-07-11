package org.jurassicraft.client.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.block.entity.ActionFigureBlockEntity;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.EntityHandler;

public class ActionFigureRenderer extends TileEntitySpecialRenderer<ActionFigureBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void renderTileEntityAt(ActionFigureBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_) {
        World world = tileEntity.getWorld();

        IBlockState state = world.getBlockState(tileEntity.getPos());

        if (state.getBlock() == BlockHandler.ACTION_FIGURE) {
            Dinosaur dino = EntityHandler.getDinosaurById(tileEntity.dinosaur);

            GlStateManager.pushMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y, z + 0.5);
            
            GlStateManager.rotate(tileEntity.getRot(), 0, 1, 0);
            //System.out.println(tileEntity.dinosaur);

            double scale = tileEntity.getEntity().isSkeleton() ? 1.0 : 0.15;
            GlStateManager.scale(scale, scale, scale);

            @SuppressWarnings("unchecked")
            Render<DinosaurEntity> renderer = (Render<DinosaurEntity>) this.mc.getRenderManager().entityRenderMap.get(dino.getDinosaurClass());

            if (tileEntity.entity != null) {
                renderer.doRender(tileEntity.entity, 0, 0, 0, 0, 0);
            } else {
                tileEntity.updateEntity();
            }

            GlStateManager.popMatrix();
        }
    }
}
