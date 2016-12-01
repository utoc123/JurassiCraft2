package org.jurassicraft.server.world.structure;

import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.world.LootTableHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VisitorCentreGenerator extends StructureGenerator {
    private static final ResourceLocation STRUCTURE = new ResourceLocation(JurassiCraft.MODID, "visitor_centre");
    private static final Map<String, ResourceLocation> LOOT_TABLES = new HashMap<>();

    static {
        LOOT_TABLES.put("GroundStorage", LootTableHandler.VISITOR_GROUND_STORAGE);
        LOOT_TABLES.put("ControlRoom", LootTableHandler.VISITOR_CONTROL_ROOM);
        LOOT_TABLES.put("Laboratory", LootTableHandler.VISITOR_LABORATORY);
        LOOT_TABLES.put("Cryonics", LootTableHandler.VISITOR_CRYONICS);
        LOOT_TABLES.put("Infirmary", LootTableHandler.VISITOR_INFIRMARY);
        LOOT_TABLES.put("Garage", LootTableHandler.VISITOR_GARAGE);
        LOOT_TABLES.put("StaffDorms", LootTableHandler.VISITOR_STAFF_DORMS);
        LOOT_TABLES.put("Kitchen", LootTableHandler.VISITOR_KITCHEN);
        LOOT_TABLES.put("DormTower", LootTableHandler.VISITOR_DORM_TOWER);
    }

    public VisitorCentreGenerator(Random rand) {
        super(rand, 83, 35, 105); //TODO
    }

    @Override
    protected void generateStructure(World world, Random random, BlockPos position) {
        System.out.println("Generate at " + position);
        MinecraftServer server = world.getMinecraftServer();
        TemplateManager templateManager = world.getSaveHandler().getStructureTemplateManager();
        PlacementSettings settings = new PlacementSettings().setRotation(this.rotation).setMirror(this.mirror);
        Template template = templateManager.getTemplate(server, STRUCTURE);
        template.addBlocksToWorldChunk(world, position, settings);
        Map<BlockPos, String> dataBlocks = template.getDataBlocks(position, settings);
        for (Map.Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String type = entry.getValue();
            BlockPos dataPos = entry.getKey();
            ResourceLocation lootTable = LOOT_TABLES.get(type);
            if (lootTable != null) {
                TileEntity tile = world.getTileEntity(dataPos.down());
                if (tile instanceof TileEntityChest) {
                    ((TileEntityChest) tile).setLootTable(lootTable, random.nextLong());
                }
            }
        }
    }

    @Override
    public int getOffsetY() {
        return -3;
    }
}
