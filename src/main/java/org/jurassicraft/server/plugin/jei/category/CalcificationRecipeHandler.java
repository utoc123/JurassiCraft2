package org.jurassicraft.server.plugin.jei.category;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jurassicraft.server.plugin.jei.category.ingredient.CalcificationInput;

public class CalcificationRecipeHandler implements IRecipeHandler<CalcificationInput> {
    @Override
    public Class<CalcificationInput> getRecipeClass() {
        return CalcificationInput.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "jurassicraft.embryo_calcification_machine";
    }

    @Override
    public String getRecipeCategoryUid(CalcificationInput recipe) {
        return this.getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(CalcificationInput recipe) {
        return new CalcificationRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(CalcificationInput recipe) {
        return recipe.dinosaur.shouldRegister();
    }
}
