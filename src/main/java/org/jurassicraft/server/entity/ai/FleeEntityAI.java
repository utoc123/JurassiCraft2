package org.jurassicraft.server.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import org.jurassicraft.server.entity.base.DinosaurEntity;

import java.util.LinkedList;
import java.util.List;

public class FleeEntityAI extends EntityAIBase {
    private DinosaurEntity dinosaur;
    private List<EntityLivingBase> attackers;

    public FleeEntityAI(DinosaurEntity dinosaur) {
        this.dinosaur = dinosaur;
    }

    @Override
    public boolean shouldExecute() {
        List<DinosaurEntity> entities = this.dinosaur.worldObj.getEntitiesWithinAABB(DinosaurEntity.class, this.dinosaur.getEntityBoundingBox().expand(10, 5, 10));

        this.attackers = new LinkedList<>();

        for (DinosaurEntity entity : entities) {
            if (entity != this.dinosaur) {
                for (Class<? extends EntityLivingBase> target : entity.getAttackTargets()) {
                    if (target.isAssignableFrom(this.dinosaur.getClass())) {
                        this.attackers.add(entity);
                        break;
                    }
                }
            }
        }

        return this.attackers.size() > 0;
    }

    @Override
    public boolean continueExecuting() {
        return false;
    }

    @Override
    public void startExecuting() {
        Herd herd = this.dinosaur.herd;

        if (herd != null && this.attackers != null && this.attackers.size() > 0) {
            for (EntityLivingBase attacker : this.attackers) {
                if (!herd.enemies.contains(attacker)) {
                    herd.enemies.add(attacker);
                }
            }

            herd.fleeing = true;
        }
    }
}
