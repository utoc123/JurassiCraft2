package org.jurassicraft.server.plugin.jei.category;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.jurassicraft.JurassiCraft;

import java.util.List;

public class FossilGrinderRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(JurassiCraft.MODID, "textures/gui/fossil_grinder.png");

    private final IDrawable background;
    private final String title;

    private final IDrawableAnimated arrow;

    public FossilGrinderRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(TEXTURE, 18, 21, 147, 43);
        this.title = I18n.translateToLocal("tile.fossil_grinder.name");

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(TEXTURE, 176, 14, 24, 16);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
        this.arrow.draw(minecraft, 61, 14);
    }

    @Override
    public String getUid() {
        return "jurassicraft.fossil_grinder";
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup stackGroup = recipeLayout.getItemStacks();
        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
        for (int row = 0; row < 2; row++) {
            for (int column = 0; column < 3; column++) {
                int index = column + row * 2;
                int outputIndex = index + 6;
                stackGroup.init(index, true, 4 + column * 18, 4 + row * 18);
                stackGroup.init(outputIndex, false, 89 + column * 18, 4 + row * 18);
                if (index < inputs.size()) {
                    stackGroup.set(index, inputs.get(index));
                }
                if (index < outputs.size()) {
                    stackGroup.set(outputIndex, outputs.get(index));
                }
            }
        }
    }
}
