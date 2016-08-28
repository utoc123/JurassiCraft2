package org.jurassicraft.client.render;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.model.animation.DinosaurAnimator;
import org.jurassicraft.client.model.animation.entity.BrachiosaurusAnimator;
import org.jurassicraft.client.model.animation.entity.CoelacanthAnimator;
import org.jurassicraft.client.model.animation.entity.DilophosaurusAnimator;
import org.jurassicraft.client.model.animation.entity.GallimimusAnimator;
import org.jurassicraft.client.model.animation.entity.MicroraptorAnimator;
import org.jurassicraft.client.model.animation.entity.ParasaurolophusAnimator;
import org.jurassicraft.client.model.animation.entity.TriceratopsAnimator;
import org.jurassicraft.client.model.animation.entity.TyrannosaurusAnimator;
import org.jurassicraft.client.model.animation.entity.VelociraptorAnimator;
import org.jurassicraft.client.render.block.ActionFigureSpecialRenderer;
import org.jurassicraft.client.render.block.DNACombinatorHybridizerSpecialRenderer;
import org.jurassicraft.client.render.block.DNAExtractorSpecialRenderer;
import org.jurassicraft.client.render.block.DNASequencerSpecialRenderer;
import org.jurassicraft.client.render.block.DNASynthesizerSpecialRenderer;
import org.jurassicraft.client.render.block.EmbryoCalcificationMachineSpecialRenderer;
import org.jurassicraft.client.render.block.EmbryonicMachineSpecialRenderer;
import org.jurassicraft.client.render.block.FeederSpecialRenderer;
import org.jurassicraft.client.render.block.IncubatorSpecialRenderer;
import org.jurassicraft.client.render.entity.AttractionSignRenderer;
import org.jurassicraft.client.render.entity.DinosaurEggRenderer;
import org.jurassicraft.client.render.entity.HelicopterRenderer;
import org.jurassicraft.client.render.entity.JeepWranglerRenderer;
import org.jurassicraft.client.render.entity.MuralRenderer;
import org.jurassicraft.client.render.entity.PaddockSignRenderer;
import org.jurassicraft.client.render.entity.SeatRenderer;
import org.jurassicraft.client.render.entity.VenomRenderer;
import org.jurassicraft.client.render.entity.dinosaur.DinosaurRenderInfo;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.EncasedFossilBlock;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.block.FossilizedTrackwayBlock;
import org.jurassicraft.server.block.NestFossilBlock;
import org.jurassicraft.server.block.entity.ActionFigureBlockEntity;
import org.jurassicraft.server.block.entity.DNACombinatorHybridizerBlockEntity;
import org.jurassicraft.server.block.entity.DNAExtractorBlockEntity;
import org.jurassicraft.server.block.entity.DNASequencerBlockEntity;
import org.jurassicraft.server.block.entity.DNASynthesizerBlockEntity;
import org.jurassicraft.server.block.entity.EmbryoCalcificationMachineBlockEntity;
import org.jurassicraft.server.block.entity.EmbryonicMachineBlockEntity;
import org.jurassicraft.server.block.entity.FeederBlockEntity;
import org.jurassicraft.server.block.entity.IncubatorBlockEntity;
import org.jurassicraft.server.block.tree.AncientLeavesBlock;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.entity.VenomEntity;
import org.jurassicraft.server.entity.item.AttractionSignEntity;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.jurassicraft.server.entity.item.MuralEntity;
import org.jurassicraft.server.entity.item.PaddockSignEntity;
import org.jurassicraft.server.entity.vehicle.HelicopterBaseEntity;
import org.jurassicraft.server.entity.vehicle.JeepWranglerEntity;
import org.jurassicraft.server.entity.vehicle.modules.SeatEntity;
import org.jurassicraft.server.item.DinosaurSpawnEggItem;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.item.bones.FossilItem;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@SideOnly(Side.CLIENT)
public enum RenderingHandler {
    INSTANCE;

    private final Minecraft mc = Minecraft.getMinecraft();
    private Map<Dinosaur, DinosaurRenderInfo> renderInfos = Maps.newHashMap();

