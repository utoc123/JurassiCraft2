package org.jurassicraft.client.render;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
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
import org.jurassicraft.client.render.entity.HelicopterRenderer;
import org.jurassicraft.client.render.entity.JurassiCraftSignRenderer;
import org.jurassicraft.client.render.entity.PaddockSignRenderer;
import org.jurassicraft.client.render.renderdef.IndominusRenderDef;
import org.jurassicraft.client.render.renderdef.RenderDinosaurDefinition;
import org.jurassicraft.server.block.EncasedFossilBlock;
import org.jurassicraft.server.block.FossilBlock;
import org.jurassicraft.server.block.JCBlockRegistry;
import org.jurassicraft.server.block.tree.JCLeavesBlock;
import org.jurassicraft.server.block.tree.JCPlanksBlock;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.entity.item.BluePrintEntity;
import org.jurassicraft.server.entity.item.CageSmallEntity;
import org.jurassicraft.server.entity.item.JurassiCraftSignEntity;
import org.jurassicraft.server.entity.item.PaddockSignEntity;
import org.jurassicraft.server.item.DinosaurSpawnEggItem;
import org.jurassicraft.server.item.JCItemRegistry;
import org.jurassicraft.server.item.bones.FossilItem;
import org.jurassicraft.server.plant.JCPlantRegistry;
import org.jurassicraft.server.plant.Plant;
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
public class JCRenderingRegistry
{
    private static Map<Dinosaur, RenderDinosaurDefinition> renderDefs = Maps.newHashMap();
    private static final Minecraft mc = Minecraft.getMinecraft();

    public void preInit()
    {
        for (Dinosaur dino : JCEntityRegistry.getDinosaurs())
        {
            String dinoName = dino.getName().toLowerCase().replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : JCItemRegistry.fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    ModelBakery.registerItemVariants(entry.getValue(), new ResourceLocation("jurassicraft:bones/" + dinoName + "_" + entry.getKey()));
                }
            }

