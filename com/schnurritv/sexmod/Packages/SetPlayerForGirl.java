package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;
import com.schnurritv.sexmod.girls.*;
import com.schnurritv.sexmod.util.*;
import java.util.*;
import net.minecraft.server.management.*;
import net.minecraft.entity.player.*;

public class SetPlayerForGirl implements IMessage
{
    boolean messageValid;
    BlockPos girlPos;
    UUID playerUUID;
    
    public SetPlayerForGirl() {
        this.messageValid = false;
    }
    
    public SetPlayerForGirl(final BlockPos girlPos, final UUID playerUUID) {
        this.girlPos = girlPos;
        this.playerUUID = playerUUID;
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.playerUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.messageValid = true;
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.girlPos.getX());
        buf.writeInt(this.girlPos.getY());
        buf.writeInt(this.girlPos.getZ());
        ByteBufUtils.writeUTF8String(buf, this.playerUUID.toString());
    }
    
    public static class Handler implements IMessageHandler<SetPlayerForGirl, IMessage>
    {
        public IMessage onMessage(final SetPlayerForGirl message, final MessageContext ctx) {
            if (message.messageValid && ctx.side == Side.SERVER) {
                final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girlList) {
                    final PlayerList playerList = Reference.server.getPlayerList();
                    try {
                        playerList.getPlayerByUUID(message.playerUUID).getName();
                    }
                    catch (NullPointerException e) {
                        System.out.println("couldn't find player with UUID: " + message.playerUUID);
                        System.out.println("could only find players with thsese UUID's:");
                        for (final EntityPlayerMP player : playerList.getPlayers()) {
                            System.out.println(player.getName() + " " + player.getUniqueID());
                        }
                        continue;
                    }
                    girl.playerSheHasSexWith = (EntityPlayer)Reference.server.getPlayerList().getPlayerByUUID(message.playerUUID);
                }
            }
            else {
                System.out.println("received an invalid message @SetPlayerForGirl :(");
            }
            return null;
        }
    }
}
