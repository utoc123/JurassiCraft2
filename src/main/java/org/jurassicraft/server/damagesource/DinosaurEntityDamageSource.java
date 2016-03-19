package org.jurassicraft.server.damagesource;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

public class DinosaurEntityDamageSource extends DamageSource
{
    protected Entity entity;
    private boolean isThornsDamage = false;

    public DinosaurEntityDamageSource(String damageType, Entity entity)
    {
        super(damageType);
        this.entity = entity;
    }

    /**
     * Sets this EntityDamageSource as originating from Thorns armor
     */
    public DinosaurEntityDamageSource setIsThornsDamage()
    {
        this.isThornsDamage = true;
        return this;
    }

    public boolean getIsThornsDamage()
    {
        return this.isThornsDamage;
    }

    public Entity getEntity()
    {
        return this.entity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public ITextComponent getDeathMessage(EntityLivingBase entity)
    {
        ItemStack itemstack = this.entity instanceof EntityLivingBase ? ((EntityLivingBase) this.entity).getHeldItemMainhand() : null;
        String s = "death.attack." + this.damageType;
        String s1 = s + ".item";
        return itemstack != null && itemstack.hasDisplayName() && I18n.canTranslate(s1) ? new TextComponentTranslation(s1, entity.getDisplayName(), this.entity.getDisplayName(), itemstack.getChatComponent()): new TextComponentTranslation(s, new Object[] {entity.getDisplayName(), this.entity.getDisplayName()});
    }

    @Override
    public Vec3d getDamageLocation()
    {
        return new Vec3d(this.entity.posX, this.entity.posY, this.entity.posZ);
    }

    @Override
    public boolean isDifficultyScaled()
    {
        return false;
    }
}