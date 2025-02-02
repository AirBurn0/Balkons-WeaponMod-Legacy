package ckathode.weaponmod.network;

import ckathode.weaponmod.AdvancedExplosion;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;

public class MsgExplosion extends WMMessage {
    private double x;
    private double y;
    private double z;
    private float size;
    private List<ChunkPosition> blocks;
    private boolean smallParticles;
    private boolean bigParticles;

    public MsgExplosion() {
        // Needed for WMMessagePipeline instantiation
    }

    public MsgExplosion(AdvancedExplosion explosion, boolean smallparts, boolean bigparts) {
        x = explosion.explosionX;
        y = explosion.explosionY;
        z = explosion.explosionZ;
        size = explosion.explosionSize;
        blocks = explosion.affectedBlockPositions;
        smallParticles = smallparts;
        bigParticles = bigparts;
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        size = buf.readFloat();
        smallParticles = buf.readBoolean();
        bigParticles = buf.readBoolean();
        int size = buf.readInt();
        blocks = new ArrayList<>(size);
        for (int i = 0; i < size; ++i) {
            int ix = buf.readByte() + (int) x;
            int iy = buf.readByte() + (int) y;
            int iz = buf.readByte() + (int) z;
            blocks.add(new ChunkPosition(ix, iy, iz));
        }
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buf) {
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeFloat(size);
        buf.writeBoolean(smallParticles);
        buf.writeBoolean(bigParticles);
        buf.writeInt(blocks.size());
        for (ChunkPosition pos : blocks) {
            int dx = pos.chunkPosX - (int) x;
            int dy = pos.chunkPosY - (int) y;
            int dz = pos.chunkPosZ - (int) z;
            buf.writeByte(dx);
            buf.writeByte(dy);
            buf.writeByte(dz);
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClientSide(EntityPlayer player) {
        World world = Minecraft.getMinecraft().theWorld;
        AdvancedExplosion expl = new AdvancedExplosion(world, null, x, y, z, size,
                false, true);
        expl.setAffectedBlockPositions(blocks);
        expl.doParticleExplosion(smallParticles, bigParticles);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
    }
}
