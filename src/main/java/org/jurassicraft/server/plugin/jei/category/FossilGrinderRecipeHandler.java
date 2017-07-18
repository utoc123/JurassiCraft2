package org.jurassicraft.server.plugin.jei.category;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.ingredient.GrinderInput;

public class FossilGrinderRecipeHandler implements IRecipeHandler<GrinderInput> {
    @Override
    public Class<GrinderInput> getRecipeClass() {
        return GrinderInput.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "jurassicraft.fossil_grinder";
    }

    @Override
    public String getRecipeCategoryUid(GrinderInput recipe) {
        return this.getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(GrinderInput recipe) {
        return new FossilGrinderRecipeWrapper(recipe.dinosaur);
    }

    @Override
    public boolean isRecipeValid(GrinderInput recipe) {
        return recipe.dinosaur.shouldRegister();
    }
}
