package com.schnurritv.sexmod.Packages;

import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;
import net.minecraft.client.*;
import net.minecraft.util.text.*;

public class SendChatMessage implements IMessage
{
    boolean messageValid;
    String chatMessage;
    
    public SendChatMessage(final String chatMessage) {
        this.chatMessage = chatMessage;
        this.messageValid = true;
    }
    
    public SendChatMessage() {
        this.messageValid = false;
    }
    
    public void fromBytes(final ByteBuf buf) {
        try {
            final int stringLength = buf.readInt();
            final byte[] bytes = new byte[stringLength];
            for (int i = 0; i < stringLength; ++i) {
                bytes[i] = buf.readByte();
            }
            this.chatMessage = new String(bytes);
            this.messageValid = true;
            this.messageValid = true;
        }
        catch (IndexOutOfBoundsException ioe) {
            this.messageValid = false;
            System.out.println("couldn't read bytes @SendChatMessage :(");
        }
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.chatMessage.getBytes().length);
        buf.writeBytes(this.chatMessage.getBytes());
    }
    
    public static class Handler implements IMessageHandler<SendChatMessage, IMessage>
    {
        public IMessage onMessage(final SendChatMessage message, final MessageContext ctx) {
            if (message.messageValid && ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().player.sendMessage((ITextComponent)new TextComponentString(message.chatMessage));
            }
            else {
                System.out.println("recieved an unvalid message @SendChatMessage :(");
            }
            return null;
        }
    }
}
