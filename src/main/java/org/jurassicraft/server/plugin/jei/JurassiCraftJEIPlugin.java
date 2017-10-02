package org.jurassicraft.server.plugin.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.tree.AncientDoorBlock;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.plugin.jei.category.CalcificationRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.CalcificationRecipeHandler;
import org.jurassicraft.server.plugin.jei.category.CleaningStationRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.CleaningStationRecipeHandler;
import org.jurassicraft.server.plugin.jei.category.DNASynthesizerRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.DNASynthesizerRecipeHandler;
import org.jurassicraft.server.plugin.jei.category.EmbryonicRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.EmbryonicRecipeHandler;
import org.jurassicraft.server.plugin.jei.category.FossilGrinderRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.FossilGrinderRecipeHandler;
import org.jurassicraft.server.plugin.jei.category.SkeletonAssemblyRecipeCategory;
import org.jurassicraft.server.plugin.jei.category.SkeletonAssemblyRecipeHandler;
import org.jurassicraft.server.plugin.jei.category.ingredient.BoneInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.CalcificationInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.EmbryoInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.GrinderInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.SkeletonInput;
import org.jurassicraft.server.plugin.jei.category.ingredient.SynthesizerInput;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@JEIPlugin
public class JurassiCraftJEIPlugin extends BlankModPlugin {
    @Override
    public void register(IModRegistry registry) {
        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();

        Collection<AncientDoorBlock> doors = BlockHandler.ANCIENT_DOORS.values();
        for (Block door : doors) {
            blacklist.addIngredientToBlacklist(new ItemStack(door));
        }

        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.CULTIVATOR_TOP, 1, OreDictionary.WILDCARD_VALUE));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.DISPLAY_BLOCK));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.KRILL_SWARM));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.PLANKTON_SWARM));
//        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.TOUR_RAIL_POWERED)); TODO
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.AJUGINUCULA_SMITHII));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.WILD_ONION));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.WILD_POTATO_PLANT));
        blacklist.addIngredientToBlacklist(new ItemStack(BlockHandler.GRACILARIA));
        blacklist.addIngredientToBlacklist(new ItemStack(ItemHandler.HATCHED_EGG));

        this.addCategories(registry);
    }

    private void addCategories(IModRegistry registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

        registry.addRecipeCategories(new FossilGrinderRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new FossilGrinderRecipeHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(BlockHandler.FOSSIL_GRINDER), "jurassicraft.fossil_grinder");

        registry.addRecipeCategories(new CleaningStationRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new CleaningStationRecipeHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(BlockHandler.CLEANING_STATION), "jurassicraft.cleaning_station");

        registry.addRecipeCategories(new DNASynthesizerRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new DNASynthesizerRecipeHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(BlockHandler.DNA_SYNTHESIZER), "jurassicraft.dna_synthesizer");

        registry.addRecipeCategories(new EmbryonicRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new EmbryonicRecipeHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(BlockHandler.EMBRYONIC_MACHINE), "jurassicraft.embryonic_machine");

        registry.addRecipeCategories(new CalcificationRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new CalcificationRecipeHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(BlockHandler.EMBRYO_CALCIFICATION_MACHINE), "jurassicraft.embryo_calcification_machine");

        registry.addRecipeCategories(new SkeletonAssemblyRecipeCategory(guiHelper));
        registry.addRecipeHandlers(new SkeletonAssemblyRecipeHandler());
        registry.addRecipeCategoryCraftingItem(new ItemStack(BlockHandler.SKELETON_ASSEMBLY), "jurassicraft.skeleton_assembly");

        List<Dinosaur> registeredDinosaurs = EntityHandler.getRegisteredDinosaurs();
        List<Plant> registeredPlants = PlantHandler.getPrehistoricPlantsAndTrees();

        registry.addRecipes(registeredDinosaurs.stream().map(GrinderInput::new).collect(Collectors.toList()));
        registry.addRecipes(registeredDinosaurs.stream().map(CalcificationInput::new).collect(Collectors.toList()));

        registry.addRecipes(registeredDinosaurs.stream().map(SynthesizerInput.DinosaurInput::new).collect(Collectors.toList()));
        registry.addRecipes(registeredPlants.stream().map(SynthesizerInput.PlantInput::new).collect(Collectors.toList()));

        registry.addRecipes(registeredDinosaurs.stream().map(EmbryoInput.DinosaurInput::new).collect(Collectors.toList()));
        registry.addRecipes(registeredPlants.stream().map(EmbryoInput.PlantInput::new).collect(Collectors.toList()));

        for (Dinosaur dinosaur : registeredDinosaurs) {
            for (String bone : dinosaur.getBones()) {
                registry.addRecipes(Lists.newArrayList(new BoneInput(dinosaur, bone)));
            }
            registry.addRecipes(Lists.newArrayList(new SkeletonInput(dinosaur, false), new SkeletonInput(dinosaur, true)));
        }
    }
}
