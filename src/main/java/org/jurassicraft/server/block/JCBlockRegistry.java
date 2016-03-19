package org.jurassicraft.server.block;

import net.ilexiconn.llibrary.common.content.IContentHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.jurassicraft.server.api.ISubBlocksBlock;
import org.jurassicraft.server.block.machine.CleaningStationBlock;
import org.jurassicraft.server.block.machine.CultivatorBottomBlock;
import org.jurassicraft.server.block.machine.CultivatorTopBlock;
import org.jurassicraft.server.block.machine.DNAExtractorBlock;
import org.jurassicraft.server.block.machine.DNACombinatorHybridizerBlock;
import org.jurassicraft.server.block.machine.DNASequencerBlock;
import org.jurassicraft.server.block.machine.DNASynthesizerBlock;
import org.jurassicraft.server.block.machine.EmbryoCalcificationMachineBlock;
import org.jurassicraft.server.block.machine.EmbryonicMachineBlock;
import org.jurassicraft.server.block.machine.FossilGrinderBlock;
import org.jurassicraft.server.block.machine.IncubatorBlock;
import org.jurassicraft.server.block.plant.BennettitaleanCycadeoideaBlock;
import org.jurassicraft.server.block.plant.CryPansyBlock;
import org.jurassicraft.server.block.plant.CycadZamitesBlock;
import org.jurassicraft.server.block.plant.DicksoniaBlock;
import org.jurassicraft.server.block.plant.MossBlock;
import org.jurassicraft.server.block.plant.ScalyTreeFernBlock;
import org.jurassicraft.server.block.plant.SmallChainFernBlock;
import org.jurassicraft.server.block.plant.SmallCycadBlock;
import org.jurassicraft.server.block.plant.SmallRoyalFernBlock;
import org.jurassicraft.server.block.tree.JCDoubleSlabBlock;
import org.jurassicraft.server.block.tree.JCLeavesBlock;
import org.jurassicraft.server.block.tree.JCLogBlock;
import org.jurassicraft.server.block.tree.JCPlanksBlock;
import org.jurassicraft.server.block.tree.JCSaplingBlock;
import org.jurassicraft.server.block.tree.JCSlabHalfBlock;
import org.jurassicraft.server.block.tree.JCStairsBlock;
import org.jurassicraft.server.block.tree.WoodType;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.JCEntityRegistry;
import org.jurassicraft.server.item.itemblock.JCSlabItemBlock;
import org.jurassicraft.server.tileentity.ActionFigureTile;
import org.jurassicraft.server.tileentity.CleaningStationTile;
import org.jurassicraft.server.tileentity.CultivatorTile;
import org.jurassicraft.server.tileentity.DNAExtractorTile;
import org.jurassicraft.server.tileentity.DNACombinatorHybridizerTile;
import org.jurassicraft.server.tileentity.DNASequencerTile;
import org.jurassicraft.server.tileentity.DNASynthesizerTile;
import org.jurassicraft.server.tileentity.EmbryoCalcificationMachineTile;
import org.jurassicraft.server.tileentity.EmbryonicMachineTile;
import org.jurassicraft.server.tileentity.FossilGrinderTile;
import org.jurassicraft.server.tileentity.IncubatorTile;
import org.jurassicraft.server.world.jurdstrees.algorythms.TreeCompendium;

import java.util.ArrayList;
import java.util.List;

public class JCBlockRegistry implements IContentHandler
{
    public static final int numOfTrees = 2;

    // tree blocks
    public static Block[] planks;
    public static Block[] woods;
    public static Block[] leaves;
    public static Block[] saplings;

    // wood products
    public static Block[] doors;
    public static Block[] fences;
    public static Block[] slabs;
    public static Block[] doubleSlabs;
    public static Block[] stairs;

    public static List<FossilBlock> fossils;
    public static List<EncasedFossilBlock> encased_fossils;

    public static Block cleaning_station;
    public static Block fossil_grinder;
    public static Block dna_sequencer;
    public static Block dna_synthesizer;
    public static Block embryonic_machine;
    public static Block embryo_calcification_machine;
    public static Block incubator;
    public static Block dna_extractor;

    public static Block amber_ore;
    public static Block ice_shard;

    public static Block gypsum_cobblestone;
    public static Block gypsum_stone;
    public static Block gypsum_bricks;
    public static Block dna_combinator_hybridizer;

    public static Block reinforced_stone;
    public static Block reinforced_bricks;

    public static Block small_royal_fern;
    public static Block small_chain_fern;
    public static Block small_cycad;

    public static Block cultivate_top;
    public static Block cultivate_bottom;

    public static Block bennettitalean_cycadeoidea;
    public static Block cry_pansy;
    public static Block scaly_tree_fern;
    public static Block cycad_zamites;
    public static Block dicksonia;

    public static Block action_figure;

    public static Block moss;

