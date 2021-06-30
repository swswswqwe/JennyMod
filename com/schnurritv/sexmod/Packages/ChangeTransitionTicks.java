package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import com.schnurritv.sexmod.girls.*;
import java.util.*;

public class ChangeTransitionTicks implements IMessage
{
    boolean messageValid;
    int newValue;
    BlockPos girlPos;
    
    public ChangeTransitionTicks() {
        this.messageValid = false;
    }
    
    public ChangeTransitionTicks(final int newValue, final BlockPos girlPos) {
        this.newValue = newValue;
        this.girlPos = girlPos;
        this.messageValid = true;
    }
    
    public ChangeTransitionTicks(final int newValue, final int x, final int y, final int z) {
        this.newValue = newValue;
        this.girlPos = new BlockPos(x, y, z);
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        try {
            this.newValue = buf.readInt();
            this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            this.messageValid = true;
        }
        catch (IndexOutOfBoundsException ioe) {
            System.out.println("couldn't read bytes @ChangeTransitionTicks :(");
        }
    }
    
    public void toBytes(final ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeInt(this.newValue);
        buf.writeInt(this.girlPos.getX());
        buf.writeInt(this.girlPos.getY());
        buf.writeInt(this.girlPos.getZ());
    }
    
    public static class Handler implements IMessageHandler<ChangeTransitionTicks, IMessage>
    {
        public IMessage onMessage(final ChangeTransitionTicks message, final MessageContext ctx) {
            if (message.messageValid) {
                final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girlList) {
                    girl.actionController.transitionLengthTicks = message.newValue;
                }
            }
            else {
                System.out.println("received an invalid message @ChangeTransitionTicks :(");
            }
            return null;
        }
    }
}
