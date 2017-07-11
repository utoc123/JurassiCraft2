package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.entity.ActionFigureBlockEntity;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActionFigureItem extends Item {
    public ActionFigureItem() {
        super();
        this.setCreativeTab(TabHandler.DECORATIONS);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void initModels(Collection<Dinosaur> dinos) {

        Map<Integer, ModelResourceLocation> standard = new HashMap<>();
        Map<Integer, ModelResourceLocation> fresh = new HashMap<>();
        Map<Integer, ModelResourceLocation> fossil = new HashMap<>();
        for (Dinosaur dino : dinos) {
            int dex = EntityHandler.getDinosaurId(dino);
            String dinoName = dino.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
            standard.put(dex,
                    new ModelResourceLocation("jurassicraft:action_figure/action_figure_" + dinoName, "inventory"));
            fresh.put(dex,
                    new ModelResourceLocation("jurassicraft:skeleton/fresh/skeleton_fresh_" + dinoName, "inventory"));
            fossil.put(dex,
                    new ModelResourceLocation("jurassicraft:skeleton/fossil/skeleton_fossil_" + dinoName, "inventory"));
        }
        for (ModelResourceLocation x : standard.values()) {
            ModelBakery.registerItemVariants(this, x);
        }
        for (ModelResourceLocation x : fresh.values()) {
            ModelBakery.registerItemVariants(this, x);
        }
        for (ModelResourceLocation x : fossil.values()) {
            ModelBakery.registerItemVariants(this, x);
        }

        ModelLoader.setCustomMeshDefinition(this, stack -> {
            if (!isSkeleton(stack)) {
                return standard.get(stack.getMetadata());
            } else {
                return getMode(stack) == 1 ? fossil.get(stack.getMetadata()) : fresh.get(stack.getMetadata());
            }
        });
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand,
            EnumFacing side, float hitX, float hitY, float hitZ) {
        pos = pos.offset(side);

        if (player.canPlayerEdit(pos, side, stack)) {
            Block block = BlockHandler.ACTION_FIGURE;

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState state = block.getDefaultState();
                world.setBlockState(pos, block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player));
                block.onBlockPlacedBy(world, pos, state, player, stack);

                int mode = this.getMode(stack);

                ActionFigureBlockEntity tile = (ActionFigureBlockEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setDinosaur(stack.getItemDamage(), mode > 0 ? mode == 1 : world.rand.nextBoolean());
                    tile.setRot(-(int) player.getRotationYawHead());
                    tile.isSkeleton = this.isSkeleton(stack);
                    tile.markDirty();
                    if (!player.capabilities.isCreativeMode) {
                        stack.stackSize--;
                    }
                }

                return EnumActionResult.SUCCESS;
            }
        }

        return EnumActionResult.PASS;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = this.getDinosaur(stack).getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        if (!this.isSkeleton(stack)) {
            return new LangHelper("item.action_figure.name")
                    .withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
        }
        return new LangHelper("item.skeleton." + (this.getMode(stack) == 1 ? "fossil" : "fresh") + ".name")
                .withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        return EntityHandler.getDinosaurById(stack.getMetadata());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());

        Collections.sort(dinosaurs);

        for (Dinosaur dinosaur : dinosaurs) {

            if (dinosaur.shouldRegister()) {
                subtypes.add(
                        this.establishNBT(new ItemStack(item, 1, EntityHandler.getDinosaurId(dinosaur)), 0, false));
                for (int gender = 1; gender < 3; gender++) {
                    subtypes.add(this.establishNBT(new ItemStack(item, 1, EntityHandler.getDinosaurId(dinosaur)), gender, true));
                }

            }
        }
    }

    // Gender is 0 for random, 1 for male, 2 for female

    public ItemStack establishNBT(ItemStack itemin, int gender, boolean isSkeleton) {
        NBTTagCompound nbttagcompound = itemin.hasTagCompound() ? itemin.getTagCompound() : new NBTTagCompound();
        nbttagcompound.setInteger("GenderMode", gender % 3);
        nbttagcompound.setBoolean("IsSkeleton", isSkeleton);
        itemin.setTagCompound(nbttagcompound);

        return itemin;
    }

    public int getMode(ItemStack stack) {
        return this.getNBT(stack).getInteger("GenderMode");
    }

    public boolean isSkeleton(ItemStack stack) {
        return this.getNBT(stack).getBoolean("IsSkeleton");
    }

    public int changeMode(ItemStack stack) {
        NBTTagCompound nbt = this.getNBT(stack);

        int mode = this.getMode(stack) + 1;
        mode %= 3;

        nbt.setInteger("GenderMode", mode);

        stack.setTagCompound(nbt);

        return mode;
    }

    public void setSkeleton(ItemStack stack, Boolean skel) {
        NBTTagCompound nbt = this.getNBT(stack);

        nbt.setBoolean("IsSkeleton", skel);

        stack.setTagCompound(nbt);
    }

    public NBTTagCompound getNBT(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
        }
        stack.setTagCompound(nbt);
        return nbt;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lore, boolean advanced) {
        if(!isSkeleton(stack)){
            lore.add(TextFormatting.BLUE + I18n.format("lore.change_gender.name"));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!this.isSkeleton(stack)) {
            int mode = this.changeMode(stack);
            if (world.isRemote) {
                String modeString = "";
                if (mode == 0) {
                    modeString = "random";
                } else if (mode == 1) {
                    modeString = "male";
                } else if (mode == 2) {
                    modeString = "female";
                }
                player.sendMessage(new TextComponentString(new LangHelper("actionfigure.genderchange.name")
                        .withProperty("mode", I18n.format("gender." + modeString + ".name")).build()));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }

}
