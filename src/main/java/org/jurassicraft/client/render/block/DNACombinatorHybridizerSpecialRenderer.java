package org.jurassicraft.client.render.block;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.jurassicraft.server.tile.DNACombinatorHybridizerTile;
import org.lwjgl.opengl.GL11;

public class DNACombinatorHybridizerSpecialRenderer extends TileEntitySpecialRenderer<DNACombinatorHybridizerTile>
{
    private Minecraft mc = Minecraft.getMinecraft();
    private TabulaModel model;
    private ResourceLocation textureCombinator;
    private ResourceLocation textureHybridizer;

    public DNACombinatorHybridizerSpecialRenderer()
    {
        try
        {
            this.model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/dna_combinator_hybridizer"));
            this.textureCombinator = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/dna_combinator.png");
            this.textureHybridizer = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/dna_hybridizer.png");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(DNACombinatorHybridizerTile tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_)
    {
        World world = tileEntity.getWorld();

        IBlockState blockState = world.getBlockState(tileEntity.getPos());

        if (blockState.getBlock() == BlockHandler.DNA_COMBINATOR_HYBRIDIZER)
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

            GlStateManager.pushMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y + 1.125F, z + 0.5);

            GlStateManager.rotate(rotation, 0, 1, 0);

            double scale = 0.75;
            GlStateManager.scale(scale, -scale, scale);

            mc.getTextureManager().bindTexture(tileEntity.getMode() ? textureHybridizer : textureCombinator);

            model.render(null, 0, 0, 0, 0, 0, 0.0625F);

            GlStateManager.popMatrix();

            GlStateManager.disableBlend();
            GlStateManager.enableCull();
        }
    }
}
