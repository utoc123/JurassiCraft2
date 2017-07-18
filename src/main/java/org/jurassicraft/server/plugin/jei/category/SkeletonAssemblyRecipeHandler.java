package org.jurassicraft.server.plugin.jei.category;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.ingredient.SkeletonInput;

public class SkeletonAssemblyRecipeHandler implements IRecipeHandler<SkeletonInput> {
    @Override
    public Class<SkeletonInput> getRecipeClass() {
        return SkeletonInput.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "jurassicraft.skeleton_assembly";
    }

    @Override
    public String getRecipeCategoryUid(SkeletonInput recipe) {
        return this.getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(SkeletonInput recipe) {
        return new SkeletonAssemblyRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(SkeletonInput recipe) {
        return recipe.dinosaur.shouldRegister();
    }
}