            for (Map.Entry<String, FossilItem> entry : JCItemRegistry.fresh_fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    ModelBakery.registerItemVariants(entry.getValue(), new ResourceLocation("jurassicraft:fresh_bones/" + dinoName + "_" + entry.getKey()));
                }
            }

            ModelBakery.registerItemVariants(JCItemRegistry.dna, new ResourceLocation("jurassicraft:dna/dna_" + dinoName));
            ModelBakery.registerItemVariants(JCItemRegistry.egg, new ResourceLocation("jurassicraft:egg/egg_" + dinoName));
            ModelBakery.registerItemVariants(JCItemRegistry.dino_meat, new ResourceLocation("jurassicraft:meat/meat_" + dinoName));
            ModelBakery.registerItemVariants(JCItemRegistry.dino_steak, new ResourceLocation("jurassicraft:meat/steak_" + dinoName));
            ModelBakery.registerItemVariants(JCItemRegistry.soft_tissue, new ResourceLocation("jurassicraft:soft_tissue/soft_tissue_" + dinoName));
            ModelBakery.registerItemVariants(JCItemRegistry.syringe, new ResourceLocation("jurassicraft:syringe/syringe_" + dinoName));
            ModelBakery.registerItemVariants(JCItemRegistry.action_figure, new ResourceLocation("jurassicraft:action_figure/action_figure_" + dinoName));
        }

        for (Plant plant : JCPlantRegistry.getPlants())
        {
            String name = plant.getName().toLowerCase().replaceAll(" ", "_");

            ModelBakery.registerItemVariants(JCItemRegistry.plant_dna, new ResourceLocation("jurassicraft:dna/plants/dna_" + name));
        }

        for (EnumDyeColor color : EnumDyeColor.values())
        {
            ModelBakery.registerItemVariants(Item.getItemFromBlock(JCBlockRegistry.cultivate_bottom), new ResourceLocation("jurassicraft:cultivate/cultivate_bottom_" + color.getName().toLowerCase()));
        }

        ModelBakery.registerItemVariants(JCItemRegistry.amber, new ResourceLocation("jurassicraft:amber_aphid"), new ResourceLocation("jurassicraft:amber_mosquito"));

        ModelBakery.registerItemVariants(JCItemRegistry.cage_small, new ResourceLocation("jurassicraft:cage_small"), new ResourceLocation("jurassicraft:cage_small_marine"));
    }

    public void init()
    {
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.achillobator, new AchillobatorAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.anklyosaurus, new AnkylosaurusAnimator(), 0.85F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.brachiosaurus, new BrachiosaurusAnimator(), 1.5F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.carnotaurus, new CarnotaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.coelacanth, new CoelacanthAnimator(), 0.35F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.compsognathus, new CompsognathusAnimator(), 1.8F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.dilophosaurus, new DilophosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.dunkleosteus, new DunkleosteusAnimator(), 0.35F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.gallimimus, new GallimimusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.giganotosaurus, new GiganotosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.hypsilophodon, new HypsilophodonAnimator(), 0.65F));
        registerRenderDef(new IndominusRenderDef(0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.majungasaurus, new MajungasaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.parasaurolophus, new ParasaurolophusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.pteranodon, new PteranodonAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.rugops, new RugopsAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.segisaurus, new SegisaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.spinosaurus, new SpinosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.stegosaurus, new StegosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.triceratops, new TriceratopsAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.tyrannosaurus, new TyrannosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.velociraptor, new VelociraptorAnimator(), 0.45F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.dodo, new DodoAnimator(), 0.5F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.leptictidium, new LeptictidiumAnimator(), 0.45F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.microceratus, new MicroceratusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.oviraptor, new OviraptorAnimator(), 0.55F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.apatosaurus, new ApatosaurusAnimator(), 1.5F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.othnielia, new OthnieliaAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.dimorphodon, new DimorphodonAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.tylosaurus, new TylosaurusAnimator(), 0.85F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.ludodactylus, new LudodactylusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.protoceratops, new ProtoceratopsAnimator(), 0.55F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.tropeognathus, new TropeognathusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.leaellynasaura, new LeaellynasauraAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.herrerasaurus, new HerrerasaurusAnimator(), 0.75F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.velociraptor_blue, new VelociraptorBlueAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.velociraptor_charlie, new VelociraptorCharlieAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.velociraptor_delta, new VelociraptorDeltaAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.velociraptor_echo, new VelociraptorEchoAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.therizinosaurus, new TherizinosaurusAnimator(), 0.55F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.megapiranha, new MegapiranhaAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.baryonyx, new BaryonyxAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.cearadactylus, new CearadactylusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.mamenchisaurus, new MamenchisaurusAnimator(), 1.5F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.chasmosaurus, new ChasmosaurusAnimator(), 0.85F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.corythosaurus, new CorythosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.edmontosaurus, new EdmontosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.lambeosaurus, new LambeosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.metriacanthosaurus, new MetriacanthosaurusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.moganopterus, new MoganopterusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.ornithomimus, new OrnithomimusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.zhenyuanopterus, new ZhenyuanopterusAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.troodon, new TroodonAnimator(), 0.65F));
        registerRenderDef(new RenderDinosaurDefinition(JCEntityRegistry.pachycephalosaurus, new PachycephalosaurusAnimator(), 0.65F));

        GuiAppRegistry.register();

        // Blocks
        ItemModelMesher modelMesher = mc.getRenderItem().getItemModelMesher();

        int i = 0;

        for (EncasedFossilBlock fossil : JCBlockRegistry.encased_fossils)
        {
            this.registerBlockRenderer(modelMesher, fossil, "encased_fossil_" + i, "inventory");

            i++;
        }

        i = 0;

        for (FossilBlock fossil : JCBlockRegistry.fossils)
        {
            this.registerBlockRenderer(modelMesher, fossil, "fossil_block_" + i, "inventory");

            i++;
        }

        for (TreeType type : TreeType.values())
        {
            String name = type.name().toLowerCase();
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.leaves.get(type), name + "_leaves", "inventory");
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.saplings.get(type), name + "_sapling", "inventory");
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.planks.get(type), name + "_planks", "inventory");
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.logs.get(type), name + "_log", "inventory");
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.stairs.get(type), name + "_stairs", "inventory");
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.slabs.get(type), name + "_slab", "inventory");
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.doubleSlabs.get(type), name + "_double_sab", "inventory");
        }

        for (EnumDyeColor color : EnumDyeColor.values())
        {
            this.registerBlockRenderer(modelMesher, JCBlockRegistry.cultivate_bottom, color.ordinal(), "cultivate/cultivate_bottom_" + color.getName().toLowerCase(), "inventory");
        }

        this.registerBlockRenderer(modelMesher, JCBlockRegistry.scaly_tree_fern, "scaly_tree_fern", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.small_royal_fern, "small_royal_fern", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.small_chain_fern, "small_chain_fern", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.small_cycad, "small_cycad", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.bennettitalean_cycadeoidea, "bennettitalean_cycadeoidea", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.cry_pansy, "cry_pansy", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.cycad_zamites, "cycad_zamites", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.dicksonia, "dicksonia", "inventory");

        this.registerBlockRenderer(modelMesher, JCBlockRegistry.reinforced_stone, "reinforced_stone", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.reinforced_bricks, "reinforced_bricks", "inventory");

        this.registerBlockRenderer(modelMesher, JCBlockRegistry.cultivate_bottom, "cultivate_bottom", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.cultivate_top, "cultivate_top", "inventory");

        this.registerBlockRenderer(modelMesher, JCBlockRegistry.amber_ore, "amber_ore", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.ice_shard, "ice_shard", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.cleaning_station, "cleaning_station", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.fossil_grinder, "fossil_grinder", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.dna_sequencer, "dna_sequencer", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.dna_combinator_hybridizer, "dna_combinator_hybridizer", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.dna_synthesizer, "dna_synthesizer", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.embryonic_machine, "embryonic_machine", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.embryo_calcification_machine, "embryo_calcification_machine", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.incubator, "incubator", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.dna_extractor, "dna_extractor", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.gypsum_stone, "gypsum_stone", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.gypsum_cobblestone, "gypsum_cobblestone", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.gypsum_bricks, "gypsum_bricks", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.action_figure, "action_figure_block", "inventory");

        this.registerBlockRenderer(modelMesher, JCBlockRegistry.moss, "moss", "inventory");
        this.registerBlockRenderer(modelMesher, JCBlockRegistry.clear_glass, "clear_glass", "inventory");

        BlockColors blockColors = mc.getBlockColors();
        blockColors.registerBlockColorHandler(new IBlockColor()
        {
            @Override
            public int colorMultiplier(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex)
            {
                return BiomeColorHelper.getGrassColorAtPos(access, pos);
            }
        }, JCBlockRegistry.moss);

        for (Map.Entry<TreeType, JCLeavesBlock> entry : JCBlockRegistry.leaves.entrySet())
        {
            blockColors.registerBlockColorHandler(new IBlockColor()
            {
                @Override
                public int colorMultiplier(IBlockState state, IBlockAccess access, BlockPos pos, int tintIndex)
                {
                    JCLeavesBlock block = (JCLeavesBlock) (state.getBlock());
                    return block.getTreeType() == TreeType.GINKGO ? 0xFFFFFF : BiomeColorHelper.getFoliageColorAtPos(access, pos);
                }
            }, entry.getValue());
        }

        this.registerBlockRenderer(modelMesher, JCBlockRegistry.ajuginucula_smithii, "ajuginucula_smithii", "inventory");
    }

    public void postInit()
    {
        RenderManager renderManager = mc.getRenderManager();

        for (Dinosaur dino : JCEntityRegistry.getDinosaurs())
        {
            RenderingRegistry.registerEntityRenderingHandler(dino.getDinosaurClass(), renderDefs.get(dino).createRenderFor(renderManager));
        }

        RenderingRegistry.registerEntityRenderingHandler(CageSmallEntity.class, new CageSmallRenderer().createRenderFor(renderManager));
        RenderingRegistry.registerEntityRenderingHandler(BluePrintEntity.class, new BluePrintRenderer().createRenderFor(renderManager));
        RenderingRegistry.registerEntityRenderingHandler(PaddockSignEntity.class, new PaddockSignRenderer().createRenderFor(renderManager));
        RenderingRegistry.registerEntityRenderingHandler(JurassiCraftSignEntity.class, new JurassiCraftSignRenderer().createRenderFor(renderManager));
        RenderingRegistry.registerEntityRenderingHandler(HelicopterBaseEntity.class, new HelicopterRenderer().createRenderFor(renderManager));

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
        this.registerItemRenderer(modelMesher, JCItemRegistry.tracker, "tracker", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.plant_cells_petri_dish, "plant_cells_petri_dish", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.plant_cells, "plant_cells", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.plant_callus, "plant_callus", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.needle, "needle", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.growth_serum, "growth_serum", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.iron_rod, "iron_rod", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.iron_blades, "iron_blades", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.cage_small, 0, "cage_small", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.cage_small, 1, "cage_small_marine", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.petri_dish, "petri_dish", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.amber, "amber", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.plaster_and_bandage, "plaster_and_bandage", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.spawn_egg, "dino_spawn_egg", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.paleo_pad, "paleo_pad", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.blue_print, "blue_print", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.paddock_sign, "paddock_sign", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.jc_sign, "jurassicraft_sign", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.empty_test_tube, "empty_test_tube", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.empty_syringe, "empty_syringe", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.storage_disc, "storage_disc", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.disc_reader, "disc_reader", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.laser, "laser", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.dna_base, "dna_base_material", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.sea_lamprey, "sea_lamprey", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.amber, 0, "amber_mosquito", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.amber, 1, "amber_aphid", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.helicopter_spawner, "helicopter_spawner", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.disc_jurassicraft_theme, "disc_jurassicraft_theme", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.disc_dont_move_a_muscle, "disc_dont_move_a_muscle", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.disc_troodons_and_raptors, "disc_troodons_and_raptors", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.amber_keychain, "amber_keychain", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.amber_cane, "amber_cane", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.mr_dna_keychain, "mr_dna_keychain", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.dino_scanner, "dino_scanner", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.basic_circuit, "basic_circuit", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.advanced_circuit, "advanced_circuit", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.iron_nugget, "iron_nugget", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.gypsum_powder, "gypsum_powder", "inventory");

        this.registerItemRenderer(modelMesher, JCItemRegistry.ajuginucula_smithii_seeds, "ajuginucula_smithii_seeds", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.ajuginucula_smithii_leaves, "ajuginucula_smithii_leaves", "inventory");
        this.registerItemRenderer(modelMesher, JCItemRegistry.ajuginucula_smithii_oil, "ajuginucula_smithii_oil", "inventory");

        int meta = 0;

        for (Dinosaur dino : JCEntityRegistry.getDinosaurs())
        {
            String dinoName = dino.getName().toLowerCase().replaceAll(" ", "_");

            for (Map.Entry<String, FossilItem> entry : JCItemRegistry.fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    this.registerItemRenderer(modelMesher, entry.getValue(), meta, "bones/" + dinoName + "_" + entry.getKey(), "inventory");
                }
            }

            for (Map.Entry<String, FossilItem> entry : JCItemRegistry.fresh_fossils.entrySet())
            {
                List<Dinosaur> dinosaursForType = FossilItem.fossilDinosaurs.get(entry.getKey());

                if (dinosaursForType.contains(dino))
                {
                    this.registerItemRenderer(modelMesher, entry.getValue(), meta, "fresh_bones/" + dinoName + "_" + entry.getKey(), "inventory");
                }
            }

            this.registerItemRenderer(modelMesher, JCItemRegistry.dna, meta, "dna/dna_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, JCItemRegistry.egg, meta, "egg/egg_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, JCItemRegistry.dino_meat, meta, "meat/meat_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, JCItemRegistry.dino_steak, meta, "meat/steak_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, JCItemRegistry.soft_tissue, meta, "soft_tissue/soft_tissue_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, JCItemRegistry.syringe, meta, "syringe/syringe_" + dinoName, "inventory");
            this.registerItemRenderer(modelMesher, JCItemRegistry.action_figure, meta, "action_figure/action_figure_" + dinoName, "inventory");

            meta++;
        }

        meta = 0;

        for (Plant plant : JCPlantRegistry.getPlants())
        {
            String name = plant.getName().toLowerCase().replaceAll(" ", "_");

            this.registerItemRenderer(modelMesher, JCItemRegistry.plant_dna, meta, "dna/plants/dna_" + name, "inventory");

            meta++;
        }

        ItemColors itemColors = mc.getItemColors();
        itemColors.registerItemColorHandler(new IItemColor()
        {
            @Override
            public int getColorFromItemstack(ItemStack stack, int tintIndex)
            {
                DinosaurSpawnEggItem item = (DinosaurSpawnEggItem) stack.getItem();
                Dinosaur dino = item.getDinosaur(stack);

                if (dino != null)
                {
                    int mode = item.getMode(stack);

                    if (mode == 0)
                    {
                        mode = JurassiCraft.timerTicks % 64 > 32 ? 1 : 2;
                    }

                    if (mode == 1)
                    {
                        return tintIndex == 0 ? dino.getEggPrimaryColorMale() : dino.getEggSecondaryColorMale();
                    }
                    else
                    {
                        return tintIndex == 0 ? dino.getEggPrimaryColorFemale() : dino.getEggSecondaryColorFemale();
                    }
                }

                return 0xFFFFFF;
            }
        }, JCItemRegistry.spawn_egg);
    }

    /**
     * Registers an item renderer
     */
    public void registerItemRenderer(ItemModelMesher itemModelMesher, Item item, final String path, final String type)
    {
        itemModelMesher.register(item, new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type);
            }
        });
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
        itemModelMesher.register(Item.getItemFromBlock(block), new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation(JurassiCraft.MODID + ":" + path, type);
            }
        });
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
