package org.jurassicraft.client.render.block;

import net.ilexiconn.llibrary.client.model.tabula.ModelJson;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.jurassicraft.server.tileentity.DNASequencerTile;
import org.lwjgl.opengl.GL11;

public class DNASequencerSpecialRenderer extends TileEntitySpecialRenderer<DNASequencerTile>
{
    private Minecraft mc = Minecraft.getMinecraft();
    private ModelJson model;
    private ResourceLocation texture;

    public DNASequencerSpecialRenderer()
    {
        try
        {
            this.model = new ModelJson(TabulaModelHelper.parseModel("/assets/jurassicraft/models/block/dna_sequencer"));
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/dna_sequencer.png");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(DNASequencerTile tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_)
    {
        World world = tileEntity.getWorld();

        IBlockState blockState = world.getBlockState(tileEntity.getPos());

        if (blockState.getBlock() == JCBlockRegistry.dna_sequencer)
        {
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            EnumFacing value = blockState.getValue(OrientedBlock.FACING);

            if (value == EnumFacing.NORTH || value == EnumFacing.SOUTH)
            {
                value = value.getOpposite();
            }

            int rotation = value.getHorizontalIndex() * 90;

            GlStateManager.pushMatrix(); // Items
            {
                GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.5);

                GlStateManager.rotate(rotation, 0, 1, 0);
                GlStateManager.translate(0.2, -0.6, 0.15);
                GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);

                GlStateManager.scale(0.75F, 0.75F, 0.75F);
                mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

                RenderItem renderItem = mc.getRenderItem();

                for (int inputSlot : tileEntity.getSlotsForFace(EnumFacing.UP))
                {
                    GlStateManager.translate(0.0, 0.0, 0.2);

                    if (inputSlot % 2 == 0)
                    {
                        ItemStack sequence = tileEntity.getStackInSlot(inputSlot);

                        if (sequence != null)
                        {
                            renderItem.renderItem(sequence, renderItem.getItemModelMesher().getItemModel(sequence));
                        }
                    }
                }
            }
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix(); // Model
            {
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.5);

                GlStateManager.rotate(rotation, 0, 1, 0);

                double scale = 1.0;
                GlStateManager.scale(scale, -scale, scale);

                mc.getTextureManager().bindTexture(texture);

                model.render(null, 0, 0, 0, 0, 0, 0.0625F);
            }
            GlStateManager.popMatrix();

            GlStateManager.disableBlend();
            GlStateManager.enableCull();
        }
    }
}