    public void preInit() {
        for (Dinosaur dino : EntityHandler.getDinosaurs().values()) {
            String dinoName = dino.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            if (!(dino instanceof Hybrid)) {
                for (Map.Entry<String, FossilItem> entry : ItemHandler.FOSSILS.entrySet()) {
                    List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                    if (dinosaursForType.contains(dino)) {
                        ModelBakery.registerItemVariants(entry.getValue(), new ResourceLocation("jurassicraft:bones/" + dinoName + "_" + entry.getKey()));
                    }
                }
            }

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FRESH_FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino)) {
                    ModelBakery.registerItemVariants(entry.getValue(), new ResourceLocation("jurassicraft:fresh_bones/" + dinoName + "_" + entry.getKey()));
                }
            }

            ModelBakery.registerItemVariants(ItemHandler.DNA, new ResourceLocation("jurassicraft:dna/dna_" + dinoName));

            if (!dino.isMammal()) {
                ModelBakery.registerItemVariants(ItemHandler.EGG, new ResourceLocation("jurassicraft:egg/egg_" + dinoName));
                if (!dino.isMarineAnimal()) {
                    ModelBakery.registerItemVariants(ItemHandler.HATCHED_EGG, new ResourceLocation("jurassicraft:hatched_egg/egg_" + dinoName));
                }
            }

            ModelBakery.registerItemVariants(ItemHandler.DINOSAUR_MEAT, new ResourceLocation("jurassicraft:meat/meat_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.DINOSAUR_STEAK, new ResourceLocation("jurassicraft:meat/steak_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.SOFT_TISSUE, new ResourceLocation("jurassicraft:soft_tissue/soft_tissue_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.SYRINGE, new ResourceLocation("jurassicraft:syringe/syringe_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.ACTION_FIGURE, new ResourceLocation("jurassicraft:action_figure/action_figure_" + dinoName));
        }

        for (FossilizedTrackwayBlock.TrackwayType trackwayType : FossilizedTrackwayBlock.TrackwayType.values()) {
            ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockHandler.FOSSILIZED_TRACKWAY), new ResourceLocation("jurassicraft:fossilized_trackway_" + trackwayType.getName()));
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockHandler.NEST_FOSSIL), new ResourceLocation("jurassicraft:nest_fossil_" + (variant.ordinal() + 1)));
            ModelBakery.registerItemVariants(ItemHandler.FOSSILIZED_EGG, new ResourceLocation("jurassicraft:fossilized_egg_" + (variant.ordinal() + 1)));
        }

        for (Plant plant : PlantHandler.getPrehistoricPlants()) {
            String name = plant.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            ModelBakery.registerItemVariants(ItemHandler.PLANT_DNA, new ResourceLocation("jurassicraft:dna/plants/dna_" + name));
            ModelBakery.registerItemVariants(ItemHandler.PLANT_SOFT_TISSUE, new ResourceLocation("jurassicraft:soft_tissue/plants/soft_tissue_" + name));
        }

