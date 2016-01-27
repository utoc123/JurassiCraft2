package org.jurassicraft.server.entity.base;

import net.minecraft.entity.monster.IMob;
import net.minecraft.world.World;
import org.jurassicraft.server.entity.ai.JCPanicEntityAI;

public abstract class DefensiveDinosaurEntity extends DinosaurEntity implements IMob
{
    private final JCPanicEntityAI entityAIPanic = new JCPanicEntityAI(this, 1.25D);

    public DefensiveDinosaurEntity(World world)
    {
        super(world);
        tasks.addTask(1, entityAIPanic);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        updateArmSwingProgress();
    }
}
