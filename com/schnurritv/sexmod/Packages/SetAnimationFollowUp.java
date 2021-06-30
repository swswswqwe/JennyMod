package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import com.schnurritv.sexmod.girls.*;
import java.util.*;

public class SetAnimationFollowUp implements IMessage
{
    boolean messageValid;
    String followUp;
    BlockPos entityPos;
    
    public SetAnimationFollowUp() {
        this.messageValid = false;
    }
    
    public SetAnimationFollowUp(final String followUp, final BlockPos entityPos) {
        this.followUp = followUp;
        this.entityPos = entityPos;
    }
    
    public void fromBytes(final ByteBuf buf) {
        try {
            final int stringLength = buf.readInt();
            final byte[] bytes = new byte[stringLength];
            for (int i = 0; i < stringLength; ++i) {
                bytes[i] = buf.readByte();
            }
            this.followUp = new String(bytes);
            this.entityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            this.messageValid = true;
            this.messageValid = true;
        }
        catch (IndexOutOfBoundsException ioe) {
            this.messageValid = false;
            System.out.println("couldn't read bytes @SetPaymentFollowUp :(");
        }
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.followUp.getBytes().length);
        buf.writeBytes(this.followUp.getBytes());
        buf.writeInt(this.entityPos.getX());
        buf.writeInt(this.entityPos.getY());
        buf.writeInt(this.entityPos.getZ());
        this.messageValid = true;
    }
    
    public static class Handler implements IMessageHandler<SetAnimationFollowUp, IMessage>
    {
        public IMessage onMessage(final SetAnimationFollowUp message, final MessageContext ctx) {
            if (message.messageValid) {
                if (!GirlEntity.getGirlsByPos(message.entityPos).isEmpty()) {
                    final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.entityPos);
                    for (final GirlEntity girl : girlList) {
                        girl.animationFollowUp = message.followUp;
                    }
                }
            }
            else {
                System.out.println("received an invalid message @ChangeAnimationParameter :(");
            }
            return null;
        }
    }
}
