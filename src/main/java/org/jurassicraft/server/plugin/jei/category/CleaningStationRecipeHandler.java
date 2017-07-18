package org.jurassicraft.server.plugin.jei.category;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plugin.jei.category.ingredient.BoneInput;

public class CleaningStationRecipeHandler implements IRecipeHandler<BoneInput> {
    @Override
    public Class<BoneInput> getRecipeClass() {
        return BoneInput.class;
    }

    @Override
    public String getRecipeCategoryUid() {
        return "jurassicraft.cleaning_station";
    }

    @Override
    public String getRecipeCategoryUid(BoneInput recipe) {
        return this.getRecipeCategoryUid();
    }

    @Override
    public IRecipeWrapper getRecipeWrapper(BoneInput recipe) {
        return new CleaningStationRecipeWrapper(recipe.dinosaur, recipe.bone);
    }

    @Override
    public boolean isRecipeValid(BoneInput recipe) {
        return recipe.dinosaur.shouldRegister() && ItemHandler.FOSSILS.containsKey(recipe.bone);
    }
}
