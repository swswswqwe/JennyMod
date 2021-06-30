package com.schnurritv.sexmod.Packages;

import com.schnurritv.sexmod.girls.*;
import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import java.util.*;

public class ChangeAnimationParameter implements IMessage
{
    boolean messageValid;
    EnumMap<GirlEntity.AnimationParameters, Boolean> parameters;
    BlockPos entityPos;
    
    public ChangeAnimationParameter() {
        this.parameters = new EnumMap<GirlEntity.AnimationParameters, Boolean>(GirlEntity.AnimationParameters.class);
        this.messageValid = false;
    }
    
    public ChangeAnimationParameter(final BlockPos entityPos, final GirlEntity.AnimationParameters parameterName, final boolean parameterValue) {
        (this.parameters = new EnumMap<GirlEntity.AnimationParameters, Boolean>(GirlEntity.AnimationParameters.class)).put(parameterName, parameterValue);
        this.entityPos = entityPos;
        this.messageValid = true;
    }
    
    public ChangeAnimationParameter(final BlockPos entityPos, final EnumMap<GirlEntity.AnimationParameters, Boolean> parameters) {
        this.parameters = new EnumMap<GirlEntity.AnimationParameters, Boolean>(GirlEntity.AnimationParameters.class);
        this.parameters = parameters;
        this.entityPos = entityPos;
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.entityPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        for (int amount = buf.readInt(), i = 0; i < amount; ++i) {
            final String key = ByteBufUtils.readUTF8String(buf);
            final boolean value = buf.readBoolean();
            this.parameters.put(GirlEntity.AnimationParameters.valueOf(key), value);
        }
        this.messageValid = true;
    }
    
    public void toBytes(final ByteBuf buf) {
        if (!this.messageValid) {
            return;
        }
        buf.writeInt(this.entityPos.getX());
        buf.writeInt(this.entityPos.getY());
        buf.writeInt(this.entityPos.getZ());
        buf.writeInt(this.parameters.size());
        for (final Map.Entry<GirlEntity.AnimationParameters, Boolean> parameter : this.parameters.entrySet()) {
            ByteBufUtils.writeUTF8String(buf, parameter.getKey().name());
            buf.writeBoolean((boolean)parameter.getValue());
        }
    }
    
    public static class Handler implements IMessageHandler<ChangeAnimationParameter, IMessage>
    {
        public IMessage onMessage(final ChangeAnimationParameter message, final MessageContext ctx) {
            if (message.messageValid) {
                if (!GirlEntity.getGirlsByPos(message.entityPos).isEmpty()) {
                    final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(message.entityPos);
                    for (final GirlEntity girl : girlList) {
                        for (final Map.Entry<GirlEntity.AnimationParameters, Boolean> parameter : message.parameters.entrySet()) {
                            girl.animationParameters.replace(parameter.getKey(), parameter.getValue());
                        }
                    }
                }
            }
            else {
                System.out.println("recieved an unvalid message @ChangeAnimationParameter :(");
            }
            return null;
        }
    }
}
