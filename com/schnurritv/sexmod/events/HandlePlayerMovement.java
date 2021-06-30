package com.schnurritv.sexmod.events;

import net.minecraft.client.*;
import com.schnurritv.sexmod.girls.*;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;

public class HandlePlayerMovement
{
    public static boolean active;
    public static boolean isThrusting;
    public static boolean isCumming;
    
    @SubscribeEvent
    public void PreventPlayerFromMoving(final InputUpdateEvent event) {
        final MovementInput movement = event.getMovementInput();
        HandlePlayerMovement.isThrusting = movement.sneak;
        if (!HandlePlayerMovement.active) {
            if (movement.sneak) {
                GirlEntity.sendThrust(Minecraft.getMinecraft().player.getPersistentID());
            }
            if (movement.jump) {
                GirlEntity.sendCum(Minecraft.getMinecraft().player.getPersistentID());
            }
            HandlePlayerMovement.isThrusting = movement.sneak;
            HandlePlayerMovement.isCumming = movement.jump;
            movement.backKeyDown = false;
            movement.forwardKeyDown = false;
            movement.leftKeyDown = false;
            movement.rightKeyDown = false;
            movement.sneak = false;
            movement.jump = false;
            movement.moveForward = 0.0f;
            movement.moveStrafe = 0.0f;
        }
    }
    
    @SubscribeEvent
    public void PreventPlayerFromTakingAction(final MouseEvent event) {
        event.setCanceled(!HandlePlayerMovement.active);
    }
    
    static {
        HandlePlayerMovement.active = true;
        HandlePlayerMovement.isThrusting = false;
        HandlePlayerMovement.isCumming = false;
    }
}
