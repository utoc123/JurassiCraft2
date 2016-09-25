package org.jurassicraft.client.render.block;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.entity.ElectricFenceWireBlockEntity;
import org.jurassicraft.server.block.fence.ElectricFenceWireBlock;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.lwjgl.opengl.GL11;

public class ElectricFenceWireRenderer extends TileEntitySpecialRenderer<ElectricFenceWireBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    private TabulaModel model;
    private TabulaModel modelDiagonal;
    private ResourceLocation texture;

    public ElectricFenceWireRenderer() {
        try {
            this.model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/low_security_fence_wire.tbl"));
            this.modelDiagonal = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/low_security_fence_wire_diagonal.tbl"));
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/low_security_fence_wire.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(ElectricFenceWireBlockEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableBlend();
        GlStateManager.cullFace(GlStateManager.CullFace.FRONT);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

        double scale = 1.0;
        GlStateManager.scale(scale, -scale, scale);

        boolean north = true;
        boolean south = true;
        boolean west = false;
        boolean east = false;
        EnumFacing upDirection = EnumFacing.DOWN;

        if (tile != null) {
            BlockPos position = tile.getPos();
            IBlockState state = tile.getWorld().getBlockState(position).getActualState(tile.getWorld(), position);
            if (state.getBlock() == BlockHandler.LOW_SECURITY_FENCE_WIRE) {
                north = state.getValue(ElectricFenceWireBlock.NORTH);
                south = state.getValue(ElectricFenceWireBlock.SOUTH);
                west = state.getValue(ElectricFenceWireBlock.WEST);
                east = state.getValue(ElectricFenceWireBlock.EAST);
                upDirection = state.getValue(ElectricFenceWireBlock.UP_DIRECTION);
            }
        }

        if (!north && !south && !west && !east) {
            north = true;
            south = true;
            west = true;
            east = true;
        }

        this.mc.getTextureManager().bindTexture(this.texture);

        if (upDirection != EnumFacing.DOWN) {
            if (upDirection == EnumFacing.NORTH || upDirection == EnumFacing.SOUTH) {
                upDirection = upDirection.getOpposite();
            }
            GlStateManager.rotate(upDirection.getHorizontalIndex() * 90 - 90, 0.0F, 1.0F, 0.0F);

            this.modelDiagonal.render(null, 0, 0, 0, 0, 0, 0.0625F);
        } else {
            this.model.getCube("Wire 3").showModel = north;
            this.model.getCube("Wire 3B").showModel = north;

            this.model.getCube("Wire 2").showModel = east;
            this.model.getCube("Wire 2B").showModel = east;

            this.model.getCube("Wire 1").showModel = west;
            this.model.getCube("Wire 1B").showModel = west;

            this.model.getCube("Wire 4").showModel = south;
            this.model.getCube("Wire 4B").showModel = south;

            this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
        }

        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
