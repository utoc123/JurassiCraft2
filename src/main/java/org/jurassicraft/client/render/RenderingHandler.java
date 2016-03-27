package org.jurassicraft.client.render;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.client.gui.app.GuiAppRegistry;
import org.jurassicraft.client.model.animation.AchillobatorAnimator;
import org.jurassicraft.client.model.animation.AnkylosaurusAnimator;
import org.jurassicraft.client.model.animation.ApatosaurusAnimator;
import org.jurassicraft.client.model.animation.BaryonyxAnimator;
import org.jurassicraft.client.model.animation.BrachiosaurusAnimator;
import org.jurassicraft.client.model.animation.CarnotaurusAnimator;
import org.jurassicraft.client.model.animation.CearadactylusAnimator;
import org.jurassicraft.client.model.animation.ChasmosaurusAnimator;
import org.jurassicraft.client.model.animation.CoelacanthAnimator;
import org.jurassicraft.client.model.animation.CompsognathusAnimator;
import org.jurassicraft.client.model.animation.CorythosaurusAnimator;
import org.jurassicraft.client.model.animation.DilophosaurusAnimator;
import org.jurassicraft.client.model.animation.DimorphodonAnimator;
import org.jurassicraft.client.model.animation.DodoAnimator;
import org.jurassicraft.client.model.animation.DunkleosteusAnimator;
import org.jurassicraft.client.model.animation.EdmontosaurusAnimator;
import org.jurassicraft.client.model.animation.GallimimusAnimator;
import org.jurassicraft.client.model.animation.GiganotosaurusAnimator;
import org.jurassicraft.client.model.animation.HerrerasaurusAnimator;
import org.jurassicraft.client.model.animation.HypsilophodonAnimator;
import org.jurassicraft.client.model.animation.LambeosaurusAnimator;
import org.jurassicraft.client.model.animation.LeaellynasauraAnimator;
import org.jurassicraft.client.model.animation.LeptictidiumAnimator;
import org.jurassicraft.client.model.animation.LudodactylusAnimator;
import org.jurassicraft.client.model.animation.MajungasaurusAnimator;
import org.jurassicraft.client.model.animation.MamenchisaurusAnimator;
import org.jurassicraft.client.model.animation.MegapiranhaAnimator;
import org.jurassicraft.client.model.animation.MetriacanthosaurusAnimator;
import org.jurassicraft.client.model.animation.MicroceratusAnimator;
import org.jurassicraft.client.model.animation.MoganopterusAnimator;
import org.jurassicraft.client.model.animation.OrnithomimusAnimator;
import org.jurassicraft.client.model.animation.OthnieliaAnimator;
import org.jurassicraft.client.model.animation.OviraptorAnimator;
import org.jurassicraft.client.model.animation.PachycephalosaurusAnimator;
import org.jurassicraft.client.model.animation.ParasaurolophusAnimator;
import org.jurassicraft.client.model.animation.ProtoceratopsAnimator;
import org.jurassicraft.client.model.animation.PteranodonAnimator;
import org.jurassicraft.client.model.animation.RugopsAnimator;
import org.jurassicraft.client.model.animation.SegisaurusAnimator;
import org.jurassicraft.client.model.animation.SpinosaurusAnimator;
import org.jurassicraft.client.model.animation.StegosaurusAnimator;
import org.jurassicraft.client.model.animation.TherizinosaurusAnimator;
import org.jurassicraft.client.model.animation.TriceratopsAnimator;
import org.jurassicraft.client.model.animation.TroodonAnimator;
import org.jurassicraft.client.model.animation.TropeognathusAnimator;
import org.jurassicraft.client.model.animation.TylosaurusAnimator;
import org.jurassicraft.client.model.animation.TyrannosaurusAnimator;
import org.jurassicraft.client.model.animation.VelociraptorAnimator;
import org.jurassicraft.client.model.animation.VelociraptorBlueAnimator;
import org.jurassicraft.client.model.animation.VelociraptorCharlieAnimator;
import org.jurassicraft.client.model.animation.VelociraptorDeltaAnimator;
import org.jurassicraft.client.model.animation.VelociraptorEchoAnimator;
import org.jurassicraft.client.model.animation.ZhenyuanopterusAnimator;
import org.jurassicraft.client.render.block.ActionFigureSpecialRenderer;
import org.jurassicraft.client.render.block.DNACombinatorHybridizerSpecialRenderer;
import org.jurassicraft.client.render.block.DNAExtractorSpecialRenderer;
import org.jurassicraft.client.render.block.DNASequencerSpecialRenderer;
import org.jurassicraft.client.render.block.DNASynthesizerSpecialRenderer;
import org.jurassicraft.client.render.block.EmbryoCalcificationMachineSpecialRenderer;
import org.jurassicraft.client.render.block.EmbryonicMachineSpecialRenderer;
import org.jurassicraft.client.render.block.IncubatorSpecialRenderer;
import org.jurassicraft.client.render.entity.BluePrintRenderer;
import org.jurassicraft.client.render.entity.CageSmallRenderer;
import org.jurassicraft.client.render.entity.DinosaurEggRenderer;
import org.jurassicraft.client.render.entity.HelicopterRenderer;
import org.jurassicraft.client.render.entity.JurassiCraftSignRenderer;
import org.jurassicraft.client.render.entity.PaddockSignRenderer;
import org.jurassicraft.client.render.renderdef.IndominusRenderDef;
import org.jurassicraft.client.render.renderdef.RenderDinosaurDefinition;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.EncasedFossilBlock;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.entity.item.BluePrintEntity;
import org.jurassicraft.server.entity.item.CageSmallEntity;
import org.jurassicraft.server.entity.item.DinosaurEggEntity;
import org.jurassicraft.server.entity.item.JurassiCraftSignEntity;
import org.jurassicraft.server.entity.item.PaddockSignEntity;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.item.bones.FossilItem;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;
import org.jurassicraft.server.tileentity.ActionFigureTile;
import org.jurassicraft.server.tileentity.DNACombinatorHybridizerTile;
import org.jurassicraft.server.tileentity.DNAExtractorTile;
import org.jurassicraft.server.tileentity.DNASequencerTile;
import org.jurassicraft.server.tileentity.DNASynthesizerTile;
import org.jurassicraft.server.tileentity.EmbryoCalcificationMachineTile;
import org.jurassicraft.server.tileentity.EmbryonicMachineTile;
import org.jurassicraft.server.tileentity.IncubatorTile;
import org.jurassicraft.server.vehicles.helicopter.HelicopterBaseEntity;

