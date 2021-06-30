package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import com.schnurritv.sexmod.girls.*;
import com.schnurritv.sexmod.girls.ellie.*;
import java.util.*;

public class SendEllieToPlayer implements IMessage
{
    boolean messageValid;
    BlockPos girlPos;
    
    public SendEllieToPlayer() {
        this.messageValid = false;
    }
    
    public SendEllieToPlayer(final BlockPos girlPos) {
        this.girlPos = girlPos;
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.messageValid = true;
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.girlPos.getX());
        buf.writeInt(this.girlPos.getY());
        buf.writeInt(this.girlPos.getZ());
    }
    
    public static class Handler implements IMessageHandler<SendEllieToPlayer, IMessage>
    {
        public IMessage onMessage(final SendEllieToPlayer message, final MessageContext ctx) {
            if (message.messageValid) {
                final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girlList) {
                    if (!girl.world.isRemote && girl instanceof EllieEntity) {
                        girl.targetPos = ((EllieEntity)girl).getBehindOfPlayer(girl.playerSheHasSexWith);
                        girl.targetYaw = girl.playerSheHasSexWith.rotationYaw;
                        girl.shouldBeAtTarget = true;
                    }
                }
            }
            else {
                System.out.println("received an invalid message @SendEllieToPlayer :(");
            }
            return null;
        }
    }
}
