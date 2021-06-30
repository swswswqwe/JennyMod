package com.schnurritv.sexmod.Packages;

import net.minecraft.util.math.*;
import io.netty.buffer.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;
import com.schnurritv.sexmod.girls.*;
import com.schnurritv.sexmod.util.*;
import net.minecraft.world.*;
import java.util.*;
import net.minecraft.entity.player.*;

public class ResetGirl implements IMessage
{
    boolean messageValid;
    BlockPos girlPos;
    boolean onlyDoPlayerPart;
    
    public ResetGirl() {
        this.messageValid = false;
    }
    
    public ResetGirl(final BlockPos girlPos) {
        this.girlPos = girlPos;
        this.onlyDoPlayerPart = false;
        this.messageValid = true;
    }
    
    public ResetGirl(final BlockPos girlPos, final boolean onlyDoPlayerPart) {
        this.girlPos = girlPos;
        this.onlyDoPlayerPart = onlyDoPlayerPart;
        this.messageValid = true;
    }
    
    public void fromBytes(final ByteBuf buf) {
        this.girlPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.onlyDoPlayerPart = buf.readBoolean();
        this.messageValid = true;
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeInt(this.girlPos.getX());
        buf.writeInt(this.girlPos.getY());
        buf.writeInt(this.girlPos.getZ());
        buf.writeBoolean(this.onlyDoPlayerPart);
        this.messageValid = true;
    }
    
    public static class Handler implements IMessageHandler<ResetGirl, IMessage>
    {
        public IMessage onMessage(final ResetGirl message, final MessageContext ctx) {
            if (message.messageValid && ctx.side == Side.SERVER) {
                final ArrayList<GirlEntity> girls = GirlEntity.getGirlsByPos(message.girlPos);
                for (final GirlEntity girl : girls) {
                    if (girl.world.isRemote) {
                        continue;
                    }
                    try {
                        final EntityPlayerMP player = Reference.server.getPlayerList().getPlayerByUUID(girl.playerSheHasSexWith.getPersistentID());
                        if (girl.playerGameMode != null) {
                            player.setGameType(girl.playerGameMode);
                        }
                        else {
                            player.setGameType(GameType.SURVIVAL);
                            System.out.println("couldn't find the players Gamemode, so i set it to SURVIVAL");
                        }
                    }
                    catch (NullPointerException ex) {}
                    if (message.onlyDoPlayerPart) {
                        continue;
                    }
                    girl.tasks.addTask(3, girl.aiWander);
                    girl.tasks.addTask(3, girl.aiLookAtPlayer);
                    girl.shouldBeAtTarget = false;
                    girl.playerIsInPosition = false;
                    girl.playerSheHasSexWith = null;
                    girl.playerIsCumming = false;
                    girl.playerIsThrusting = false;
                    girl.playerCamPos = null;
                    girl.setNoGravity(false);
                    girl.noClip = false;
                    girl.setPositionAndUpdate(girl.getPositionVector().x, (double)girl.getPosition().getY(), girl.getPositionVector().z);
                }
            }
            else {
                System.out.println("recieved an unvalid message @SendChatMessage :(");
            }
            return null;
        }
    }
}
