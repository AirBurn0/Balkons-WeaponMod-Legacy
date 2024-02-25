package ckathode.weaponmod.item;

import ckathode.weaponmod.PhysHelper;
import ckathode.weaponmod.PlayerWeaponData;
import ckathode.weaponmod.WarhammerExplosion;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MeleeCompWarhammer extends MeleeComponent {
    public static int CHARGE_DELAY = 400;

    public MeleeCompWarhammer(IItemTier itemTier) {
        super(MeleeSpecs.WARHAMMER, itemTier);
    }

    @Override
    public float getBlockDamage(ItemStack itemstack, IBlockState block) {
        float f = super.getBlockDamage(itemstack, block);
        float f2 = weaponMaterial.getAttackDamage() + 2.0f;
        return f * f2;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemstack, World world,
                                     EntityLivingBase entityliving, int i) {
        EntityPlayer entityplayer = (EntityPlayer) entityliving;
        int j = getUseDuration(itemstack) - i;
        float f = j / 20.0f;
        f = (f * f + f * 2.0f) / 4.0f;
        if (f > 1.0f) {
            superSmash(itemstack, world, entityplayer);
        }
    }

    protected void superSmash(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.swingArm(EnumHand.MAIN_HAND);
        float f = getEntityDamage() / 2.0f;
        WarhammerExplosion expl = new WarhammerExplosion(world, entityplayer, entityplayer.posX,
                entityplayer.posY, entityplayer.posZ, f, false, true);
        expl.doEntityExplosion(DamageSource.causePlayerDamage(entityplayer));
        expl.doParticleExplosion(true, false);
        PhysHelper.sendExplosion(world, expl, true, false);
        itemstack.damageItem(16, entityplayer);
        entityplayer.addExhaustion(6.0f);
        setSmashed(entityplayer);
    }

    public void setSmashed(EntityPlayer entityplayer) {
        PlayerWeaponData.setLastWarhammerSmashTicks(entityplayer, entityplayer.ticksExisted);
    }

    public boolean isCharged(EntityPlayer entityplayer) {
        return entityplayer.ticksExisted > PlayerWeaponData.getLastWarhammerSmashTicks(entityplayer) + 400;
    }

    @Override
    public EnumAction getUseAction(ItemStack itemstack) {
        return EnumAction.BOW;
    }

    @Override
    public int getUseDuration(ItemStack itemstack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityplayer,
                                                    EnumHand hand) {
        ItemStack itemstack = entityplayer.getHeldItem(hand);
        if (itemstack.isEmpty()) {
            return new ActionResult<>(EnumActionResult.FAIL, itemstack);
        }
        if (isCharged(entityplayer)) {
            entityplayer.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
        }
        return new ActionResult<>(EnumActionResult.FAIL, itemstack);
    }
}
