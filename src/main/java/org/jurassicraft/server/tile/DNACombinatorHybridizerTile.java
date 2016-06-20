package org.jurassicraft.server.tile;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.jurassicraft.JurassiCraft;
import org.jurassicraft.server.api.Hybrid;
import org.jurassicraft.server.container.DNACombinatorHybridizerContainer;
import org.jurassicraft.server.dinosaur.Dinosaur;
import org.jurassicraft.server.entity.base.EntityHandler;
import org.jurassicraft.server.genetics.DinoDNA;
import org.jurassicraft.server.genetics.PlantDNA;
import org.jurassicraft.server.item.ItemHandler;

import java.util.ArrayList;
import java.util.List;

public class DNACombinatorHybridizerTile extends MachineBaseTile
{
    private static final int[] HYBRIDIZER_INPUTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7 };
    private static final int[] COMBINATOR_INPUTS = new int[] { 8, 9 };
    private static final int[] HYBRIDIZER_OUTPUTS = new int[] { 10 };
    private static final int[] COMBINATOR_OUTPUTS = new int[] { 11 };

    private ItemStack[] slots = new ItemStack[12];

    private boolean hybridizerMode;

    @Override
    protected int getProcess(int slot)
    {
        return 0;
    }

    private Dinosaur getHybrid()
    {
        return getHybrid(slots[0], slots[1], slots[2], slots[3], slots[4], slots[5], slots[6], slots[7]);
    }

    private Dinosaur getHybrid(ItemStack... discs)
    {
        Dinosaur hybrid = null;

        Dinosaur[] dinosaurs = new Dinosaur[discs.length];

        for (int i = 0; i < dinosaurs.length; i++)
        {
            dinosaurs[i] = getDino(discs[i]);
        }

        for (Dinosaur dino : EntityHandler.getDinosaurs())
        {
            if (dino instanceof Hybrid && dino.shouldRegister())
            {
                Hybrid dinoHybrid = (Hybrid) dino;

                int count = 0;
                boolean extra = false;

                List<Class<? extends Dinosaur>> usedGenes = new ArrayList<>();

                for (Dinosaur discDinosaur : dinosaurs)
                {
                    Class match = null;

                    for (Class clazz : dinoHybrid.getDinosaurs())
                    {
                        if (clazz.isInstance(discDinosaur) && !usedGenes.contains(clazz))
                        {
                            match = clazz;
                        }
                    }

                    if (match != null && match.isInstance(discDinosaur))
                    {
                        usedGenes.add(match);
                        count++;
                    }
                    else if (discDinosaur != null)
                    {
                        extra = true;
                    }
                }

                if (!extra && count == dinoHybrid.getDinosaurs().length)
                {
                    hybrid = dino;

                    break;
                }
            }
        }

        return hybrid;
    }

    private Dinosaur getDino(ItemStack disc)
    {
        if (disc != null)
        {
            DinoDNA data = DinoDNA.readFromNBT(disc.getTagCompound());

            return data.getDNAQuality() == 100 ? data.getDinosaur() : null;
        }
        else
        {
            return null;
        }
    }

    @Override
    protected boolean canProcess(int process)
    {
        if (hybridizerMode)
        {
            return slots[10] == null && getHybrid() != null;
        }
        else
        {
            if (slots[8] != null && slots[8].getItem() == ItemHandler.STORAGE_DISC && slots[9] != null && slots[9].getItem() == ItemHandler.STORAGE_DISC)
            {
                if (slots[8].getTagCompound() != null && slots[9].getTagCompound() != null && slots[11] == null && slots[8].getItemDamage() == slots[9].getItemDamage() && slots[8].getTagCompound().getString("StorageId").equals(slots[9].getTagCompound().getString("StorageId")))
                {
                    return true;
                }
            }

            return false;
        }
    }

    @Override
    protected void processItem(int process)
    {
        if (this.canProcess(process))
        {
            if (hybridizerMode)
            {
                Dinosaur hybrid = getHybrid();

                NBTTagCompound nbt = new NBTTagCompound();

                DinoDNA dna = new DinoDNA(hybrid, 100, slots[0].getTagCompound().getString("Genetics"));
                dna.writeToNBT(nbt);

                ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC, 1, EntityHandler.getDinosaurId(hybrid));
                output.setItemDamage(EntityHandler.getDinosaurId(dna.getDinosaur()));
                output.setTagCompound(nbt);

                mergeStack(getOutputSlot(output), output);

                for (int i = 0; i < 8; i++)
                {
                    decreaseStackSize(i);
                }
            }
            else
            {
                ItemStack output = new ItemStack(ItemHandler.STORAGE_DISC, 1, slots[8].getItemDamage());

                String storageId = slots[8].getTagCompound().getString("StorageId");

                if (storageId.equals("DinoDNA"))
                {
                    DinoDNA dna1 = DinoDNA.readFromNBT(slots[8].getTagCompound());
                    DinoDNA dna2 = DinoDNA.readFromNBT(slots[9].getTagCompound());

                    int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

                    if (newQuality > 100)
                    {
                        newQuality = 100;
                    }

                    DinoDNA newDNA = new DinoDNA(dna1.getDinosaur(), newQuality, dna1.getGenetics());

                    NBTTagCompound outputTag = new NBTTagCompound();
                    newDNA.writeToNBT(outputTag);
                    output.setTagCompound(outputTag);
                }
                else if (storageId.equals("PlantDNA"))
                {
                    PlantDNA dna1 = PlantDNA.readFromNBT(slots[8].getTagCompound());
                    PlantDNA dna2 = PlantDNA.readFromNBT(slots[9].getTagCompound());

                    int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

                    if (newQuality > 100)
                    {
                        newQuality = 100;
                    }

                    PlantDNA newDNA = new PlantDNA(dna1.getPlant(), newQuality);

                    NBTTagCompound outputTag = new NBTTagCompound();
                    newDNA.writeToNBT(outputTag);
                    output.setTagCompound(outputTag);
                }

                mergeStack(11, output);

                decreaseStackSize(8);
                decreaseStackSize(9);
            }
        }
    }

    @Override
    protected int getMainOutput(int process)
    {
        return hybridizerMode ? 10 : 11;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack)
    {
        return 1000;
    }

    @Override
    protected int getProcessCount()
    {
        return 1;
    }

    @Override
    protected int[] getInputs()
    {
        return hybridizerMode ? HYBRIDIZER_INPUTS : COMBINATOR_INPUTS;
    }

    @Override
    protected int[] getInputs(int process)
    {
        return getInputs();
    }

    @Override
    protected int[] getOutputs()
    {
        return hybridizerMode ? HYBRIDIZER_OUTPUTS : COMBINATOR_OUTPUTS;
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
        return new DNACombinatorHybridizerContainer(playerInventory, this);
    }

    @Override
    public String getGuiID()
    {
        return JurassiCraft.MODID + ":dna_combinator_hybridizer";
    }

    @Override
    public String getName()
    {
        return hasCustomName() ? customName : "container.dna_combinator_hybridizer";
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        this.hybridizerMode = nbt.getBoolean("HybridizerMode");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        nbt = super.writeToNBT(nbt);

        nbt.setBoolean("HybridizerMode", hybridizerMode);

        return nbt;
    }

    public void setMode(boolean mode)
    {
        this.hybridizerMode = mode;
        this.processTime[0] = 0;
    }

    public boolean getMode()
    {
        return hybridizerMode;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(hybridizerMode ? "container.dna_hybridizer" : "container.dna_combinator");
    }
}
