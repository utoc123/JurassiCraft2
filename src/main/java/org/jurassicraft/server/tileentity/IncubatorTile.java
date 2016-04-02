package org.jurassicraft.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.IncubatorContainer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.DinosaurEntity;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.entity.item.CageSmallEntity;
import org.jurassicraft.server.item.DinosaurEggItem;

import java.util.List;

public class IncubatorTile extends MachineBaseTile
{
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4 };
    private static final int[] OTHER = new int[] { 5 };
    private static final int[] OUTPUTS = new int[0];

    private int[] temperature = new int[5];

    private ItemStack[] slots = new ItemStack[6];

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);

        for (int i = 0; i < getProcessCount(); i++)
        {
            temperature[i] = compound.getShort("Temperature" + i);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);

        for (int i = 0; i < getProcessCount(); i++)
        {
            compound.setShort("Temperature" + i, (short) this.temperature[i]);
        }
    }

    @Override
    protected int getProcess(int slot)
    {
        return slot;
    }

    @Override
    protected boolean canProcess(int process)
    {
        return slots[process] != null && slots[process].stackSize > 0 && slots[process].getItem() instanceof DinosaurEggItem;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return side == EnumFacing.DOWN ? getInputs() : OTHER;
    }

    @Override
    protected void processItem(int process)
    {
        if (this.canProcess(process) && !worldObj.isRemote)
        {
            ItemStack egg = slots[process];

            Dinosaur dinoInEgg = EntityHandler.INSTANCE.getDinosaurById(egg.getMetadata());

            if (dinoInEgg != null)
            {
                Class<? extends DinosaurEntity> dinoClass = dinoInEgg.getDinosaurClass();

                try
                {
                    DinosaurEntity dino = dinoClass.getConstructor(World.class).newInstance(worldObj);

                    dino.setDNAQuality(egg.getTagCompound().getInteger("DNAQuality"));
                    dino.setGenetics(egg.getTagCompound().getString("Genetics"));

                    dino.setMale(temperature[process] > 50);
                    dino.setAge(0);

                    int blockX = pos.getX();
                    int blockY = pos.getY();
                    int blockZ = pos.getZ();

                    List<CageSmallEntity> cages = worldObj.getEntitiesWithinAABB(CageSmallEntity.class, new AxisAlignedBB(blockX - 2, blockY, blockZ - 2, blockX + 2, blockY + 1, blockZ + 2));

                    CageSmallEntity spawnCage = null;

                    for (CageSmallEntity cage : cages)
                    {
                        if (cage.getEntity() == null)
                        {
                            spawnCage = cage;
                            break;
                        }
                    }

                    if (spawnCage != null)
                    {
                        spawnCage.setEntity(dino);
                    }
                    else
                    {
                        dino.setLocationAndAngles(blockX + 0.5, blockY + 1, blockZ + 0.5, MathHelper.wrapDegrees(worldObj.rand.nextFloat() * 360.0F), 0.0F);
                        dino.rotationYawHead = dino.rotationYaw;
                        dino.renderYawOffset = dino.rotationYaw;

                        worldObj.spawnEntityInWorld(dino);
                    }

                    decreaseStackSize(process);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected int getMainOutput(int process)
    {
        return 0;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack)
    {
        return 8000;
    }

    @Override
    protected int getProcessCount()
    {
        return 5;
    }

    @Override
    protected int[] getInputs()
    {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process)
    {
        return new int[] { process };
    }

    @Override
    protected int[] getOutputs()
    {
        return OUTPUTS;
    }

    @Override
    protected ItemStack[] getSlots()
    {
        return slots;
    }

    @Override
    protected void setSlots(ItemStack[] slots)
    {
        this.slots = slots;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
    {
        return new IncubatorContainer(playerInventory, this);
    }

    @Override
    public String getGuiID()
    {
        return JurassiCraft.MODID + ":incubator";
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? customName : "container.incubator";
    }

    @Override
    public int getField(int id)
    {
        if (id < 5)
        {
            return processTime[id];
        }
        else if (id < 10)
        {
            return totalProcessTime[id - 5];
        }
        else if (id < 15)
        {
            return temperature[id - 10];
        }

        return 0;
    }

    @Override
    public void setField(int id, int value)
    {
        if (id < 5)
        {
            processTime[id] = value;
        }
        else if (id < 10)
        {
            totalProcessTime[id - 5] = value;
        }
        else if (id < 15)
        {
            temperature[id - 10] = value;
        }
    }

    public String getCommandSenderName() // Forge Version compatibility, keep both getName and getCommandSenderName
    {
        return getName();
    }
}
