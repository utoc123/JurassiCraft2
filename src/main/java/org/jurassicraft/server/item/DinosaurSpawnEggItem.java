package org.jurassicraft.server.item;

import net.minecraft.block.BlockFence;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
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
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.creativetab.TabHandler;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.lang.AdvLang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DinosaurSpawnEggItem extends Item
{
    public DinosaurSpawnEggItem()
    {
        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.INSTANCE.spawnEggs);
    }

    public DinosaurEntity spawnCreature(World world, EntityPlayer player, ItemStack stack, double x, double y, double z)
    {
        Dinosaur dinoInEgg = getDinosaur(stack);

        if (dinoInEgg != null)
        {
            Class<? extends DinosaurEntity> dinoClass = dinoInEgg.getDinosaurClass();

            try
            {
                DinosaurEntity dino = dinoClass.getConstructor(World.class).newInstance(player.worldObj);

                dino.setDNAQuality(100);

                int mode = getMode(stack);

                if (mode == 1)
                {
                    dino.setMale(true);
                }
                else if (mode == 2)
                {
                    dino.setMale(false);
                }

                if (player.isSneaking())
                {
                    dino.setAge(0);
                }

                dino.setPosition(x, y, z);
                dino.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360.0F), 0.0F);
                dino.rotationYawHead = dino.rotationYaw;
                dino.renderYawOffset = dino.rotationYaw;
                return dino;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        int mode = changeMode(stack);

        if (world.isRemote)
        {
            String modeString = "";

            if (mode == 0)
            {
                modeString = "random";
            }
            else if (mode == 1)
            {
                modeString = "male";
            }
            else if (mode == 2)
            {
                modeString = "female";
            }

            player.addChatMessage(new TextComponentString(new AdvLang("spawnegg.genderchange.name").withProperty("mode", I18n.translateToLocal("gender." + modeString + ".name")).build()));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack)
    {
        Dinosaur dinosaur = this.getDinosaur(stack);

        return new AdvLang("item.dino_spawn_egg.name").withProperty("dino", "entity.jurassicraft." + dinosaur.getName().replace(" ", "_").toLowerCase() + ".name").build();
    }

    public Dinosaur getDinosaur(ItemStack stack)
    {
        Dinosaur dinosaur = EntityHandler.INSTANCE.getDinosaurById(stack.getItemDamage());

        if (dinosaur == null)
        {
            dinosaur = EntityHandler.INSTANCE.achillobator;
        }

        return dinosaur;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> subtypes)
    {
        List<Dinosaur> dinosaurs = new ArrayList<>(EntityHandler.INSTANCE.getDinosaurs());

        Map<Dinosaur, Integer> ids = new HashMap<Dinosaur, Integer>();

        for (Dinosaur dino : dinosaurs)
        {
            ids.put(dino, EntityHandler.INSTANCE.getDinosaurId(dino));
        }

        Collections.sort(dinosaurs);

        for (Dinosaur dino : dinosaurs)
        {
            if (dino.shouldRegister())
            {
                subtypes.add(new ItemStack(item, 1, ids.get(dino)));
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
        {
            return EnumActionResult.SUCCESS;
        }
        else if (!player.canPlayerEdit(pos.offset(side), side, stack))
        {
            return EnumActionResult.PASS;
        }
        else
        {
            IBlockState iblockstate = world.getBlockState(pos);

            if (iblockstate.getBlock() == Blocks.mob_spawner)
            {
                TileEntity tile = world.getTileEntity(pos);

                if (tile instanceof TileEntityMobSpawner)
                {
                    MobSpawnerBaseLogic spawnerLogic = ((TileEntityMobSpawner) tile).getSpawnerBaseLogic();
                    spawnerLogic.setEntityName(EntityList.classToStringMapping.get(getDinosaur(stack).getDinosaurClass()));
                    tile.markDirty();

                    if (!player.capabilities.isCreativeMode)
                    {
                        --stack.stackSize;
                    }

                    return EnumActionResult.SUCCESS;
                }
            }

            pos = pos.offset(side);
            double yOffset = 0.0D;

            if (side == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence)
            {
                yOffset = 0.5D;
            }

            DinosaurEntity dinosaur = spawnCreature(world, player, stack, (double) pos.getX() + 0.5D, (double) pos.getY() + yOffset, (double) pos.getZ() + 0.5D);

            if (dinosaur != null)
            {
                if (stack.hasDisplayName())
                {
                    dinosaur.setCustomNameTag(stack.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode)
                {
                    --stack.stackSize;
                }

                world.spawnEntityInWorld(dinosaur);
                dinosaur.playLivingSound();
            }

            return EnumActionResult.SUCCESS;
        }
    }

    public int getMode(ItemStack stack)
    {
        return getNBT(stack).getInteger("GenderMode");
    }

    public int changeMode(ItemStack stack)
    {
        NBTTagCompound nbt = getNBT(stack);

        int mode = getMode(stack);
        mode++;

        if (mode > 2)
        {
            mode = 0;
        }

        nbt.setInteger("GenderMode", mode);

        stack.setTagCompound(nbt);

        return mode;
    }

    public NBTTagCompound getNBT(ItemStack stack)
    {
        NBTTagCompound nbt = stack.getTagCompound();

        if (nbt == null)
        {
            nbt = new NBTTagCompound();
        }

        stack.setTagCompound(nbt);

        return nbt;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lore, boolean advanced)
    {
        lore.add(I18n.translateToLocal("lore.baby_dino.name"));
        lore.add(I18n.translateToLocal("lore.change_gender.name"));
    }
}
