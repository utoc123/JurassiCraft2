package org.jurassicraft.server.item;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.DinosaurEntity;
import org.jurassicraft.server.entity.EntityHandler;
import org.jurassicraft.server.tab.TabHandler;
import org.jurassicraft.server.util.LangHelper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class DinosaurSpawnEggItem extends Item {
    public DinosaurSpawnEggItem() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        return true;
    }

    public DinosaurEntity spawnDinosaur(World world, EntityPlayer player, ItemStack stack, double x, double y, double z) {
        Dinosaur dinosaur = this.getDinosaur(stack);
        if (dinosaur != null) {
            Class<? extends DinosaurEntity> entityClass = dinosaur.getDinosaurClass();
            try {
                DinosaurEntity entity = entityClass.getConstructor(World.class).newInstance(player.world);
                entity.setDNAQuality(100);

                int mode = this.getMode(stack);
                if (mode > 0) {
                    entity.setMale(mode == 1);
                }

                if (player.isSneaking()) {
                    entity.setAge(0);
                }

                entity.setPosition(x, y, z);
                entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                entity.rotationYawHead = entity.rotationYaw;
                entity.renderYawOffset = entity.rotationYaw;
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
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
            player.sendMessage(new TextComponentString(new LangHelper("spawnegg.genderchange.name").withProperty("mode", I18n.format("gender." + modeString + ".name")).build()));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = this.getDinosaur(stack);

        return new LangHelper("item.dino_spawn_egg.name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().replace(" ", "_").toLowerCase(Locale.ENGLISH) + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        Dinosaur dinosaur = EntityHandler.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null) {
            dinosaur = EntityHandler.VELOCIRAPTOR;
        }

        return dinosaur;
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

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else if (!player.canPlayerEdit(pos.offset(side), side, stack)) {
            return EnumActionResult.PASS;
        } else {
            IBlockState state = world.getBlockState(pos);

            if (state.getBlock() == Blocks.MOB_SPAWNER) {
                TileEntity tile = world.getTileEntity(pos);

                if (tile instanceof TileEntityMobSpawner) {
                    MobSpawnerBaseLogic spawnerLogic = ((TileEntityMobSpawner) tile).getSpawnerBaseLogic();
                    spawnerLogic.setEntityName(EntityList.CLASS_TO_NAME.get(this.getDinosaur(stack).getDinosaurClass()));
                    tile.markDirty();

                    if (!player.capabilities.isCreativeMode) {
                        --stack.stackSize;
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            pos = pos.offset(side);
            double yOffset = 0.0D;

            if (side == EnumFacing.UP && state.getBlock() instanceof BlockFence) {
                yOffset = 0.5D;
            }

            DinosaurEntity dinosaur = this.spawnDinosaur(world, player, stack, pos.getX() + 0.5D, pos.getY() + yOffset, pos.getZ() + 0.5D);

            if (dinosaur != null) {
                if (stack.hasDisplayName()) {
                    dinosaur.setCustomNameTag(stack.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode) {
                    --stack.stackSize;
                }

                world.spawnEntity(dinosaur);
                dinosaur.playLivingSound();
            }

            return EnumActionResult.SUCCESS;
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
        lore.add(TextFormatting.BLUE + I18n.format("lore.baby_dino.name"));
        lore.add(TextFormatting.YELLOW + I18n.format("lore.change_gender.name"));
    }
}