//        for (EnumDyeColor color : EnumDyeColor.values())
//        {
//            ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockHandler.CULTIVATOR_BOTTOM), new ResourceLocation("jurassicraft:cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH)));
//        }

        for (AttractionSignEntity.AttractionSignType type : AttractionSignEntity.AttractionSignType.values()) {
            ModelBakery.registerItemVariants(ItemHandler.ATTRACTION_SIGN, new ResourceLocation("jurassicraft:attraction_sign_" + type.name().toLowerCase(Locale.ENGLISH)));
        }

        ModelBakery.registerItemVariants(ItemHandler.AMBER, new ResourceLocation("jurassicraft:amber_aphid"), new ResourceLocation("jurassicraft:amber_mosquito"));

        this.registerRenderInfo(EntityHandler.BRACHIOSAURUS, new BrachiosaurusAnimator(), 1.5F);
        this.registerRenderInfo(EntityHandler.COELACANTH, new CoelacanthAnimator(), 0.0F);
        this.registerRenderInfo(EntityHandler.DILOPHOSAURUS, new DilophosaurusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.GALLIMIMUS, new GallimimusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.PARASAUROLOPHUS, new ParasaurolophusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.MICRORAPTOR, new MicroraptorAnimator(), 0.45F);
        this.registerRenderInfo(EntityHandler.TRICERATOPS, new TriceratopsAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.TYRANNOSAURUS, new TyrannosaurusAnimator(), 0.65F);
        this.registerRenderInfo(EntityHandler.VELOCIRAPTOR, new VelociraptorAnimator(), 0.45F);

        RenderingRegistry.registerEntityRenderingHandler(PaddockSignEntity.class, new PaddockSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(AttractionSignEntity.class, new AttractionSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(HelicopterBaseEntity.class, new HelicopterRenderer());
        RenderingRegistry.registerEntityRenderingHandler(DinosaurEggEntity.class, new DinosaurEggRenderer());
        RenderingRegistry.registerEntityRenderingHandler(VenomEntity.class, new VenomRenderer());
        RenderingRegistry.registerEntityRenderingHandler(JeepWranglerEntity.class, new JeepWranglerRenderer());
        RenderingRegistry.registerEntityRenderingHandler(SeatEntity.class, new SeatRenderer());
        RenderingRegistry.registerEntityRenderingHandler(MuralEntity.class, new MuralRenderer());
    }

    public void init() {
        ItemModelMesher modelMesher = this.mc.getRenderItem().getItemModelMesher();

        int i = 0;

        for (EncasedFossilBlock fossil : BlockHandler.ENCASED_FOSSILS) {
            this.registerBlockRenderer(modelMesher, fossil, "encased_fossil_" + i, "inventory");

            i++;
        }

        i = 0;

        for (FossilBlock fossil : BlockHandler.FOSSILS) {
            this.registerBlockRenderer(modelMesher, fossil, "fossil_block_" + i, "inventory");

            i++;
        }

        this.registerBlockRenderer(modelMesher, BlockHandler.PLANT_FOSSIL, "plant_fossil_block", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.FOSSILIZED_TRACKWAY, "fossilized_trackway", "inventory");

        for (TreeType type : TreeType.values()) {
            String name = type.name().toLowerCase(Locale.ENGLISH);
            this.registerBlockRenderer(modelMesher, BlockHandler.ANCIENT_LEAVES.get(type), name + "_leaves", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.ANCIENT_SAPLINGS.get(type), name + "_sapling", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.ANCIENT_PLANKS.get(type), name + "_planks", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.ANCIENT_LOGS.get(type), name + "_log", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.ANCIENT_STAIRS.get(type), name + "_stairs", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.ANCIENT_SLABS.get(type), name + "_slab", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.ANCIENT_DOUBLE_SLABS.get(type), name + "_double_sab", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.PETRIFIED_LOGS.get(type), name + "_log_petrified", "inventory");
        }

        for (EnumDyeColor color : EnumDyeColor.values()) {
            this.registerBlockRenderer(modelMesher, BlockHandler.CULTIVATOR_BOTTOM, color.ordinal(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(Locale.ENGLISH), "inventory");
        }

        this.registerBlockRenderer(modelMesher, BlockHandler.SCALY_TREE_FERN, "scaly_tree_fern", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.SMALL_ROYAL_FERN, "small_royal_fern", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.SMALL_CHAIN_FERN, "small_chain_fern", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.SMALL_CYCAD, "small_cycad", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.CYCADEOIDEA, "bennettitalean_cycadeoidea", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.CRY_PANSY, "cry_pansy", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.ZAMITES, "cycad_zamites", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.DICKSONIA, "dicksonia", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.REINFORCED_STONE, "reinforced_stone", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.REINFORCED_BRICKS, "reinforced_bricks", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.CULTIVATOR_BOTTOM, "cultivate_bottom", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.CULTIVATOR_TOP, "cultivate_bottom", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.AMBER_ORE, "amber_ore", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.ICE_SHARD, "ice_shard", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.CLEANING_STATION, "cleaning_station", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.FOSSIL_GRINDER, "fossil_grinder", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.DNA_SEQUENCER, "dna_sequencer", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.DNA_COMBINATOR_HYBRIDIZER, "dna_combinator_hybridizer", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.DNA_SYNTHESIZER, "dna_synthesizer", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.EMBRYONIC_MACHINE, "embryonic_machine", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.EMBRYO_CALCIFICATION_MACHINE, "embryo_calcification_machine", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INCUBATOR, "incubator", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.DNA_EXTRACTOR, "dna_extractor", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.FEEDER, "feeder", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.GYPSUM_STONE, "gypsum_stone", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.GYPSUM_COBBLESTONE, "gypsum_cobblestone", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.GYPSUM_BRICKS, "gypsum_bricks", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.ACTION_FIGURE, "action_figure_block", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.MOSS, "moss", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.CLEAR_GLASS, "clear_glass", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.WILD_ONION, "wild_onion_plant", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.GRACILARIA, "graciliaria_seaweed", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.PEAT, "peat", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.PEAT_MOSS, "peat_moss", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.DICROIDIUM_ZUBERI, "dicroidium_zuberi", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.WEST_INDIAN_LILAC, "west_indian_lilac", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.DICTYOPHYLLUM, "dictyophyllum", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.SERENNA_VERIFORMANS, "serenna_veriformans", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.LADINIA_SIMPLEX, "ladinia_simplex", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.ORONTIUM_MACKII, "orontium_mackii", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.UMALTOLEPIS, "umaltolepis", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.LIRIODENDRITES, "liriodendrites", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.RAPHAELIA, "raphaelia", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.ENCEPHALARTOS, "encephalartos", "inventory");

        for (FossilizedTrackwayBlock.TrackwayType trackwayType : FossilizedTrackwayBlock.TrackwayType.values()) {
            this.registerBlockRenderer(modelMesher, BlockHandler.FOSSILIZED_TRACKWAY, trackwayType.ordinal(), "fossilized_trackway_" + trackwayType.getName(), "inventory");
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            this.registerBlockRenderer(modelMesher, BlockHandler.NEST_FOSSIL, variant.ordinal(), "nest_fossil_" + (variant.ordinal() + 1), "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.ENCASED_NEST_FOSSIL, variant.ordinal(), "encased_nest_fossil", "inventory");
        }

        this.registerBlockRenderer(modelMesher, BlockHandler.PALEO_BALE_CYCADEOIDEA, "paleo_bale_cycadeoidea", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.PALEO_BALE_CYCAD, "paleo_bale_cycad", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.PALEO_BALE_FERN, "paleo_bale_fern", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.PALEO_BALE_LEAVES, "paleo_bale_leaves", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.PALEO_BALE_OTHER, "paleo_bale_other", "inventory");

        BlockColors blockColors = this.mc.getBlockColors();
        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos != null ? BiomeColorHelper.getGrassColorAtPos(access, pos) : 0xFFFFFF, BlockHandler.MOSS);

        for (Map.Entry<TreeType, AncientLeavesBlock> entry : BlockHandler.ANCIENT_LEAVES.entrySet()) {
            blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), entry.getValue());
        }

        blockColors.registerBlockColorHandler((state, access, pos, tintIndex) -> pos == null ? ColorizerFoliage.getFoliageColorBasic() : BiomeColorHelper.getFoliageColorAtPos(access, pos), BlockHandler.MOSS);

        ItemColors itemColors = this.mc.getItemColors();

        for (Map.Entry<TreeType, AncientLeavesBlock> entry : BlockHandler.ANCIENT_LEAVES.entrySet()) {
            itemColors.registerItemColorHandler((stack, tintIndex) -> ColorizerFoliage.getFoliageColorBasic(), entry.getValue());
        }

        this.registerBlockRenderer(modelMesher, BlockHandler.AJUGINUCULA_SMITHII, "ajuginucula_smithii", "inventory");
    }

    public void postInit() {
        ClientRegistry.bindTileEntitySpecialRenderer(DNAExtractorBlockEntity.class, new DNAExtractorSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ActionFigureBlockEntity.class, new ActionFigureSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DNASequencerBlockEntity.class, new DNASequencerSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EmbryoCalcificationMachineBlockEntity.class, new EmbryoCalcificationMachineSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DNACombinatorHybridizerBlockEntity.class, new DNACombinatorHybridizerSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EmbryonicMachineBlockEntity.class, new EmbryonicMachineSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DNASynthesizerBlockEntity.class, new DNASynthesizerSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(IncubatorBlockEntity.class, new IncubatorSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(FeederBlockEntity.class, new FeederSpecialRenderer());

        RenderItem renderItem = this.mc.getRenderItem();
        ItemModelMesher modelMesher = renderItem.getItemModelMesher();

        // Items
        this.registerItemRenderer(modelMesher, ItemHandler.TRACKER, "tracker", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PLANT_CELLS_PETRI_DISH, "plant_cells_petri_dish", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PLANT_CELLS, "plant_cells", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PLANT_CALLUS, "plant_callus", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.GROWTH_SERUM, "growth_serum", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.IRON_ROD, "iron_rod", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.IRON_BLADES, "iron_blades", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PETRI_DISH, "petri_dish", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PETRI_DISH_AGAR, "petri_dish_agar", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.AMBER, "amber", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PLASTER_AND_BANDAGE, "plaster_and_bandage", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.SPAWN_EGG, "dino_spawn_egg", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PADDOCK_SIGN, "paddock_sign", "inventory");

        for (AttractionSignEntity.AttractionSignType type : AttractionSignEntity.AttractionSignType.values()) {
            this.registerItemRenderer(modelMesher, ItemHandler.ATTRACTION_SIGN, type.ordinal(), "attraction_sign_" + type.name().toLowerCase(Locale.ENGLISH), "inventory");
        }

        this.registerItemRenderer(modelMesher, ItemHandler.EMPTY_TEST_TUBE, "empty_test_tube", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.EMPTY_SYRINGE, "empty_syringe", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.STORAGE_DISC, "storage_disc", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.HARD_DRIVE, "disc_reader", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.LASER, "laser", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.DNA_NUCLEOTIDES, "dna_base_material", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.SEA_LAMPREY, "sea_lamprey", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.AMBER, 0, "amber_mosquito", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.AMBER, 1, "amber_aphid", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.HELICOPTER, "helicopter_spawner", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.JURASSICRAFT_THEME_DISC, "disc_jurassicraft_theme", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.DONT_MOVE_A_MUSCLE_DISC, "disc_dont_move_a_muscle", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.TROODONS_AND_RAPTORS_DISC, "disc_troodons_and_raptors", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.AMBER_KEYCHAIN, "amber_keychain", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.AMBER_CANE, "amber_cane", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.MR_DNA_KEYCHAIN, "mr_dna_keychain", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.DINO_SCANNER, "dino_scanner", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.BASIC_CIRCUIT, "basic_circuit", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.ADVANCED_CIRCUIT, "advanced_circuit", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.IRON_NUGGET, "iron_nugget", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.GYPSUM_POWDER, "gypsum_powder", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.AJUGINUCULA_SMITHII_SEEDS, "ajuginucula_smithii_seeds", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.AJUGINUCULA_SMITHII_LEAVES, "ajuginucula_smithii_leaves", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.AJUGINUCULA_SMITHII_OIL, "ajuginucula_smithii_oil", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.WILD_ONION, "wild_onion", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.GRACILARIA, "gracilaria", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.LIQUID_AGAR, "liquid_agar", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.PLANT_FOSSIL, "plant_fossil", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.TWIG_FOSSIL, "twig_fossil", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.KEYBOARD, "keyboard", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.COMPUTER_SCREEN, "computer_screen", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.DNA_ANALYZER, "dna_analyzer", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.CHILEAN_SEA_BASS, "chilean_sea_bass", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.FIELD_GUIDE, "field_guide", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.CAR_CHASSIS, "car_chassis", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.CAR_ENGINE_SYSTEM, "car_engine_system", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.CAR_SEATS, "car_seats", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.CAR_TIRE, "car_tire", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.CAR_WINDSCREEN, "car_windscreen", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.UNFINISHED_CAR, "unfinished_car", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.JEEP_WRANGLER, "jeep_wrangler", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.MURAL, "mural", "inventory");

        for (Dinosaur dinosaur : EntityHandler.getDinosaurs().values()) {
            int meta = EntityHandler.getDinosaurId(dinosaur);

            String formattedName = dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                 if (dinosaursForType.contains(dinosaur)) {
                    this.registerItemRenderer(modelMesher, entry.getValue(), meta, "bones/" + formattedName + "_" + entry.getKey(), "inventory");
                }
            }

            for (Map.Entry<String, FossilItem> entry : ItemHandler.FRESH_FOSSILS.entrySet()) {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());
                if (dinosaursForType.contains(dinosaur)) {
                    this.registerItemRenderer(modelMesher, entry.getValue(), meta, "fresh_bones/" + formattedName + "_" + entry.getKey(), "inventory");
                }
            }

            this.registerItemRenderer(modelMesher, ItemHandler.DNA, meta, "dna/dna_" + formattedName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.EGG, meta, "egg/egg_" + formattedName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.DINOSAUR_MEAT, meta, "meat/meat_" + formattedName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.DINOSAUR_STEAK, meta, "meat/steak_" + formattedName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.SOFT_TISSUE, meta, "soft_tissue/soft_tissue_" + formattedName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.SYRINGE, meta, "syringe/syringe_" + formattedName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.ACTION_FIGURE, meta, "action_figure/action_figure_" + formattedName, "inventory");
            if (!dinosaur.isMarineAnimal()) {
                this.registerItemRenderer(modelMesher, ItemHandler.HATCHED_EGG, meta, "hatched_egg/egg_" + formattedName, "inventory");
            }
        }

        for (Plant plant : PlantHandler.getPrehistoricPlants()) {
            int meta = PlantHandler.getPlantId(plant);

            String name = plant.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");

            this.registerItemRenderer(modelMesher, ItemHandler.PLANT_DNA, meta, "dna/plants/dna_" + name, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.PLANT_SOFT_TISSUE, meta, "soft_tissue/plants/soft_tissue_" + name, "inventory");
        }

        for (NestFossilBlock.Variant variant : NestFossilBlock.Variant.values()) {
            this.registerItemRenderer(modelMesher, ItemHandler.FOSSILIZED_EGG, variant.ordinal(), "fossilized_egg_" + (variant.ordinal() + 1), "inventory");
        }

        this.registerItemRenderer(modelMesher, ItemHandler.PHOENIX_SEEDS, "phoenix_seeds", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.PHOENIX_FRUIT, "phoenix_fruit", "inventory");

        ItemColors itemColors = this.mc.getItemColors();
        itemColors.registerItemColorHandler((stack, tintIndex) -> {
            DinosaurSpawnEggItem item = (DinosaurSpawnEggItem) stack.getItem();
            Dinosaur dino = item.getDinosaur(stack);

            if (dino != null) {
                int mode = item.getMode(stack);

                if (mode == 0) {
                    mode = JurassiCraft.timerTicks % 64 > 32 ? 1 : 2;
                }

                if (mode == 1) {
                    return tintIndex == 0 ? dino.getEggPrimaryColorMale() : dino.getEggSecondaryColorMale();
                } else {
                    return tintIndex == 0 ? dino.getEggPrimaryColorFemale() : dino.getEggSecondaryColorFemale();
                }
            }

            return 0xFFFFFF;
        }, ItemHandler.SPAWN_EGG);
    }

    public void registerItemRenderer(ItemModelMesher itemModelMesher, Item item, final String path, final String type) {
        itemModelMesher.register(item, stack -> new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    public void registerItemRenderer(ItemModelMesher itemModelMesher, Item item, int meta, String path, String type) {
        itemModelMesher.register(item, meta, new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    public void registerBlockRenderer(ItemModelMesher itemModelMesher, Block block, int meta, String path, String type) {
        itemModelMesher.register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    public void registerBlockRenderer(ItemModelMesher itemModelMesher, Block block, final String path, final String type) {
        itemModelMesher.register(Item.getItemFromBlock(block), stack -> new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    private void registerRenderInfo(Dinosaur dinosaur, DinosaurAnimator<?> animator, float shadowSize) {
        this.registerRenderInfo(new DinosaurRenderInfo(dinosaur, animator, shadowSize));
    }

    private void registerRenderInfo(DinosaurRenderInfo renderDef) {
        this.renderInfos.put(renderDef.getDinosaur(), renderDef);
        RenderingRegistry.registerEntityRenderingHandler(renderDef.getDinosaur().getDinosaurClass(), renderDef);
    }

    public DinosaurRenderInfo getRenderInfo(Dinosaur dino) {
        return this.renderInfos.get(dino);
    }
}
