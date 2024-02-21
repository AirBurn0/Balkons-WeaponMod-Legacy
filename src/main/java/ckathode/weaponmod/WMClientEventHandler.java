package ckathode.weaponmod;

import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import net.minecraftforge.fml.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import ckathode.weaponmod.entity.*;
import ckathode.weaponmod.network.*;
import net.minecraftforge.client.event.*;
import ckathode.weaponmod.item.*;

@SideOnly(Side.CLIENT)
public class WMClientEventHandler
{
    @SubscribeEvent
    public void onMouseClick(final MouseEvent e) {
        final EntityPlayer player = Minecraft.getMinecraft().player;
        if (!player.world.isRemote) {
            return;
        }
        if (player instanceof EntityPlayerSP && player != null && e.getButton() == 0 && e.isButtonstate()) {
            final ItemStack itemstack = player.getHeldItemMainhand();
            if (itemstack != null) {
                IExtendedReachItem ieri;
                if (itemstack.getItem() instanceof IExtendedReachItem) {
                    ieri = (IExtendedReachItem)itemstack.getItem();
                }
                else if (itemstack.getItem() instanceof IItemWeapon && ((IItemWeapon)itemstack.getItem()).getMeleeComponent() instanceof IExtendedReachItem) {
                    ieri = (IExtendedReachItem)((IItemWeapon)itemstack.getItem()).getMeleeComponent();
                }
                else {
                    ieri = null;
                }
                if (ieri != null) {
                    final float reach = ieri.getExtendedReach(player.world, player, itemstack);
                    final RayTraceResult raytraceResult = ExtendedReachHelper.getMouseOver(0.0f, reach);
                    if (raytraceResult != null && raytraceResult.entityHit != null && raytraceResult.entityHit != player && raytraceResult.entityHit.hurtResistantTime == 0) {
                        FMLClientHandler.instance().getClient().playerController.attackEntity(player, raytraceResult.entityHit);
                    }
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent e) {
        if (!e.player.world.isRemote) {
            return;
        }
        if (e.phase == TickEvent.Phase.START && e.player instanceof EntityPlayerSP) {
            final EntityPlayerSP entity = (EntityPlayerSP)e.player;
            if (entity.movementInput.jump && entity.getRidingEntity() instanceof EntityCannon && ((EntityCannon)entity.getRidingEntity()).isLoaded()) {
                final MsgCannonFire msg = new MsgCannonFire((EntityCannon)entity.getRidingEntity());
                BalkonsWeaponMod.instance.messagePipeline.sendToServer(msg);
            }
        }
    }
    
    @SubscribeEvent
    public void onFOVUpdateEvent(final FOVUpdateEvent e) {
        if (e.getEntity().isHandActive() && e.getEntity().getActiveItemStack().getItem() instanceof IItemWeapon) {
            final RangedComponent rc = ((IItemWeapon)e.getEntity().getActiveItemStack().getItem()).getRangedComponent();
            if (rc != null && RangedComponent.isReadyToFire(e.getEntity().getActiveItemStack())) {
                e.setNewfov(e.getFov() * rc.getFOVMultiplier(e.getEntity().getItemInUseMaxCount()));
            }
        }
    }
}
