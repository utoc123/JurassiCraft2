package org.jurassicraft.client.render.block;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.entity.ElectricFencePoleBlockEntity;
import org.jurassicraft.server.block.fence.ElectricFencePoleBlock;
import org.jurassicraft.server.tabula.TabulaModelHelper;

public class ElectricFencePoleRenderer extends TileEntitySpecialRenderer<ElectricFencePoleBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    private TabulaModel model;
    private ResourceLocation texture;

    public ElectricFencePoleRenderer() {
        try {
            this.model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/low_security_fence_pole_active.tbl"));
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/low_security_fence_pole.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(ElectricFencePoleBlockEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        boolean powered = tile == null;

        if (tile != null) {
            BlockPos position = tile.getPos();
            IBlockState state = tile.getWorld().getBlockState(position).getActualState(tile.getWorld(), position);
            if (state.getBlock() == BlockHandler.LOW_SECURITY_FENCE_POLE) {
                powered = state.getValue(ElectricFencePoleBlock.POWERED);
            }
        }

        if (powered) {
            GlStateManager.pushMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

            double scale = 1.01;
            float modelScale = (float) (0.0625F / scale);
            GlStateManager.scale(-scale, -scale, scale);

            this.mc.getTextureManager().bindTexture(this.texture);

            AdvancedModelRenderer blue = this.model.getCube("Blue light active");
            AdvancedModelRenderer orange = this.model.getCube("Orange light active");

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

            blue.render(modelScale);
            orange.render(modelScale);

            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, OpenGlHelper.lastBrightnessX, OpenGlHelper.lastBrightnessY);

            GlStateManager.popMatrix();
        }
    }
}
