package org.jurassicraft.client.sound;

import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jurassicraft.server.entity.vehicle.CarEntity;

@SideOnly(Side.CLIENT)
public class CarSound extends MovingSound
{
    private CarEntity entity;

    public CarSound(CarEntity entity)
    {
        super(SoundHandler.CAR_MOVE, SoundCategory.BLOCKS);
        this.entity = entity;
        this.attenuationType = AttenuationType.NONE;
    }

    @Override
    public boolean canRepeat()
    {
        return true;
    }

    @Override
    public float getVolume()
    {
        return Math.abs(entity.wheelRotateAmount) + 0.001F;
    }

    @Override
    public float getPitch()
    {
        return Math.min(1.0F, this.getVolume()) * 0.5F + 0.5F;
    }

    @Override
    public void update()
    {
        this.xPosF = (float) entity.posX;
        this.yPosF = (float) entity.posY;
        this.zPosF = (float) entity.posZ;
    }
}
