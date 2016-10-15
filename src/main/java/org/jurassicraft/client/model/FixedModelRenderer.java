package org.jurassicraft.client.model;

import net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;

import java.util.Random;

public class FixedModelRenderer extends AdvancedModelRenderer {
    private static final Random RANDOM = new Random();

    private int displayList;
    private boolean compiled;

    public FixedModelRenderer(AdvancedModelBase model, String name) {
        super(model, name);
    }

    @Override
    public void render(float scale) {
        if (!this.isHidden) {
            if (this.showModel) {
                GlStateManager.pushMatrix();
                if (!this.compiled) {
                    this.compileDisplayList(scale);
                }
                RANDOM.setSeed(this.displayList * 15415);
                float offsetScale = 0.005F;
                GlStateManager.translate((RANDOM.nextFloat() - 0.5F) * offsetScale, (RANDOM.nextFloat() - 0.5F) * offsetScale, (RANDOM.nextFloat() - 0.5F) * offsetScale);
                GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
                GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                if (this.rotateAngleZ != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                }
                if (this.rotateAngleY != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                }
                if (this.rotateAngleX != 0.0F) {
                    GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                }
                if (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F) {
                    GlStateManager.scale(this.scaleX, this.scaleY, this.scaleZ);
                }
                GlStateManager.callList(this.displayList);
                if (!this.scaleChildren && (this.scaleX != 1.0F || this.scaleY != 1.0F || this.scaleZ != 1.0F)) {
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
                    GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
                    if (this.rotateAngleZ != 0.0F) {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleZ), 0.0F, 0.0F, 1.0F);
                    }
                    if (this.rotateAngleY != 0.0F) {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleY), 0.0F, 1.0F, 0.0F);
                    }
                    if (this.rotateAngleX != 0.0F) {
                        GlStateManager.rotate((float) Math.toDegrees(this.rotateAngleX), 1.0F, 0.0F, 0.0F);
                    }
                }
                if (this.childModels != null) {
                    for (ModelRenderer childModel : this.childModels) {
                        childModel.render(scale);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }

    private void compileDisplayList(float scale) {
        this.displayList = GLAllocation.generateDisplayLists(1);
        GlStateManager.glNewList(this.displayList, 4864);
        VertexBuffer buffer = Tessellator.getInstance().getBuffer();
        for (ModelBox box : this.cubeList) {
            box.render(buffer, scale);
        }
        GlStateManager.glEndList();
        this.compiled = true;
    }
}
