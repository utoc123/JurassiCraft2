package org.jurassicraft.server.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.SubBlocksBlock;
import org.jurassicraft.server.block.machine.CleaningStationBlock;
import org.jurassicraft.server.block.machine.CultivatorBottomBlock;
import org.jurassicraft.server.block.machine.CultivatorTopBlock;
import org.jurassicraft.server.block.machine.DNACombinatorHybridizerBlock;
import org.jurassicraft.server.block.machine.DNAExtractorBlock;
import org.jurassicraft.server.block.machine.DNASequencerBlock;
import org.jurassicraft.server.block.machine.DNASynthesizerBlock;
import org.jurassicraft.server.block.machine.EmbryoCalcificationMachineBlock;
import org.jurassicraft.server.block.machine.EmbryonicMachineBlock;
import org.jurassicraft.server.block.machine.FossilGrinderBlock;
import org.jurassicraft.server.block.machine.IncubatorBlock;
import org.jurassicraft.server.block.plant.AjuginuculaSmithiiBlock;
import org.jurassicraft.server.block.plant.AncientPlantBlock;
import org.jurassicraft.server.block.plant.BennettitaleanCycadeoideaBlock;
import org.jurassicraft.server.block.plant.CycadZamitesBlock;
import org.jurassicraft.server.block.plant.DicksoniaBlock;
import org.jurassicraft.server.block.plant.DicroidiumZuberiBlock;
import org.jurassicraft.server.block.plant.GracilariaBlock;
import org.jurassicraft.server.block.plant.MossBlock;
import org.jurassicraft.server.block.plant.ScalyTreeFernBlock;
import org.jurassicraft.server.block.plant.SmallChainFernBlock;
import org.jurassicraft.server.block.plant.SmallCycadBlock;
import org.jurassicraft.server.block.plant.SmallRoyalFernBlock;
import org.jurassicraft.server.block.plant.WildOnionBlock;
import org.jurassicraft.server.block.tree.AncientDoubleSlabBlock;
import org.jurassicraft.server.block.tree.AncientLeavesBlock;
import org.jurassicraft.server.block.tree.AncientLogBlock;
import org.jurassicraft.server.block.tree.AncientPlanksBlock;
import org.jurassicraft.server.block.tree.AncientSaplingBlock;
import org.jurassicraft.server.block.tree.AncientSlabHalfBlock;
import org.jurassicraft.server.block.tree.AncientStairsBlock;
import org.jurassicraft.server.block.tree.TreeType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.tile.ActionFigureTile;
import org.jurassicraft.server.tile.CleaningStationTile;
import org.jurassicraft.server.tile.CultivatorTile;
import org.jurassicraft.server.tile.DNACombinatorHybridizerTile;
import org.jurassicraft.server.tile.DNAExtractorTile;
import org.jurassicraft.server.tile.DNASequencerTile;
import org.jurassicraft.server.tile.DNASynthesizerTile;
import org.jurassicraft.server.tile.EmbryoCalcificationMachineTile;
import org.jurassicraft.server.tile.EmbryonicMachineTile;
import org.jurassicraft.server.tile.FossilGrinderTile;
import org.jurassicraft.server.tile.IncubatorTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BlockHandler
{
    INSTANCE;

    public Map<TreeType, AncientPlanksBlock> ANCIENT_PLANKS = new HashMap<>();
    public Map<TreeType, AncientLogBlock> ANCIENT_LOGS = new HashMap<>();
    public Map<TreeType, AncientLeavesBlock> ANCIENT_LEAVES = new HashMap<>();
    public Map<TreeType, AncientSaplingBlock> ANCIENT_SAPLINGS = new HashMap<>();

    public Map<TreeType, AncientSlabHalfBlock> ANCIENT_SLABS = new HashMap<>();
    public Map<TreeType, AncientDoubleSlabBlock> ANCIENT_DOUBLE_SLABS = new HashMap<>();
    public Map<TreeType, AncientStairsBlock> ANCIENT_STAIRS = new HashMap<>();

    public Map<TreeType, AncientLogBlock> PETRIFIED_LOGS = new HashMap<>();

    public List<FossilBlock> FOSSILS;
    public List<EncasedFossilBlock> ENCASED_FOSSILS;

    public PlantFossilBlock PLANT_FOSSIL;

    public CleaningStationBlock CLEANING_STATION;
    public FossilGrinderBlock FOSSIL_GRINDER;
    public DNASequencerBlock DNA_SEQUENCER;
    public DNASynthesizerBlock DNA_SYNTHESIZER;
    public EmbryonicMachineBlock EMBRYONIC_MACHINE;
    public EmbryoCalcificationMachineBlock EMBRYO_CALCIFICATION_MACHINE;
    public IncubatorBlock INCUBATOR;
    public DNAExtractorBlock DNA_EXTRACTOR;
    public DNACombinatorHybridizerBlock DNA_COMBINATOR_HYBRIDIZER;

    public AmberBlock AMBER_ORE;
    public IceShardBlock ICE_SHARD;

    public Block GYPSUM_COBBLESTONE;
    public Block GYPSUM_STONE;
    public Block GYPSUM_BRICKS;

    public Block REINFORCED_STONE;
    public Block REINFORCED_BRICKS;

    public SmallRoyalFernBlock SMALL_ROYAL_FERN;
    public SmallChainFernBlock SMALL_CHAIN_FERN;
    public SmallCycadBlock SMALL_CYCAD;

    public CultivatorTopBlock CULTIVATOR_TOP;
    public CultivatorBottomBlock CULTIVATOR_BOTTOM;

    public BennettitaleanCycadeoideaBlock CYCADEOIDEA;
    public AncientPlantBlock CRY_PANSY;
    public ScalyTreeFernBlock SCALY_TREE_FERN;
    public CycadZamitesBlock ZAMITES;
    public DicksoniaBlock DICKSONIA;
    public DicroidiumZuberiBlock DICROIDIUM_ZUBERI;

    public ActionFigureBlock ACTION_FIGURE;

    public MossBlock MOSS;

    public ClearGlassBlock CLEAR_GLASS;

    public AjuginuculaSmithiiBlock AJUGINUCULA_SMITHII;
    public WildOnionBlock WILD_ONION;
    public GracilariaBlock GRACILARIA;

    public PeatBlock PEAT;
    public Block PEAT_MOSS;

    public void init()
    {
        FOSSILS = new ArrayList<>();
        ENCASED_FOSSILS = new ArrayList<>();
        PLANT_FOSSIL = new PlantFossilBlock();

        CLEANING_STATION = new CleaningStationBlock();
        FOSSIL_GRINDER = new FossilGrinderBlock();
        DNA_SEQUENCER = new DNASequencerBlock();
        DNA_SYNTHESIZER = new DNASynthesizerBlock();
        EMBRYONIC_MACHINE = new EmbryonicMachineBlock();
        EMBRYO_CALCIFICATION_MACHINE = new EmbryoCalcificationMachineBlock();
        INCUBATOR = new IncubatorBlock();
        DNA_EXTRACTOR = new DNAExtractorBlock();
        DNA_COMBINATOR_HYBRIDIZER = new DNACombinatorHybridizerBlock();
        CULTIVATOR_BOTTOM = new CultivatorBottomBlock();
        CULTIVATOR_TOP = new CultivatorTopBlock();

        AMBER_ORE = new AmberBlock();
        ICE_SHARD = new IceShardBlock();

        GYPSUM_STONE = new GypsumStoneBlock();
        GYPSUM_COBBLESTONE = new BasicBlock(Material.ROCK).setHardness(1.5F);
        GYPSUM_BRICKS = new BasicBlock(Material.ROCK).setHardness(1.5F);
        REINFORCED_STONE = new BasicBlock(Material.ROCK).setHardness(2.0F);
        REINFORCED_BRICKS = new BasicBlock(Material.ROCK).setHardness(2.0F);

        AJUGINUCULA_SMITHII = new AjuginuculaSmithiiBlock();
        SMALL_ROYAL_FERN = new SmallRoyalFernBlock();
        SMALL_CHAIN_FERN = new SmallChainFernBlock();
        SMALL_CYCAD = new SmallCycadBlock();
        CYCADEOIDEA = new BennettitaleanCycadeoideaBlock();
        CRY_PANSY = new AncientPlantBlock();
        SCALY_TREE_FERN = new ScalyTreeFernBlock();
        ZAMITES = new CycadZamitesBlock();
        DICKSONIA = new DicksoniaBlock();
        WILD_ONION = new WildOnionBlock();
        GRACILARIA = new GracilariaBlock();
        DICROIDIUM_ZUBERI = new DicroidiumZuberiBlock();

        ACTION_FIGURE = new ActionFigureBlock();

        MOSS = new MossBlock();

        CLEAR_GLASS = new ClearGlassBlock();

        PEAT = new PeatBlock();
        PEAT_MOSS = new BasicBlock(Material.GROUND, SoundType.GROUND).setHardness(0.5F).setCreativeTab(TabHandler.INSTANCE.PLANTS);

        for (int i = 0; i < (int) Math.ceil(EntityHandler.INSTANCE.getDinosaurs().size() / 16.0F); i++)
        {
            FossilBlock fossil = new FossilBlock(i * 16);
            EncasedFossilBlock encasedFossil = new EncasedFossilBlock(i * 16);

            FOSSILS.add(fossil);
            ENCASED_FOSSILS.add(encasedFossil);

            registerBlock(fossil, "Fossil Block " + i);
            registerBlock(encasedFossil, "Encased Fossil " + i);

            OreDictionary.registerOre("fossil", fossil);
        }

        registerBlock(PLANT_FOSSIL, "Plant Fossil Block");

        for (TreeType type : TreeType.values())
        {
            registerTreeType(type);
        }

        registerBlock(AMBER_ORE, "Amber Ore");
        registerBlock(ICE_SHARD, "Ice Shard");
        registerBlock(GYPSUM_STONE, "Gypsum Stone");
        registerBlock(GYPSUM_COBBLESTONE, "Gypsum Cobblestone");
        registerBlock(GYPSUM_BRICKS, "Gypsum Bricks");
        registerBlock(REINFORCED_STONE, "Reinforced Stone");
        registerBlock(REINFORCED_BRICKS, "Reinforced Bricks");

        registerBlock(AJUGINUCULA_SMITHII, "Ajuginucula Smithii");
        registerBlock(SMALL_ROYAL_FERN, "Small Royal Fern");
        registerBlock(SMALL_CHAIN_FERN, "Small Chain Fern");
        registerBlock(SMALL_CYCAD, "Small Cycad");
        registerBlock(CYCADEOIDEA, "Bennettitalean Cycadeoidea");
        registerBlock(CRY_PANSY, "Cry Pansy");
        registerBlock(SCALY_TREE_FERN, "Scaly Tree Fern");
        registerBlock(ZAMITES, "Cycad Zamites");
        registerBlock(DICKSONIA, "Dicksonia");
        registerBlock(WILD_ONION, "Wild Onion Plant");
        registerBlock(GRACILARIA, "Gracilaria Seaweed");
        registerBlock(DICROIDIUM_ZUBERI, "Dicroidium Zuberi");

        registerBlock(MOSS, "Moss");
        registerBlock(PEAT, "Peat");
        registerBlock(PEAT_MOSS, "Peat Moss");

        registerBlock(CLEAR_GLASS, "Clear Glass");

        registerBlockTileEntity(CultivatorTile.class, CULTIVATOR_BOTTOM, "Cultivate Bottom");
        registerBlock(CULTIVATOR_TOP, "Cultivate Top");
        registerBlockTileEntity(CleaningStationTile.class, CLEANING_STATION, "Cleaning Station");
        registerBlockTileEntity(FossilGrinderTile.class, FOSSIL_GRINDER, "Fossil Grinder");
        registerBlockTileEntity(DNASequencerTile.class, DNA_SEQUENCER, "DNA Sequencer");
        registerBlockTileEntity(DNASynthesizerTile.class, DNA_SYNTHESIZER, "DNA Synthesizer");
        registerBlockTileEntity(EmbryonicMachineTile.class, EMBRYONIC_MACHINE, "Embryonic Machine");
        registerBlockTileEntity(EmbryoCalcificationMachineTile.class, EMBRYO_CALCIFICATION_MACHINE, "Embryo Calcification Machine");
        registerBlockTileEntity(DNAExtractorTile.class, DNA_EXTRACTOR, "DNA Extractor");
        registerBlockTileEntity(DNACombinatorHybridizerTile.class, DNA_COMBINATOR_HYBRIDIZER, "DNA Combinator Hybridizer");
        registerBlockTileEntity(IncubatorTile.class, INCUBATOR, "Incubator");
        registerBlockTileEntity(ActionFigureTile.class, ACTION_FIGURE, "Action Figure Block");
    }

    public void registerTreeType(TreeType type)
    {
        AncientPlanksBlock planks = new AncientPlanksBlock(type);
        AncientLogBlock log = new AncientLogBlock(type, false);
        AncientLogBlock petrified_log = new AncientLogBlock(type, true);
        AncientLeavesBlock leaves = new AncientLeavesBlock(type);
        AncientSaplingBlock sapling = new AncientSaplingBlock(type);
        AncientStairsBlock stair = new AncientStairsBlock(type, planks.getDefaultState());
        AncientSlabHalfBlock slab = new AncientSlabHalfBlock(type, planks.getDefaultState());
        AncientDoubleSlabBlock double_slab = new AncientDoubleSlabBlock(type, slab, planks.getDefaultState());

        this.ANCIENT_PLANKS.put(type, planks);
        this.ANCIENT_LOGS.put(type, log);
        this.ANCIENT_LEAVES.put(type, leaves);
        this.ANCIENT_SAPLINGS.put(type, sapling);
        this.ANCIENT_STAIRS.put(type, stair);
        this.ANCIENT_SLABS.put(type, slab);
        this.ANCIENT_DOUBLE_SLABS.put(type, double_slab);
        this.PETRIFIED_LOGS.put(type, petrified_log);

        String typeName = type.name();

        registerBlock(planks, typeName + " Planks");
        registerBlock(log, typeName + " Log");
        registerBlock(petrified_log, typeName + " Log Petrified");
        registerBlock(leaves, typeName + " Leaves");
        registerBlock(sapling, typeName + " Sapling");
        registerBlock(stair, typeName + " Stairs");
        registerBlock(slab, typeName + " Slab");
        registerBlock(double_slab, typeName + " Double Slab");

        OreDictionary.registerOre("logWood", log);
        OreDictionary.registerOre("logWood", petrified_log);
        OreDictionary.registerOre("plankWood", planks);
        OreDictionary.registerOre("treeLeaves", leaves);
        OreDictionary.registerOre("treeSapling", sapling);
        OreDictionary.registerOre("slabWood", slab);
        OreDictionary.registerOre("stairWood", stair);

        Blocks.FIRE.setFireInfo(leaves, 30, 60);
        Blocks.FIRE.setFireInfo(planks, 5, 20);
        Blocks.FIRE.setFireInfo(log, 5, 5);
        Blocks.FIRE.setFireInfo(petrified_log, 5, 5);
        Blocks.FIRE.setFireInfo(double_slab, 5, 20);
        Blocks.FIRE.setFireInfo(slab, 5, 20);
        Blocks.FIRE.setFireInfo(stair, 5, 20);
    }

    public FossilBlock getFossilBlock(Dinosaur dinosaur)
    {
        return getFossilBlock(EntityHandler.INSTANCE.getDinosaurId(dinosaur));
    }

    private int getBlockId(int id)
    {
        return (int) (Math.floor((((float) id + 1.0F) / 16.0F) - 0.0625F));
    }

    public EncasedFossilBlock getEncasedFossil(Dinosaur dinosaur)
    {
        return getEncasedFossil(EntityHandler.INSTANCE.getDinosaurId(dinosaur));
    }

    public EncasedFossilBlock getEncasedFossil(int id)
    {
        return ENCASED_FOSSILS.get(getBlockId(id));
    }

    public FossilBlock getFossilBlock(int id)
    {
        return FOSSILS.get(getBlockId(id));
    }

    public int getDinosaurId(FossilBlock fossil, int metadata)
    {
        return (FOSSILS.indexOf(fossil) * 16) + metadata;
    }

    public int getDinosaurId(EncasedFossilBlock fossil, int metadata)
    {
        return (ENCASED_FOSSILS.indexOf(fossil) * 16) + metadata;
    }

    public int getMetadata(int id)
    {
        return id % 16;
    }

    public int getMetadata(Dinosaur dinosaur)
    {
        return getMetadata(EntityHandler.INSTANCE.getDinosaurId(dinosaur));
    }

    public void registerBlockTileEntity(Class<? extends TileEntity> tileEntity, Block block, String name)
    {
        registerBlock(block, name);

        GameRegistry.registerTileEntity(tileEntity, "jurassicraft:" + name.toLowerCase().replaceAll(" ", "_"));
    }

    public void registerBlock(Block block, String name)
    {
        name = name.toLowerCase().replaceAll(" ", "_");

        block.setUnlocalizedName(name);

        ResourceLocation resource = new ResourceLocation(JurassiCraft.MODID, name);

        if (block instanceof SubBlocksBlock)
        {
            GameRegistry.register(block, resource);
            GameRegistry.register(((SubBlocksBlock) block).getItemBlock(), resource);
        }
        else
        {
            GameRegistry.register(block, resource);
            GameRegistry.register(new ItemBlock(block), resource);
        }
    }
}