import java.util.List;
import java.util.Map;

@SideOnly(Side.CLIENT)
public enum RenderingHandler
{
    INSTANCE;

    private Map<Dinosaur, RenderDinosaurDefinition<?>> renderDefs = Maps.newHashMap();
    private final Minecraft mc = Minecraft.getMinecraft();

    public void preInit()
    {
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.achillobator, new AchillobatorAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.anklyosaurus, new AnkylosaurusAnimator(), 0.85F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.brachiosaurus, new BrachiosaurusAnimator(), 1.5F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.carnotaurus, new CarnotaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.coelacanth, new CoelacanthAnimator(), 0.35F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.compsognathus, new CompsognathusAnimator(), 1.8F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.dilophosaurus, new DilophosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.dunkleosteus, new DunkleosteusAnimator(), 0.35F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.gallimimus, new GallimimusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.giganotosaurus, new GiganotosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.hypsilophodon, new HypsilophodonAnimator(), 0.65F));
        registerRenderDef(new IndominusRenderDef(0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.majungasaurus, new MajungasaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.parasaurolophus, new ParasaurolophusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.pteranodon, new PteranodonAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.rugops, new RugopsAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.segisaurus, new SegisaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.spinosaurus, new SpinosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.stegosaurus, new StegosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.triceratops, new TriceratopsAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.tyrannosaurus, new TyrannosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.velociraptor, new VelociraptorAnimator(), 0.45F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.dodo, new DodoAnimator(), 0.5F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.leptictidium, new LeptictidiumAnimator(), 0.45F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.microceratus, new MicroceratusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.oviraptor, new OviraptorAnimator(), 0.55F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.apatosaurus, new ApatosaurusAnimator(), 1.5F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.othnielia, new OthnieliaAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.dimorphodon, new DimorphodonAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.tylosaurus, new TylosaurusAnimator(), 0.85F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.ludodactylus, new LudodactylusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.protoceratops, new ProtoceratopsAnimator(), 0.55F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.tropeognathus, new TropeognathusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.leaellynasaura, new LeaellynasauraAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.herrerasaurus, new HerrerasaurusAnimator(), 0.75F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.velociraptor_blue, new VelociraptorBlueAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.velociraptor_charlie, new VelociraptorCharlieAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.velociraptor_delta, new VelociraptorDeltaAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.velociraptor_echo, new VelociraptorEchoAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.therizinosaurus, new TherizinosaurusAnimator(), 0.55F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.megapiranha, new MegapiranhaAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.baryonyx, new BaryonyxAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.cearadactylus, new CearadactylusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.mamenchisaurus, new MamenchisaurusAnimator(), 1.5F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.chasmosaurus, new ChasmosaurusAnimator(), 0.85F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.corythosaurus, new CorythosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.edmontosaurus, new EdmontosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.lambeosaurus, new LambeosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.metriacanthosaurus, new MetriacanthosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.moganopterus, new MoganopterusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.ornithomimus, new OrnithomimusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.zhenyuanopterus, new ZhenyuanopterusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.troodon, new TroodonAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition<>(EntityHandler.INSTANCE.pachycephalosaurus, new PachycephalosaurusAnimator(), 0.65F));

        for (Dinosaur dino : EntityHandler.INSTANCE.getDinosaurs())
        {
            RenderingRegistry.registerEntityRenderingHandler(dino.getDinosaurClass(), renderDefs.get(dino));
        }

        RenderingRegistry.registerEntityRenderingHandler(CageSmallEntity.class, new CageSmallRenderer());
        RenderingRegistry.registerEntityRenderingHandler(BluePrintEntity.class, new BluePrintRenderer());
        RenderingRegistry.registerEntityRenderingHandler(PaddockSignEntity.class, new PaddockSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(JurassiCraftSignEntity.class, new JurassiCraftSignRenderer());
        RenderingRegistry.registerEntityRenderingHandler(HelicopterBaseEntity.class, new HelicopterRenderer());
        RenderingRegistry.registerEntityRenderingHandler(DinosaurEggEntity.class, new DinosaurEggRenderer());

        for (Dinosaur dino : EntityHandler.INSTANCE.getDinosaurs())
        {
            String dinoName = dino.getName().toLowerCase().replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : ItemHandler.INSTANCE.fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    ModelBakery.registerItemVariants(entry.getValue(), new ResourceLocation("jurassicraft:bones/" + dinoName + "_" + entry.getKey()));
                }
            }

            for (Map.Entry<String, FossilItem> entry : ItemHandler.INSTANCE.fresh_fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    ModelBakery.registerItemVariants(entry.getValue(), new ResourceLocation("jurassicraft:fresh_bones/" + dinoName + "_" + entry.getKey()));
                }
            }

            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.dna, new ResourceLocation("jurassicraft:dna/dna_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.egg, new ResourceLocation("jurassicraft:egg/egg_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.dino_meat, new ResourceLocation("jurassicraft:meat/meat_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.dino_steak, new ResourceLocation("jurassicraft:meat/steak_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.soft_tissue, new ResourceLocation("jurassicraft:soft_tissue/soft_tissue_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.syringe, new ResourceLocation("jurassicraft:syringe/syringe_" + dinoName));
            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.action_figure, new ResourceLocation("jurassicraft:action_figure/action_figure_" + dinoName));
        }

        for (Plant plant : PlantHandler.INSTANCE.getPlants())
        {
            String name = plant.getName().toLowerCase().replaceAll(" ", "_");

            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.plant_dna, new ResourceLocation("jurassicraft:dna/plants/dna_" + name));
            ModelBakery.registerItemVariants(ItemHandler.INSTANCE.plant_soft_tissue, new ResourceLocation("jurassicraft:soft_tissue/plants/soft_tissue_" + name));
        }

        for (EnumDyeColor color : EnumDyeColor.values())
        {
            ModelBakery.registerItemVariants(Item.getItemFromBlock(BlockHandler.INSTANCE.cultivate_bottom), new ResourceLocation("jurassicraft:cultivate/cultivate_bottom_" + color.getName().toLowerCase()));
        }

        ModelBakery.registerItemVariants(ItemHandler.INSTANCE.amber, new ResourceLocation("jurassicraft:amber_aphid"), new ResourceLocation("jurassicraft:amber_mosquito"));

        ModelBakery.registerItemVariants(ItemHandler.INSTANCE.cage_small, new ResourceLocation("jurassicraft:cage_small"), new ResourceLocation("jurassicraft:cage_small_marine"));
    }

    public void init()
    {
        GuiAppRegistry.register();

        // Blocks
        ItemModelMesher modelMesher = mc.getRenderItem().getItemModelMesher();

        int i = 0;

        for (EncasedFossilBlock fossil : BlockHandler.INSTANCE.encased_fossils)
        {
            this.registerBlockRenderer(modelMesher, fossil, "encased_fossil_" + i, "inventory");

            i++;
        }

        i = 0;

        for (FossilBlock fossil : BlockHandler.INSTANCE.fossils)
        {
            this.registerBlockRenderer(modelMesher, fossil, "fossil_block_" + i, "inventory");

            i++;
        }

        for (TreeType type : TreeType.values())
        {
            String name = type.name().toLowerCase();
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.leaves.get(type), name + "_leaves", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.saplings.get(type), name + "_sapling", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.planks.get(type), name + "_planks", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.logs.get(type), name + "_log", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.stairs.get(type), name + "_stairs", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.slabs.get(type), name + "_slab", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.double_slabs.get(type), name + "_double_sab", "inventory");
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.petrified_logs.get(type), name + "_log_petrified", "inventory");
        }

        for (EnumDyeColor color : EnumDyeColor.values())
        {
            this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.cultivate_bottom, color.ordinal(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(), "inventory");
        }

        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.scaly_tree_fern, "scaly_tree_fern", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.small_royal_fern, "small_royal_fern", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.small_chain_fern, "small_chain_fern", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.small_cycad, "small_cycad", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.bennettitalean_cycadeoidea, "bennettitalean_cycadeoidea", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.cry_pansy, "cry_pansy", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.cycad_zamites, "cycad_zamites", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.dicksonia, "dicksonia", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.reinforced_stone, "reinforced_stone", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.reinforced_bricks, "reinforced_bricks", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.cultivate_bottom, "cultivate_bottom", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.cultivate_top, "cultivate_top", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.amber_ore, "amber_ore", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.ice_shard, "ice_shard", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.cleaning_station, "cleaning_station", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.fossil_grinder, "fossil_grinder", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.dna_sequencer, "dna_sequencer", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.dna_combinator_hybridizer, "dna_combinator_hybridizer", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.dna_synthesizer, "dna_synthesizer", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.embryonic_machine, "embryonic_machine", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.embryo_calcification_machine, "embryo_calcification_machine", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.incubator, "incubator", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.dna_extractor, "dna_extractor", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.gypsum_stone, "gypsum_stone", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.gypsum_cobblestone, "gypsum_cobblestone", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.gypsum_bricks, "gypsum_bricks", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.action_figure, "action_figure_block", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.moss, "moss", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.clear_glass, "clear_glass", "inventory");

        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.ajuginucula_smithii, "ajuginucula_smithii", "inventory");
        this.registerBlockRenderer(modelMesher, BlockHandler.INSTANCE.wild_onion, "wild_onion_plant", "inventory");

