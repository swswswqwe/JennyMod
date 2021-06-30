package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import com.schnurritv.sexmod.girls.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import java.util.*;

public class ChangeSkin implements IMessage
{
    boolean messageValid;
    int skinIndex;
    BlockPos girlPos;
    
    public ChangeSkin() {
        this.messageValid = false;
    }
    
    public ChangeSkin(final int skinIndex, final BlockPos girlPos) {
        this.skinIndex = skinIndex;
        this.girlPos = girlPos;
        this.messageValid = true;
    }
    
    public ChangeSkin(final int skinIndex, final GirlEntity sophi) {
        this.skinIndex = skinIndex;
        this.girlPos = sophi.getPosition();
        this.messageValid = true;
    }
    
    public ChangeSkin(final int skinIndex, final int x, final int y, final int z) {
        this.skinIndex = skinIndex;
        this.girlPos = new BlockPos(x, y, z);
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        try {
            this.skinIndex = buf.readInt();
            this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
            this.messageValid = true;
        }
        catch (IndexOutOfBoundsException ioe) {
            System.out.println("couldn't read bytes @ChangeSkin :(");
        }
    }
    
    public void toBytes(final ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeInt(this.skinIndex);
        buf.writeInt(this.girlPos.getX());
        buf.writeInt(this.girlPos.getY());
        buf.writeInt(this.girlPos.getZ());
    }
    
    public static class Handler implements IMessageHandler<ChangeSkin, IMessage>
    {
        public IMessage onMessage(final ChangeSkin message, final MessageContext ctx) {
            if (message.messageValid) {
                final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girlList) {
                    girl.currentModel = message.skinIndex;
                }
            }
            else {
                System.out.println("recieved an unvalid message @ChangeSkin :(");
            }
            return null;
        }
    }
}
