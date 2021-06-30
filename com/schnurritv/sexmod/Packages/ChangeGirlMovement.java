package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import com.schnurritv.sexmod.girls.*;
import net.minecraft.entity.*;
import java.util.*;

public class ChangeGirlMovement implements IMessage
{
    boolean messageValid;
    double speed;
    BlockPos girlPos;
    
    public ChangeGirlMovement() {
        this.messageValid = false;
    }
    
    public ChangeGirlMovement(final double speed, final int x, final int y, final int z) {
        this.speed = speed;
        this.girlPos = new BlockPos(x, y, z);
        this.messageValid = true;
    }
    
    public ChangeGirlMovement(final double speed, final BlockPos girlPos) {
        this.girlPos = girlPos;
        this.speed = speed;
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        try {
            this.speed = buf.readDouble();
            this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            this.messageValid = true;
        }
        catch (IndexOutOfBoundsException ioe) {
            System.out.println("couldn't read bytes @ChangeGirlMovement :(");
        }
    }
    
    public void toBytes(final ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeDouble(this.speed);
        buf.writeInt(this.girlPos.getX());
        buf.writeInt(this.girlPos.getY());
        buf.writeInt(this.girlPos.getZ());
    }
    
    public static class Handler implements IMessageHandler<ChangeGirlMovement, IMessage>
    {
        public IMessage onMessage(final ChangeGirlMovement message, final MessageContext ctx) {
            if (message.messageValid) {
                final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girlList) {
                    girl.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(message.speed);
                }
            }
            else {
                System.out.println("recieved an unvalid message @ChangeGirlMovement :(");
            }
            return null;
        }
    }
}
