package org.jurassicraft.client.render.block;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.ilexiconn.llibrary.client.model.tabula.TabulaModelHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.tile.DNAExtractorTile;
import org.lwjgl.opengl.GL11;

public class DNAExtractorSpecialRenderer extends TileEntitySpecialRenderer<DNAExtractorTile>
{
    private Minecraft mc = Minecraft.getMinecraft();

    private TabulaModel model;
    private ResourceLocation texture;

    public DNAExtractorSpecialRenderer()
    {
        try
        {
            this.model = new TabulaModel(TabulaModelHandler.INSTANCE.loadTabulaModel("/assets/jurassicraft/models/block/dna_extractor"));
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/dna_extractor.png");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(DNAExtractorTile tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_)
    {
        IBlockState blockState = tileEntity.getWorld().getBlockState(tileEntity.getPos());

        if (blockState.getBlock() == BlockHandler.INSTANCE.DNA_EXTRACTOR)
        {
            GlStateManager.pushMatrix();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);

            EnumFacing value = blockState.getValue(OrientedBlock.FACING);

            if (value == EnumFacing.EAST || value == EnumFacing.WEST)
            {
                value = value.getOpposite();
            }

            int rotation = value.getHorizontalIndex() * 90;

            GlStateManager.rotate(rotation - 180, 0, 1, 0);

            double scale = 1.0;
            GlStateManager.scale(scale, -scale, scale);

            mc.getTextureManager().bindTexture(texture);

            model.render(null, 0, 0, 0, 0, 0, 0.0625F);

            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            ItemStack extraction = tileEntity.getStackInSlot(0);

            RenderItem renderItem = mc.getRenderItem();

            if (extraction != null)
            {
                GlStateManager.translate(0.225, 1.25, -0.125);
                GlStateManager.rotate(-90, 1, 0, 0);
                GlStateManager.scale(-0.75 * 0.5, -0.75 * 0.5, 0.75 * 0.5);
                renderItem.renderItem(extraction, renderItem.getItemModelMesher().getItemModel(extraction));
            }
//
//            ItemStack disc = tileEntity.getStackInSlot(1);
//
//            if (disc != null)
//            {
//                GlStateManager.translate(0, 0, -0.45);
//                GlStateManager.rotate(15, 1, 0, 0);
//
//                if (tileEntity.isProcessing(0))
//                {
//                    GlStateManager.rotate(mc.thePlayer.ticksExisted * 2 % 360, 0, 0, 1);
//                }
//
//                renderItem.renderItem(disc, renderItem.getItemModelMesher().getItemModel(disc));
//            }

            GlStateManager.disableBlend();
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }
}
