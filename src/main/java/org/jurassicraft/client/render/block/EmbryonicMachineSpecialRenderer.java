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
import org.jurassicraft.server.tile.EmbryonicMachineTile;
import org.lwjgl.opengl.GL11;

public class EmbryonicMachineSpecialRenderer extends TileEntitySpecialRenderer<EmbryonicMachineTile>
{
    private Minecraft mc = Minecraft.getMinecraft();
    private TabulaModel model;
    private ResourceLocation texture;
    private ResourceLocation textureNoTestTubes;

    public EmbryonicMachineSpecialRenderer()
    {
        try
        {
            this.model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/embryonic_machine"));
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/embryonic_machine.png");
            this.textureNoTestTubes = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/embryonic_machine_no_test_tubes.png");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(EmbryonicMachineTile tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_)
    {
        World world = tileEntity.getWorld();

        IBlockState state = world.getBlockState(tileEntity.getPos());

        if (state.getBlock() == BlockHandler.INSTANCE.EMBRYONIC_MACHINE)
        {
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            EnumFacing value = state.getValue(OrientedBlock.FACING);

            if (value == EnumFacing.NORTH || value == EnumFacing.SOUTH)
            {
                value = value.getOpposite();
            }

            int rotation = value.getHorizontalIndex() * 90;

            GlStateManager.pushMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y + 1.5F, z + 0.5);

            GlStateManager.rotate(rotation, 0, 1, 0);

            double scale = 1.0;
            GlStateManager.scale(scale, -scale, scale);

            boolean hasDNA = tileEntity.getStackInSlot(0) != null;
            boolean hasPetridish = tileEntity.getStackInSlot(1) != null;

            model.getCube("Petri dish 1").showModel = hasPetridish;
            model.getCube("Petri dish 2").showModel = hasPetridish;

            mc.getTextureManager().bindTexture(hasDNA ? texture : textureNoTestTubes);

            model.render(null, 0, 0, 0, 0, 0, 0.0625F);

            GlStateManager.popMatrix();

            GlStateManager.disableBlend();
            GlStateManager.enableCull();
        }
    }
}
