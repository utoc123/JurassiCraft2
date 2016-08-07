package org.jurassicraft.client.render.block;

import net.ilexiconn.llibrary.client.model.tabula.TabulaModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.render.RenderingHandler;
import org.jurassicraft.client.render.entity.dinosaur.DinosaurRenderInfo;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.block.entity.IncubatorBlockEntity;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.tabula.TabulaModelHelper;
import org.lwjgl.opengl.GL11;

public class IncubatorSpecialRenderer extends TileEntitySpecialRenderer<IncubatorBlockEntity> {
    private Minecraft mc = Minecraft.getMinecraft();
    private TabulaModel model;
    private ResourceLocation texture;

    public IncubatorSpecialRenderer() {
        try {
            this.model = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicraft/models/block/incubator"));
            this.texture = new ResourceLocation(JurassiCraft.MODID, "textures/blocks/incubator.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void renderTileEntityAt(IncubatorBlockEntity tileEntity, double x, double y, double z, float p_180535_8_, int p_180535_9_) {
        World world = tileEntity.getWorld();

        IBlockState state = world.getBlockState(tileEntity.getPos());

        if (state.getBlock() == BlockHandler.INCUBATOR) {
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.disableRescaleNormal();

            EnumFacing value = state.getValue(OrientedBlock.FACING);

            if (value == EnumFacing.NORTH || value == EnumFacing.SOUTH) {
                value = value.getOpposite();
            }

            int rotation = value.getHorizontalIndex() * 90;

            GlStateManager.pushMatrix();

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.translate(x + 0.5, y, z + 0.5);

            GlStateManager.rotate(rotation, 0, 1, 0);

            double scale = 0.5;
            GlStateManager.scale(-scale, -scale, scale);

            GlStateManager.translate(0, -1.5, 0);

            this.mc.getTextureManager().bindTexture(this.texture);

            this.model.render(null, 0, 0, 0, 0, 0, 0.0625F);

            GlStateManager.popMatrix();

            this.renderEgg(tileEntity.getStackInSlot(0), rotation, x, y, z, 0.2, 0.2);
            this.renderEgg(tileEntity.getStackInSlot(1), rotation, x, y, z, 0.2, 0.8);
            this.renderEgg(tileEntity.getStackInSlot(3), rotation, x, y, z, 0.8, 0.8);
            this.renderEgg(tileEntity.getStackInSlot(4), rotation, x, y, z, 0.8, 0.2);
            this.renderEgg(tileEntity.getStackInSlot(2), rotation, x, y + 0.05, z, 0.5, 0.5);

            GlStateManager.disableBlend();
            GlStateManager.enableCull();
        }
    }

    public void renderEgg(ItemStack stack, float rotation, double x, double y, double z, double xOffset, double zOffset) {
        if (stack != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y + 0.05, z);
            GlStateManager.translate(xOffset, 1.2, zOffset);
            GlStateManager.scale(-0.5F, -0.5F, -0.5F);
            DinosaurRenderInfo renderDef = RenderingHandler.INSTANCE.getRenderDef(EntityHandler.getDinosaurById(stack.getItemDamage()));
            this.mc.getTextureManager().bindTexture(renderDef.getEggTexture());
            renderDef.getEggModel().render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
            GlStateManager.popMatrix();
        }
    }
}