    public static Block clear_glass;

    @Override
    public void init()
    {
        fossils = new ArrayList<FossilBlock>();
        encased_fossils = new ArrayList<EncasedFossilBlock>();

        cleaning_station = new CleaningStationBlock();
        fossil_grinder = new FossilGrinderBlock();
        dna_sequencer = new DNASequencerBlock();
        dna_synthesizer = new DNASynthesizerBlock();
        embryonic_machine = new EmbryonicMachineBlock();
        embryo_calcification_machine = new EmbryoCalcificationMachineBlock();
        incubator = new IncubatorBlock();
        dna_extractor = new DNAExtractorBlock();
        dna_combinator_hybridizer = new DNACombinatorHybridizerBlock();
        cultivate_bottom = new CultivatorBottomBlock();
        cultivate_top = new CultivatorTopBlock();

        amber_ore = new AmberBlock();
        ice_shard = new IceShardBlock();

        gypsum_stone = new GypsumStoneBlock();
        gypsum_cobblestone = new BasicBlock(Material.rock).setHardness(1.5F).setResistance(1.5F);
        gypsum_bricks = new BasicBlock(Material.rock).setHardness(1.5F).setResistance(1.5F);
        reinforced_stone = new BasicBlock(Material.rock).setHardness(2.0F).setResistance(15.0F);
        reinforced_bricks = new BasicBlock(Material.rock).setHardness(2.0F).setResistance(15.0F);

        small_royal_fern = new SmallRoyalFernBlock();
        small_chain_fern = new SmallChainFernBlock();
        small_cycad = new SmallCycadBlock();
        bennettitalean_cycadeoidea = new BennettitaleanCycadeoideaBlock();
        cry_pansy = new CryPansyBlock();
        scaly_tree_fern = new ScalyTreeFernBlock();
        cycad_zamites = new CycadZamitesBlock();
        dicksonia = new DicksoniaBlock();

        action_figure = new ActionFigureBlock();

        moss = new MossBlock();

        clear_glass = new ClearGlassBlock();

        List<Dinosaur> dinosaurs = JCEntityRegistry.getDinosaurs();

        int blocksToCreate = (int) (Math.ceil(((float) dinosaurs.size()) / 16.0F));

        for (int i = 0; i < blocksToCreate; i++)
        {
            FossilBlock fossil = new FossilBlock(i * 16);
            EncasedFossilBlock encasedFossil = new EncasedFossilBlock(i * 16);

            fossils.add(fossil);
            encased_fossils.add(encasedFossil);

            registerBlock(fossil, "Fossil Block " + i);
            registerBlock(encasedFossil, "Encased Fossil " + i);

            OreDictionary.registerOre("fossil", fossil);
        }

        // initialize WoodType meta lookup
        WoodType.GINKGO.setMetaLookup();
        WoodType.CALAMITES.setMetaLookup();

        // register Tree Variables
        TreeCompendium.addShapesToCompendium();
        TreeCompendium.registerTrees();

        // initialize arrays
        planks = new Block[numOfTrees];
        woods = new Block[numOfTrees];
        leaves = new Block[numOfTrees];
        saplings = new Block[numOfTrees];
        doors = new Block[numOfTrees];
        fences = new Block[numOfTrees];
        slabs = new Block[numOfTrees];
        doubleSlabs = new Block[numOfTrees];
        stairs = new Block[numOfTrees];

        // initialize blocks within arrays
        for (int i = 0; i < numOfTrees; i++)
        {
            WoodType type = WoodType.getMetaLookup()[i];
            String typeName = type.getName();

            planks[i] = new JCPlanksBlock(typeName);
            woods[i] = new JCLogBlock(typeName);
            leaves[i] = new JCLeavesBlock(type, typeName);
            saplings[i] = new JCSaplingBlock(type, typeName);
            stairs[i] = new JCStairsBlock(typeName, planks[i].getDefaultState());
            slabs[i] = new JCSlabHalfBlock(typeName, planks[i].getDefaultState());
            doubleSlabs[i] = new JCDoubleSlabBlock(typeName, slabs[i], planks[i].getDefaultState());

            GameRegistry.registerBlock(planks[i], typeName + "_planks");
            GameRegistry.registerBlock(woods[i], typeName + "_log");
            GameRegistry.registerBlock(leaves[i], typeName + "_leaves");
            GameRegistry.registerBlock(saplings[i], typeName + "_sapling");
            GameRegistry.registerBlock(stairs[i], typeName + "_stairs");
            GameRegistry.registerBlock(slabs[i], JCSlabItemBlock.class, typeName + "_slab", slabs[i], doubleSlabs[i]);
            GameRegistry.registerBlock(doubleSlabs[i], JCSlabItemBlock.class, typeName + "_double_slab", slabs[i], doubleSlabs[i]);

            OreDictionary.registerOre("logWood", woods[i]);
            OreDictionary.registerOre("plankWood", planks[i]);
            OreDictionary.registerOre("treeLeaves", leaves[i]);
            OreDictionary.registerOre("treeSapling", saplings[i]);
            OreDictionary.registerOre("slabWood", slabs[i]);
            OreDictionary.registerOre("stairkWood", stairs[i]);

            Blocks.fire.setFireInfo(leaves[i], 30, 60);
            Blocks.fire.setFireInfo(planks[i], 5, 20);
            Blocks.fire.setFireInfo(woods[i], 5, 5);
            Blocks.fire.setFireInfo(doubleSlabs[i], 5, 20);
            Blocks.fire.setFireInfo(slabs[i], 5, 20);
            Blocks.fire.setFireInfo(stairs[i], 5, 20);
        }
    }

