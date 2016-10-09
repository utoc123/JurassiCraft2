package org.jurassicraft.server.world.structure;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import net.minecraft.world.storage.loot.LootTableList;
import org.jurassicraft.JurassiCraft;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RaptorPaddockGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(JurassiCraft.MODID, "raptor_paddock");

    public RaptorPaddockGenerator(Random rand) {
        super(rand, 32, 16, 23);
    }

    @Override
    protected void generateStructure(World world, Random random, BlockPos position) {
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(this.rotation).setMirror(this.mirror);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(position, settings);
        Map<BlockPos, Integer> heights = new HashMap<>();
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String type = entry.getValue();
            BlockPos pos = entry.getKey();
            if (type.equals("Ground")) {
                heights.put(pos, this.getGround(world, pos).getY());
            }
        }
        template.addBlocksToWorldChunk(world, position, settings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String type = entry.getValue();
            BlockPos dataPos = entry.getKey();
            if (type.equals("Chest")) {
                world.setBlockState(dataPos, Blocks.AIR.getDefaultState(), 3);
                TileEntity tile = world.getTileEntity(dataPos.down());
                if (tile instanceof TileEntityChest) {
                    ((TileEntityChest) tile).setLootTable(LootTableList.CHESTS_VILLAGE_BLACKSMITH, random.nextLong()); //TODO Proper loottable
                }
            } else if (type.equals("Ground")) {
                int baseHeight = heights.get(dataPos);
                int newHeight = this.getGround(world, dataPos).getY();
                int yOffset = baseHeight - newHeight;
                IBlockState[] blocks = new IBlockState[dataPos.getY() - position.getY()];
                BlockPos currentPos = new BlockPos(dataPos);
                for (int i = 0; i < blocks.length; i++) {
                    blocks[i] = world.getBlockState(currentPos);
                    world.setBlockState(currentPos, Blocks.AIR.getDefaultState(), 3);
                    currentPos = currentPos.down();
                }
                currentPos = dataPos.up(yOffset);
                for (IBlockState block : blocks) {
                    if (block.getBlock() == Blocks.AIR) {
                        if (!world.getBlockState(currentPos).getMaterial().isLiquid()) {
                            world.setBlockState(currentPos, block, 3);
                        }
                    } else {
                        world.setBlockState(currentPos, block, 3);
                    }
                    currentPos = currentPos.down();
                }
                world.setBlockState(dataPos, Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(dataPos.up(), Blocks.AIR.getDefaultState(), 3);
            }
        }
    }
}
