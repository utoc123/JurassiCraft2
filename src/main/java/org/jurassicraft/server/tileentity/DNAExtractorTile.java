package org.jurassicraft.server.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.container.DNAExtractorContainer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.GeneticsHelper;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.item.ItemHandler;
import org.jurassicraft.server.plant.Plant;
import org.jurassicraft.server.plant.PlantHandler;

import java.util.List;
import java.util.Random;

public class DNAExtractorTile extends MachineBaseTile
{
    private static final int[] INPUTS = new int[] { 0, 1 };
    private static final int[] OUTPUTS = new int[] { 2, 3, 4, 5 };

    private ItemStack[] slots = new ItemStack[6];

    @Override
    protected int getProcess(int slot)
    {
        return 0;
    }

    @Override
    protected boolean canProcess(int process)
    {
        ItemStack extraction = slots[0];
        ItemStack storage = slots[1];

        if (storage != null && storage.getItem() == ItemHandler.INSTANCE.storage_disc && extraction != null && (extraction.getItem() == ItemHandler.INSTANCE.amber || extraction.getItem() == ItemHandler.INSTANCE.sea_lamprey || extraction.getItem() == ItemHandler.INSTANCE.dino_meat) && (storage.getTagCompound() == null || !storage.getTagCompound().hasKey("Genetics")))
        {
            for (int i = 2; i < 6; i++)
            {
                if (slots[i] == null)
                {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void processItem(int process)
    {
        if (this.canProcess(process))
        {
            Random rand = worldObj.rand;
            ItemStack input = slots[0];

            ItemStack disc = null;

            Item item = input.getItem();

            if (item == ItemHandler.INSTANCE.amber || item == ItemHandler.INSTANCE.sea_lamprey)
            {
                if (input.getItemDamage() == 0)
                {
                    List<Dinosaur> possibleDinos = item == ItemHandler.INSTANCE.amber ? EntityHandler.INSTANCE.getDinosaursFromAmber() : EntityHandler.INSTANCE.getDinosaursFromSeaLampreys();

                    Dinosaur dino = possibleDinos.get(rand.nextInt(possibleDinos.size()));

                    int dinosaurId = EntityHandler.INSTANCE.getDinosaurId(dino);

                    disc = new ItemStack(ItemHandler.INSTANCE.storage_disc, 1, dinosaurId);

                    int quality = rand.nextInt(50);

                    if (rand.nextDouble() < 0.1)
                    {
                        quality += 50;
                    }

                    DinoDNA dna = new DinoDNA(quality, GeneticsHelper.randomGenetics(rand, dinosaurId, quality).toString());

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                }
                else if (input.getItemDamage() == 1)
                {
                    List<Plant> possiblePlants = PlantHandler.INSTANCE.getPlants();
                    Plant plant = possiblePlants.get(rand.nextInt(possiblePlants.size()));

                    int plantId = PlantHandler.INSTANCE.getPlantId(plant);

                    disc = new ItemStack(ItemHandler.INSTANCE.storage_disc, 1, plantId);

                    int quality = rand.nextInt(50);

                    if (rand.nextDouble() < 0.1)
                    {
                        quality += 50;
                    }

                    PlantDNA dna = new PlantDNA(plantId, quality);

                    NBTTagCompound nbt = new NBTTagCompound();
                    dna.writeToNBT(nbt);

                    disc.setTagCompound(nbt);
                }
            }
            else if (item == ItemHandler.INSTANCE.dino_meat)
            {
                disc = new ItemStack(ItemHandler.INSTANCE.storage_disc, 1, input.getItemDamage());

                disc.setTagCompound(input.getTagCompound());
            }

            int empty = getOutputSlot(disc);

            mergeStack(empty, disc);

            decreaseStackSize(0);
            decreaseStackSize(1);
        }
    }

    @Override
    protected int getMainOutput(int process)
    {
        return 2;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack)
    {
        return 2000;
    }

    @Override
    protected int getProcessCount()
    {
        return 1;
    }

    @Override
    protected int[] getInputs()
    {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process)
    {
        return getInputs();
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
        return new DNAExtractorContainer(playerInventory, this);
    }

    @Override
    public String getGuiID()
    {
        return JurassiCraft.MODID + ":dna_extractor";
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? customName : "container.dna_extractor";
    }
}