    @Override
    public void gameRegistry() throws Exception
    {
        registerBlock(amber_ore, "Amber Ore");
        registerBlock(ice_shard, "Ice Shard");
        registerBlock(gypsum_stone, "Gypsum Stone");
        registerBlock(gypsum_cobblestone, "Gypsum Cobblestone");
        registerBlock(gypsum_bricks, "Gypsum Bricks");
        registerBlock(reinforced_stone, "Reinforced Stone");
        registerBlock(reinforced_bricks, "Reinforced Bricks");
        registerBlock(small_royal_fern, "Small Royal Fern");
        registerBlock(small_chain_fern, "Small Chain Fern");
        registerBlock(small_cycad, "Small Cycad");
        registerBlock(bennettitalean_cycadeoidea, "Bennettitalean Cycadeoidea");
        registerBlock(cry_pansy, "Cry Pansy");
        registerBlock(scaly_tree_fern, "Scaly Tree Fern");
        registerBlock(cycad_zamites, "Cycad Zamites");
        registerBlock(dicksonia, "Dicksonia");
        registerBlock(moss, "Moss");
        registerBlock(clear_glass, "Clear Glass");

        registerBlockTileEntity(CultivatorTile.class, cultivate_bottom, "Cultivate Bottom");
        registerBlock(cultivate_top, "Cultivate Top");
        registerBlockTileEntity(CleaningStationTile.class, cleaning_station, "Cleaning Station");
        registerBlockTileEntity(FossilGrinderTile.class, fossil_grinder, "Fossil Grinder");
        registerBlockTileEntity(DNASequencerTile.class, dna_sequencer, "DNA Sequencer");
        registerBlockTileEntity(DNASynthesizerTile.class, dna_synthesizer, "DNA Synthesizer");
        registerBlockTileEntity(EmbryonicMachineTile.class, embryonic_machine, "Embryonic Machine");
        registerBlockTileEntity(EmbryoCalcificationMachineTile.class, embryo_calcification_machine, "Embryo Calcification Machine");
        registerBlockTileEntity(DNAExtractorTile.class, dna_extractor, "DNA Extractor");
        registerBlockTileEntity(DNACombinatorHybridizerTile.class, dna_combinator_hybridizer, "DNA Combinator Hybridizer");
        registerBlockTileEntity(IncubatorTile.class, incubator, "Incubator");
        registerBlockTileEntity(ActionFigureTile.class, action_figure, "Action Figure Block");
    }

    public FossilBlock getFossilBlock(Dinosaur dinosaur)
    {
        return getFossilBlock(JCEntityRegistry.getDinosaurId(dinosaur));
    }

    private int getBlockId(int dinosaurId)
    {
        return (int) (Math.floor((((float) dinosaurId + 1.0F) / 16.0F) - 0.0625F));
    }

    public EncasedFossilBlock getEncasedFossil(Dinosaur dinosaur)
    {
        return getEncasedFossil(JCEntityRegistry.getDinosaurId(dinosaur));
    }

    public EncasedFossilBlock getEncasedFossil(int id)
    {
        return encased_fossils.get(getBlockId(id));
    }

    public FossilBlock getFossilBlock(int id)
    {
        return fossils.get(getBlockId(id));
    }

    public int getDinosaurId(FossilBlock fossil, int metadata)
    {
        return (fossils.indexOf(fossil) * 16) + metadata;
    }

    public int getDinosaurId(EncasedFossilBlock fossil, int metadata)
    {
        return (encased_fossils.indexOf(fossil) * 16) + metadata;
    }

    public int getMetadata(int id)
    {
        return id % 16;
    }

    public int getMetadata(Dinosaur dinosaur)
    {
        return getMetadata(JCEntityRegistry.getDinosaurId(dinosaur));
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

        if (block instanceof ISubBlocksBlock)
        {
            GameRegistry.registerBlock(block, ((ISubBlocksBlock) block).getItemBlockClass(), name);
        }
        else
        {
            GameRegistry.registerBlock(block, name);
        }
    }
}
