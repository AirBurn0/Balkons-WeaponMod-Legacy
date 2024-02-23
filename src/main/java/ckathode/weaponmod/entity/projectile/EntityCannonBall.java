package ckathode.weaponmod.entity.projectile;

import ckathode.weaponmod.BalkonsWeaponMod;
import ckathode.weaponmod.PhysHelper;
import ckathode.weaponmod.WeaponDamageSource;
import ckathode.weaponmod.entity.EntityCannon;
import javax.annotation.Nonnull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCannonBall extends EntityProjectile {
    public EntityCannonBall(final World world) {
        super(world);
    }

    public EntityCannonBall(final World world, final double d, final double d1, final double d2) {
        this(world);
        this.setPosition(d, d1, d2);
    }

    public EntityCannonBall(final World world, final EntityCannon entitycannon, final float f, final float f1,
                            final boolean superPowered) {
        this(world, entitycannon.posX, entitycannon.posY + 1.0, entitycannon.posZ);
        final Entity entityPassenger = entitycannon.getPassengers().isEmpty() ? null :
                entitycannon.getPassengers().get(0);
        this.shootingEntity = entitycannon;
        if (entityPassenger instanceof EntityLivingBase) {
            this.setPickupModeFromEntity((EntityLivingBase) entityPassenger);
        } else {
            this.setPickupMode(1);
        }
        this.setSize(0.5f, 0.5f);
        final float x = -MathHelper.sin(f1 * 0.017453292f) * MathHelper.cos(f * 0.017453292f);
        final float y = -MathHelper.sin(f * 0.017453292f);
        final float z = MathHelper.cos(f1 * 0.017453292f) * MathHelper.cos(f * 0.017453292f);
        this.shoot(x, y, z, superPowered ? 4.0f : 2.0f, superPowered ? 0.1f : 2.0f);
        this.motionX += entitycannon.motionX;
        this.motionZ += entitycannon.motionZ;
        this.setIsCritical(superPowered);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        final double speed =
                MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        final double amount = 8.0;
        if (speed > 1.0) {
            for (int i1 = 1; i1 < amount; ++i1) {
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + this.motionX * i1 / amount,
                        this.posY + this.motionY * i1 / amount, this.posZ + this.motionZ * i1 / amount, 0.0, 0.0, 0.0);
            }
        }
    }

    public void createCrater() {
        if (this.world.isRemote || !this.inGround || this.isInWater()) {
            return;
        }
        this.setDead();
        final float f = this.getIsCritical() ? 5.0f : 2.5f;
        PhysHelper.createAdvancedExplosion(this.world, this, this.posX, this.posY, this.posZ, f,
                BalkonsWeaponMod.instance.modConfig.cannonDoesBlockDamage, true, false, false);
    }

    @Override
    public void onEntityHit(final Entity entity) {
        DamageSource damagesource;
        if (this.shootingEntity == null) {
            damagesource = WeaponDamageSource.causeProjectileWeaponDamage(this, this);
        } else {
            damagesource = WeaponDamageSource.causeProjectileWeaponDamage(this, this.shootingEntity);
        }
        if (entity.attackEntityFrom(damagesource, 30.0f)) {
            this.playSound(SoundEvents.ENTITY_PLAYER_HURT, 1.0f, 1.2f / (this.rand.nextFloat() * 0.4f + 0.7f));
        }
    }

    @Override
    public void onGroundHit(final RayTraceResult raytraceResult) {
        final BlockPos blockpos = raytraceResult.getBlockPos();
        this.xTile = blockpos.getX();
        this.yTile = blockpos.getY();
        this.zTile = blockpos.getZ();
        final IBlockState iblockstate = this.world.getBlockState(blockpos);
        this.inTile = iblockstate.getBlock();
        this.inData = this.inTile.getMetaFromState(iblockstate);
        this.motionX = raytraceResult.hitVec.x - this.posX;
        this.motionY = raytraceResult.hitVec.y - this.posY;
        this.motionZ = raytraceResult.hitVec.z - this.posZ;
        final float f1 =
                MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
        this.posX -= this.motionX / f1 * 0.05;
        this.posY -= this.motionY / f1 * 0.05;
        this.posZ -= this.motionZ / f1 * 0.05;
        this.inGround = true;
        if (this.inTile != null) {
            this.inTile.onEntityCollision(this.world, blockpos, iblockstate, this);
        }
        this.createCrater();
    }

    @Override
    public boolean canBeCritical() {
        return true;
    }

    @Override
    public float getAirResistance() {
        return 0.98f;
    }

    @Override
    public float getGravity() {
        return 0.04f;
    }

    @Override
    public ItemStack getPickupItem() {
        return new ItemStack(BalkonsWeaponMod.cannonBall, 1);
    }

    @Nonnull
    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(BalkonsWeaponMod.cannonBall);
    }
}
