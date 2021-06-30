package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import com.schnurritv.sexmod.girls.*;
import com.schnurritv.sexmod.girls.jenny.*;
import java.util.*;

public class SendJennyToDoggy implements IMessage
{
    boolean messageValid;
    BlockPos girlPos;
    
    public SendJennyToDoggy() {
        this.messageValid = false;
    }
    
    public SendJennyToDoggy(final BlockPos girlPos) {
        this.girlPos = girlPos;
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
    
    public static class Handler implements IMessageHandler<SendJennyToDoggy, IMessage>
    {
        public IMessage onMessage(final SendJennyToDoggy message, final MessageContext ctx) {
            if (message.messageValid) {
                final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girlList) {
                    if (!girl.world.isRemote && girl instanceof JennyEntity) {
                        ((JennyEntity)girl).goForDoggy();
                    }
                }
            }
            else {
                System.out.println("received an invalid message @SendGirlToSex :(");
            }
            return null;
        }
    }
}
