package com.schnurritv.sexmod.util.Handlers;

import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.common.network.*;
import net.minecraftforge.fml.relauncher.*;
import com.schnurritv.sexmod.Packages.*;

public class PacketHandler
{
    public static SimpleNetworkWrapper INSTANCE;
    private static int ID;
    
    private static int nextID() {
        return PacketHandler.ID++;
    }
    
    public static void registerMessages() {
        (PacketHandler.INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("sexmodchannel")).registerMessage((Class)ChangeSkin.Handler.class, (Class)ChangeSkin.class, nextID(), Side.SERVER);
        PacketHandler.INSTANCE.registerMessage((Class)ChangeSkin.Handler.class, (Class)ChangeSkin.class, nextID(), Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage((Class)ChangeAnimationParameter.Handler.class, (Class)ChangeAnimationParameter.class, nextID(), Side.SERVER);
        PacketHandler.INSTANCE.registerMessage((Class)ChangeAnimationParameter.Handler.class, (Class)ChangeAnimationParameter.class, nextID(), Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage((Class)SendChatMessage.Handler.class, (Class)SendChatMessage.class, nextID(), Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage((Class)ChangeGirlMovement.Handler.class, (Class)ChangeGirlMovement.class, nextID(), Side.SERVER);
        PacketHandler.INSTANCE.registerMessage((Class)SetPlayerMovement.Handler.class, (Class)SetPlayerMovement.class, nextID(), Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage((Class)SetAnimationFollowUp.Handler.class, (Class)SetAnimationFollowUp.class, nextID(), Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage((Class)TeleportPlayer.Handler.class, (Class)TeleportPlayer.class, nextID(), Side.SERVER);
        PacketHandler.INSTANCE.registerMessage((Class)ChangeTransitionTicks.Handler.class, (Class)ChangeTransitionTicks.class, nextID(), Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage((Class)SendPaymentItems.Handler.class, (Class)SendPaymentItems.class, nextID(), Side.CLIENT);
        PacketHandler.INSTANCE.registerMessage((Class)ResetGirl.Handler.class, (Class)ResetGirl.class, nextID(), Side.SERVER);
        PacketHandler.INSTANCE.registerMessage((Class)SendJennyToDoggy.Handler.class, (Class)SendJennyToDoggy.class, nextID(), Side.SERVER);
        PacketHandler.INSTANCE.registerMessage((Class)SetPlayerForGirl.Handler.class, (Class)SetPlayerForGirl.class, nextID(), Side.SERVER);
        PacketHandler.INSTANCE.registerMessage((Class)SendEllieToPlayer.Handler.class, (Class)SendEllieToPlayer.class, nextID(), Side.SERVER);
    }
    
    static {
        PacketHandler.ID = 0;
    }
}
