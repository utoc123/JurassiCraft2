package org.jurassicraft.client.render.block;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.entity.ElectricFenceBaseBlockEntity;
import org.jurassicraft.server.block.fence.ElectricFenceBaseBlock;
import org.jurassicraft.server.tabula.TabulaModelHelper;

public class ElectricFenceBaseRenderer extends TileEntitySpecialRenderer<ElectricFenceBaseBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();

    private TabulaModel model;
    private TabulaModel modelCorner;
    private TabulaModel modelPole;
    private ResourceLocation texture;
    private ResourceLocation texturePole;
    private ResourceLocation textureCorner;

    public ElectricFenceBaseRenderer() {
        try {
            this.model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/low_security_fence_base.tbl"));
            this.modelPole = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/low_security_fence_base_pole.tbl"));
            this.modelCorner = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/low_security_fence_base_corner.tbl"));
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/low_security_fence_base.png");
            this.texturePole = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/low_security_fence_base_pole.png");
            this.textureCorner = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/low_security_fence_base_corner.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(ElectricFenceBaseBlockEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.cullFace(GlStateManager.CullFace.FRONT);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

        double scale = 1.0;
        GlStateManager.scale(scale, -scale, scale);

        boolean north = true;
        boolean south = true;
        boolean west = false;
        boolean east = false;
        boolean pole = false;

        if (tile != null) {
            BlockPos position = tile.getPos();
            IBlockState state = tile.getWorld().getBlockState(position).getActualState(tile.getWorld(), position);
            if (state.getBlock() == BlockHandler.LOW_SECURITY_FENCE_BASE) {
                north = state.getValue(ElectricFenceBaseBlock.NORTH);
                south = state.getValue(ElectricFenceBaseBlock.SOUTH);
                west = state.getValue(ElectricFenceBaseBlock.WEST);
                east = state.getValue(ElectricFenceBaseBlock.EAST);
                pole = state.getValue(ElectricFenceBaseBlock.POLE);
            }
        }

        if (!north && !south && !west && !east && !pole) {
            north = true;
            south = true;
            west = false;
            east = false;
        }

        if (pole) {
            this.mc.getTextureManager().bindTexture(this.texturePole);
            this.modelPole.render(null, 0, 0, 0, 0, 0, 0.0625F);
        } else {
            int count = 0;
            if (north) {
                count++;
            }
            if (south) {
                count++;
            }
            if (west) {
                count++;
            }
            if (east) {
                count++;
            }

            if (count == 4) {
                this.mc.getTextureManager().bindTexture(this.texture);
                if (west || east) {
                    this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
                }
                if (north || south) {
                    GlStateManager.scale(0.999, 0.999, 0.999);
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                    this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
                }
            } else if (count == 3) {
                this.mc.getTextureManager().bindTexture(this.textureCorner);
                if (south && west && north) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                } else if (east && south && west) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                } else if (east && north && west) {
                    GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
                }
                this.modelCorner.render(null, 0, 0, 0, 0, 0, 0.0625F);
                this.mc.getTextureManager().bindTexture(this.texture);
                GlStateManager.scale(0.999, 0.999, 0.999);
                GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
            } else if ((north && east) || (north && west) || (south && east) || (south && west)) {
                this.mc.getTextureManager().bindTexture(this.textureCorner);
                if (south && east) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                } else if (south) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                } else if (west) {
                    GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
                }
                this.modelCorner.render(null, 0, 0, 0, 0, 0, 0.0625F);
            } else {
                this.mc.getTextureManager().bindTexture(this.texture);
                if (north || south) {
                    GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
                }
                this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);
            }
        }

        GlStateManager.cullFace(GlStateManager.CullFace.BACK);
        GlStateManager.popMatrix();
    }
}
