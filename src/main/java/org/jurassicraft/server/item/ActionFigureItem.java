package org.jurassicraft.server.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.block.BlockHandler;
import org.jurassicraft.server.block.OrientedBlock;
import org.jurassicraft.server.block.entity.ActionFigureBlockEntity;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class ActionFigureItem extends Item {
    public ActionFigureItem() {
        super();
        this.setCreativeTab(TabHandler.DECORATIONS);
        this.setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        pos = pos.offset(side);

        if (player.canPlayerEdit(pos, side, ItemStack.EMPTY)) {
            Block block = BlockHandler.ACTION_FIGURE;

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState state = block.getDefaultState();
                world.setBlockState(pos, ((OrientedBlock) block).onBlockPlaced(world, pos, side, hitX, hitY, hitZ, 0, player));
                block.onBlockPlacedBy(world, pos, state, player, ItemStack.EMPTY);

                int mode = this.getMode(player.getHeldItemMainhand());

                ActionFigureBlockEntity tile = (ActionFigureBlockEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setDinosaur( player.getHeldItemMainhand().getItemDamage(), mode > 0 ? mode == 1 : world.rand.nextBoolean());
                    if (!player.capabilities.isCreativeMode) {
                    	 player.getHeldItemMainhand().shrink(1);;
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

        return new LangHelper("item.action_figure.name").withProperty("dino", "entity.jurassicraft." + dinoName + ".name").build();
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
                subtypes.add(new ItemStack(item, 1, EntityHandler.getDinosaurId(dinosaur)));
            }
        }
    }

    public int getMode(ItemStack stack) {
        return this.getNBT(stack).getInteger("GenderMode");
    }

    public int changeMode(ItemStack stack) {
        NBTTagCompound nbt = this.getNBT(stack);

        int mode = this.getMode(stack) + 1;
        mode %= 3;

        nbt.setInteger("GenderMode", mode);

        stack.setTagCompound(nbt);

        return mode;
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
        lore.add(TextFormatting.BLUE + I18n.format("lore.change_gender.name"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        int mode = this.changeMode(ItemStack.EMPTY);
        if (world.isRemote) {
            String modeString = "";
            if (mode == 0) {
                modeString = "random";
            } else if (mode == 1) {
                modeString = "male";
            } else if (mode == 2) {
                modeString = "female";
            }
            player.sendMessage(new TextComponentString(new LangHelper("actionfigure.genderchange.name").withProperty("mode", I18n.format("gender." + modeString + ".name")).build()));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
    }
}

