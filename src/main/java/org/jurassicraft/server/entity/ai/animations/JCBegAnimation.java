package org.jurassicraft.server.entity.ai.animations;

import net.ilexiconn.llibrary.server.animation.Animation;
import org.jurassicraft.client.animation.Animations;
import org.jurassicraft.server.entity.base.DinosaurEntity;

public class JCBegAnimation extends JCNonAutoAnimBase
{
    public JCBegAnimation(DinosaurEntity entity, Animation animation, int chance)
    {
        super(entity, animation, chance);
    }

    @Override
    public boolean shouldExecute()
    {
        return animatingEntity.getAnimation() == Animations.IDLE.get() && animatingEntity.getRNG().nextInt(chance) == 0;
    }
}