//        this.registerRenderSubBlock(BlockHandler.INSTANCE.bPlanks);
    }

    public void postInit()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(DNAExtractorTile.class, new DNAExtractorSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(ActionFigureTile.class, new ActionFigureSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DNASequencerTile.class, new DNASequencerSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EmbryoCalcificationMachineTile.class, new EmbryoCalcificationMachineSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DNACombinatorHybridizerTile.class, new DNACombinatorHybridizerSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(EmbryonicMachineTile.class, new EmbryonicMachineSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(DNASynthesizerTile.class, new DNASynthesizerSpecialRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(IncubatorTile.class, new IncubatorSpecialRenderer());

        RenderItem renderItem = mc.getRenderItem();
        ItemModelMesher modelMesher = renderItem.getItemModelMesher();

        // Items
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.tracker, "tracker", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.plant_cells_petri_dish, "plant_cells_petri_dish", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.plant_cells, "plant_cells", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.plant_callus, "plant_callus", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.needle, "needle", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.growth_serum, "growth_serum", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.iron_rod, "iron_rod", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.iron_blades, "iron_blades", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.cage_small, 0, "cage_small", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.cage_small, 1, "cage_small_marine", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.petri_dish, "petri_dish", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.amber, "amber", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.plaster_and_bandage, "plaster_and_bandage", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.spawn_egg, "dino_spawn_egg", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.paleo_pad, "paleo_pad", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.blue_print, "blue_print", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.paddock_sign, "paddock_sign", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.jc_sign, "jurassicraft_sign", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.empty_test_tube, "empty_test_tube", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.empty_syringe, "empty_syringe", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.storage_disc, "storage_disc", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.disc_reader, "disc_reader", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.laser, "laser", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.dna_base, "dna_base_material", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.sea_lamprey, "sea_lamprey", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.amber, 0, "amber_mosquito", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.amber, 1, "amber_aphid", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.helicopter_spawner, "helicopter_spawner", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.disc_jurassicraft_theme, "disc_jurassicraft_theme", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.disc_dont_move_a_muscle, "disc_dont_move_a_muscle", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.disc_troodons_and_raptors, "disc_troodons_and_raptors", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.amber_keychain, "amber_keychain", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.amber_cane, "amber_cane", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.mr_dna_keychain, "mr_dna_keychain", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.dino_scanner, "dino_scanner", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.basic_circuit, "basic_circuit", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.advanced_circuit, "advanced_circuit", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.iron_nugget, "iron_nugget", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.gypsum_powder, "gypsum_powder", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.ajuginucula_smithii_seeds, "ajuginucula_smithii_seeds", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.ajuginucula_smithii_leaves, "ajuginucula_smithii_leaves", "inventory");
        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.ajuginucula_smithii_oil, "ajuginucula_smithii_oil", "inventory");

        this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.wild_onion, "wild_onion", "inventory");

        int meta = 0;

        for (Dinosaur dino : EntityHandler.INSTANCE.getDinosaurs())
        {
            String dinoName = dino.getName().toLowerCase().replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : ItemHandler.INSTANCE.fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    this.registerItemRenderer(modelMesher, entry.getValue(), meta, "bones/" + dinoName + "_" + entry.getKey(), "inventory");
                }
            }

            for (Map.Entry<String, FossilItem> entry : ItemHandler.INSTANCE.fresh_fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    this.registerItemRenderer(modelMesher, entry.getValue(), meta, "fresh_bones/" + dinoName + "_" + entry.getKey(), "inventory");
                }
            }

            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.dna, meta, "dna/dna_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.egg, meta, "egg/egg_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.dino_meat, meta, "meat/meat_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.dino_steak, meta, "meat/steak_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.soft_tissue, meta, "soft_tissue/soft_tissue_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.syringe, meta, "syringe/syringe_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.action_figure, meta, "action_figure/action_figure_" + dinoName, "inventory");

            meta++;
        }

        meta = 0;

        for (Plant plant : PlantHandler.INSTANCE.getPlants())
        {
            String name = plant.getName().toLowerCase().replaceAll(" ", "_");

            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.plant_dna, meta, "dna/plants/dna_" + name, "inventory");
            this.registerItemRenderer(modelMesher, ItemHandler.INSTANCE.plant_soft_tissue, meta, "soft_tissue/plants/soft_tissue_" + name, "inventory");

            meta++;
        }
    }

    /**
     * Registers an item renderer
     */
    public void registerItemRenderer(ItemModelMesher itemModelMesher, Item item, final String path, final String type)
    {
        itemModelMesher.register(item, stack -> new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    /**
     * Registers an item renderer with metadata
     */
    public void registerItemRenderer(ItemModelMesher itemModelMesher, Item item, int meta, String path, String type)
    {
        itemModelMesher.register(item, meta, new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    /**
     * Registers an block renderer with metadata
     */
    public void registerBlockRenderer(ItemModelMesher itemModelMesher, Block block, int meta, String path, String type)
    {
        itemModelMesher.register(Item.getItemFromBlock(block), meta, new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    /**
     * Registers a block renderer
     */
    public void registerBlockRenderer(ItemModelMesher itemModelMesher, Block block, final String path, final String type)
    {
        itemModelMesher.register(Item.getItemFromBlock(block), stack -> new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type));
    }

    private void registerRenderDef(RenderDinosaurDefinition renderDef)
    {
        renderDefs.put(renderDef.getDinosaur(), renderDef);
    }

    public RenderDinosaurDefinition getRenderDef(Dinosaur dino)
    {
        return renderDefs.get(dino);
    }
}
