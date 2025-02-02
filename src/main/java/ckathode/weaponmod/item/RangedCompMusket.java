package ckathode.weaponmod.item;

import ckathode.weaponmod.ReloadHelper.ReloadState;
import ckathode.weaponmod.entity.projectile.EntityMusketBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class RangedCompMusket extends RangedComponent {
    protected ItemMusket musket;

    public RangedCompMusket() {
        super(RangedSpecs.MUSKET);
    }

    @Override
    protected void onSetItem() {
        super.onSetItem();
        if (item instanceof ItemMusket) {
            musket = (ItemMusket) item;
        }
    }

    @Override
    public void effectReloadDone(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        entityplayer.swingItem();
        world.playSoundAtEntity(entityplayer, "random.click", 1.0F,
                1.0F / (weapon.getItemRand().nextFloat() * 0.4F + 0.8F));
    }

    @Override
    public void fire(ItemStack itemstack, World world, EntityLivingBase entityliving, int i) {
        EntityPlayer entityplayer = (EntityPlayer) entityliving;
        int j = getMaxItemUseDuration(itemstack) - i;
        float f = j / 20.0f;
        f = (f * f + f * 2.0f) / 3.0f;
        if (f > 1.0f) {
            f = 1.0f;
        }
        f += 0.02f;
        if (!world.isRemote) {
            EntityMusketBullet entitymusketbullet = new EntityMusketBullet(world, entityplayer);
            entitymusketbullet.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0f, 5.0f,
                    1.0f / f);
            applyProjectileEnchantments(entitymusketbullet, itemstack);
            world.spawnEntityInWorld(entitymusketbullet);
        }
        int deltaDamage = 1;
        boolean flag = itemstack.getItemDamage() + deltaDamage >= itemstack.getMaxDamage();
        if (flag && musket != null && musket.hasBayonet()) {
            int bayonetDamage = itemstack.hasTagCompound() ? itemstack.getTagCompound().getShort("bayonetDamage") : 0;
            ItemStack newStack = new ItemStack(musket.bayonetItem, 1, bayonetDamage);
            itemstack.damageItem(deltaDamage, entityplayer);
            if (itemstack.stackSize <= 0) {
                WMItem.deleteStack(entityplayer.inventory, itemstack);
            }
            entityplayer.inventory.addItemStackToInventory(newStack);
        } else {
            itemstack.damageItem(deltaDamage, entityplayer);
            if (itemstack.stackSize <= 0) {
                WMItem.deleteStack(entityplayer.inventory, itemstack);
            }
            RangedComponent.setReloadState(itemstack, ReloadState.STATE_NONE);
        }
        postShootingEffects(itemstack, entityplayer, world);
    }

    @Override
    public void effectPlayer(ItemStack itemstack, EntityPlayer entityplayer, World world) {
        float f = entityplayer.isSneaking() ? -0.05f : -0.1f;
        double d = -MathHelper.sin(entityplayer.rotationYaw * 0.017453292f) * MathHelper.cos(0.0f) * f;
        double d2 = MathHelper.cos(entityplayer.rotationYaw * 0.017453292f) * MathHelper.cos(0.0f) * f;
        entityplayer.rotationPitch -= (entityplayer.isSneaking() ? 7.5f : 15.0f);
        entityplayer.addVelocity(d, 0.0, d2);
    }

    @Override
    public void effectShoot(World world, double x, double y, double z, float yaw,
                            float pitch) {
        world.playSoundEffect(x, y, z, "random.explode", 3F, 1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.7F));
        world.playSoundEffect(x, y, z, "ambient.weather.thunder", 3F,
                1F / (weapon.getItemRand().nextFloat() * 0.4F + 0.4F));
        float particleX = -MathHelper.sin((yaw + 23.0f) * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f);
        float particleY = -MathHelper.sin(pitch * 0.017453292f) + 1.6f;
        float particleZ = MathHelper.cos((yaw + 23.0f) * 0.017453292f) * MathHelper.cos(pitch * 0.017453292f);
        for (int i = 0; i < 3; ++i) {
            world.spawnParticle("smoke", x + particleX, y + particleY, z + particleZ, 0.0, 0.0,
                    0.0);
        }
        world.spawnParticle("flame", x + particleX, y + particleY, z + particleZ, 0.0, 0.0, 0.0);
    }

    @Override
    public float getMaxZoom() {
        return 0.15f;
    }
}
