package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import com.schnurritv.sexmod.girls.*;
import java.util.*;

public class SendPaymentItems implements IMessage
{
    boolean messageValid;
    String itemName;
    int amount;
    BlockPos girlPos;
    
    public SendPaymentItems() {
        this.messageValid = false;
    }
    
    public SendPaymentItems(final String itemName, final int amount, final BlockPos girlPos) {
        this.itemName = itemName;
        this.amount = amount;
        this.girlPos = girlPos;
        this.messageValid = true;
    }
    
    public SendPaymentItems(final int amount, final BlockPos girlPos) {
        this.amount = amount;
        this.itemName = "diamond";
        this.girlPos = girlPos;
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.itemName = ByteBufUtils.readUTF8String(buf);
        this.amount = buf.readInt();
        this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.messageValid = true;
    }
    
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.itemName);
        buf.writeInt(this.amount);
        buf.writeInt(this.girlPos.getX());
        buf.writeInt(this.girlPos.getY());
        buf.writeInt(this.girlPos.getZ());
        this.messageValid = true;
    }
    
    public static class Handler implements IMessageHandler<SendPaymentItems, IMessage>
    {
        public IMessage onMessage(final SendPaymentItems message, final MessageContext ctx) {
            if (message.messageValid) {
                final ArrayList<GirlEntity> girls = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girls) {
                    girl.paymentItem = GirlEntity.PaymentItems.valueOf(message.itemName);
                    girl.paymentItemsAmount = message.amount;
                }
            }
            else {
                System.out.println("received an invalid message @SendPaymentItems :(");
            }
            return null;
        }
    }
}
