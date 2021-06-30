package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;
import com.schnurritv.sexmod.util.*;
import java.util.*;
import net.minecraft.entity.player.*;

public class TeleportPlayer implements IMessage
{
    boolean messageValid;
    String playerUUID;
    Vec3d pos;
    float yaw;
    float pitch;
    
    public TeleportPlayer() {
        this.messageValid = false;
    }
    
    public TeleportPlayer(final String playerUUID, final Vec3d pos) {
        this.playerUUID = playerUUID;
        this.pos = pos;
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.messageValid = true;
    }
    
    public TeleportPlayer(final String playerUUID, final Vec3d pos, final float yaw, final float pitch) {
        this.playerUUID = playerUUID;
        this.pos = pos;
        this.yaw = yaw;
        this.pitch = pitch;
        this.messageValid = true;
    }
    
    public TeleportPlayer(final String playerUUID, final double x, final double y, final double z, final float yaw, final float pitch) {
        this.playerUUID = playerUUID;
        this.pos = new Vec3d(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.playerUUID = ByteBufUtils.readUTF8String(buf);
        this.pos = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.yaw = buf.readFloat();
        this.pitch = buf.readFloat();
        this.messageValid = true;
    }
    
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.playerUUID);
        buf.writeDouble(this.pos.x);
        buf.writeDouble(this.pos.y);
        buf.writeDouble(this.pos.z);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.pitch);
        this.messageValid = true;
    }
    
    public static class Handler implements IMessageHandler<TeleportPlayer, IMessage>
    {
        public IMessage onMessage(final TeleportPlayer message, final MessageContext ctx) {
            if (message.messageValid && ctx.side == Side.SERVER) {
                try {
                    final EntityPlayerMP player = Reference.server.getPlayerList().getPlayerByUUID(UUID.fromString(message.playerUUID));
                    player.setPositionAndRotation(message.pos.x, message.pos.y, message.pos.z, message.yaw, message.pitch);
                    player.setPositionAndUpdate(message.pos.x, message.pos.y, message.pos.z);
                    player.setVelocity(0.0, 0.0, 0.0);
                }
                catch (NullPointerException e) {
                    System.out.println("couldn't find player with UUID: " + message.playerUUID);
                    System.out.println("could only find the following players:");
                    System.out.println(Reference.server.getPlayerList().getFormattedListOfPlayers(true));
                }
            }
            else {
                System.out.println("received an invalid message @TeleportPlayer :(");
            }
            return null;
        }
    }
}
