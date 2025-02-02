package ckathode.weaponmod;

import ckathode.weaponmod.network.MsgExplosion;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public final class PhysHelper {
    private static double kbMotionX = 0.0;
    private static double kbMotionY = 0.0;
    private static double kbMotionZ = 0.0;
    private static int knockBackModifier = 0;

    public static AdvancedExplosion createStandardExplosion(World world, Entity entity, double d,
                                                            double d1, double d2, float size,
                                                            boolean flame, boolean smoke) {
        AdvancedExplosion explosion = new AdvancedExplosion(world, entity, d, d1, d2, size, flame, smoke);
        explosion.doEntityExplosion();
        explosion.doBlockExplosion();
        explosion.doParticleExplosion(true, true);
        sendExplosion(world, explosion, true, true);
        return explosion;
    }

    public static AdvancedExplosion createAdvancedExplosion(World world, Entity entity, double d,
                                                            double d1, double d2, float size,
                                                            boolean destroyBlocks,
                                                            boolean spawnSmallParticles,
                                                            boolean spawnBigParticles, boolean flame,
                                                            boolean smoke) {
        AdvancedExplosion explosion = new AdvancedExplosion(world, entity, d, d1, d2, size, flame, smoke);
        explosion.doEntityExplosion();
        if (destroyBlocks) {
            explosion.doBlockExplosion();
        }
        if (flame) {
            explosion.doFlaming();
        }
        explosion.doParticleExplosion(spawnSmallParticles, spawnBigParticles);
        sendExplosion(world, explosion, spawnSmallParticles, spawnBigParticles);
        return explosion;
    }

    public static AdvancedExplosion createAdvancedExplosion(World world, Entity entity,
                                                            DamageSource damagesource, double d,
                                                            double d1, double d2, float size,
                                                            boolean destroyBlocks,
                                                            boolean spawnSmallParticles,
                                                            boolean spawnBigParticles, boolean flame,
                                                            boolean smoke) {
        AdvancedExplosion explosion = new AdvancedExplosion(world, entity, d, d1, d2, size, flame, smoke);
        explosion.doEntityExplosion(damagesource);
        if (destroyBlocks) {
            explosion.doBlockExplosion();
        }
        if (flame) {
            explosion.doFlaming();
        }
        explosion.doParticleExplosion(spawnSmallParticles, spawnBigParticles);
        sendExplosion(world, explosion, spawnSmallParticles, spawnBigParticles);
        return explosion;
    }

    public static AdvancedExplosion createAdvancedExplosion(World world, Entity entity, double d,
                                                            double d1, double d2, float size,
                                                            boolean destroyBlocks, boolean spawnParticles,
                                                            boolean flame, boolean smoke) {
        AdvancedExplosion explosion = new AdvancedExplosion(world, entity, d, d1, d2, size, flame, smoke);
        explosion.doEntityExplosion();
        if (destroyBlocks) {
            explosion.doBlockExplosion();
        }
        if (flame) {
            explosion.doFlaming();
        }
        explosion.doParticleExplosion(spawnParticles, spawnParticles);
        sendExplosion(world, explosion, spawnParticles, spawnParticles);
        return explosion;
    }

    public static void sendExplosion(World world, AdvancedExplosion explosion, boolean smallparts, boolean bigparts) {
        if (world instanceof WorldServer && !world.isRemote) {
            MsgExplosion msg = new MsgExplosion(explosion, smallparts, bigparts);
            BalkonsWeaponMod.instance.messagePipeline.sendToAllAround(msg,
                    new NetworkRegistry.TargetPoint(world.provider.dimensionId, explosion.explosionX,
                            explosion.explosionY, explosion.explosionZ, 64.0));
        }
    }

    public static void knockBack(EntityLivingBase entityliving, EntityLivingBase attacker, float knockback) {
        entityliving.motionX = kbMotionX;
        entityliving.motionY = kbMotionY;
        entityliving.motionZ = kbMotionZ;
        double dx = attacker.posX - entityliving.posX;
        double dz;
        for (dz = attacker.posZ - entityliving.posZ; dx * dx + dz * dz < 1E-4D;
             dz = (Math.random() - Math.random()) * 0.01D) {
            dx = (Math.random() - Math.random()) * 0.01D;
        }
        entityliving.attackedAtYaw =
                (float) (Math.atan2(dz, dx) * 180.0 / 3.141592653589793) - entityliving.rotationYaw;
        float f = MathHelper.sqrt_double(dx * dx + dz * dz);
        entityliving.motionX -= dx / f * knockback;
        entityliving.motionY += knockback;
        entityliving.motionZ -= dz / f * knockback;
        if (entityliving.motionY > 0.4) {
            entityliving.motionY = 0.4;
        }
        if (knockBackModifier > 0) {
            dx = -Math.sin(Math.toRadians(attacker.rotationYaw)) * knockBackModifier * 0.5;
            dz = Math.cos(Math.toRadians(attacker.rotationYaw)) * knockBackModifier * 0.5;
            entityliving.addVelocity(dx, 0.1, dz);
        }
        if (entityliving instanceof EntityPlayerMP) {
            ((EntityPlayerMP) entityliving).playerNetServerHandler.sendPacket(new S12PacketEntityVelocity(entityliving));
        }
        knockBackModifier = 0;
        kbMotionX = (kbMotionY = (kbMotionZ = 0.0));
    }

    public static void prepareKnockbackOnEntity(EntityLivingBase attacker, EntityLivingBase entity) {
        knockBackModifier = EnchantmentHelper.getKnockbackModifier(attacker, entity);
        if (attacker.isSprinting()) {
            ++knockBackModifier;
        }
        kbMotionX = entity.motionX;
        kbMotionY = entity.motionY;
        kbMotionZ = entity.motionZ;
    }

}
