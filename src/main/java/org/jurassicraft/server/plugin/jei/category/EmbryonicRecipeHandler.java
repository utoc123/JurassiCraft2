package org.jurassicraft.server.plugin.jei.category;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.ingredient.EmbryoInput;

public class EmbryonicRecipeHandler implements IRecipeHandler<EmbryoInput> {
    @Override
    public Class<EmbryoInput> getRecipeClass() {
        return EmbryoInput.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "jurassicraft.embryonic_machine";
    }

    @Override
    public String getRecipeCategoryUid(EmbryoInput recipe) {
        return this.getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(EmbryoInput recipe) {
        return new EmbryonicRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(EmbryoInput recipe) {
        return recipe.isValid();
    }
}
