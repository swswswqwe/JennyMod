package com.schnurritv.sexmod.Packages;

import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;
import com.schnurritv.sexmod.events.*;
import net.minecraft.client.*;

public class SetPlayerMovement implements IMessage
{
    boolean messageValid;
    boolean setActive;
    
    public SetPlayerMovement(final boolean setActive) {
        this.setActive = setActive;
        this.messageValid = true;
    }
    
    public SetPlayerMovement() {
        this.messageValid = false;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.setActive = buf.readBoolean();
        this.messageValid = true;
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeBoolean(this.setActive);
        this.messageValid = true;
    }
    
    public static class Handler implements IMessageHandler<SetPlayerMovement, IMessage>
    {
        public IMessage onMessage(final SetPlayerMovement message, final MessageContext ctx) {
            if (message.messageValid && ctx.side == Side.CLIENT) {
                HandlePlayerMovement.active = message.setActive;
                Minecraft.getMinecraft().player.setVelocity(0.0, 0.0, 0.0);
            }
            else {
                System.out.println("received an invalid message @SetPlayerMovement :(");
            }
            return null;
        }
    }
}
