package com.schnurritv.sexmod.gui;

import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.*;
import org.lwjgl.opengl.*;
import com.schnurritv.sexmod.events.*;
import com.schnurritv.sexmod.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class SexUI extends Gui
{
    static ResourceLocation buttons;
    static ResourceLocation hornyMeter;
    public static boolean shouldBeRendered;
    static double cumPercentage;
    static double drawnCumPercentage;
    static float transitionStep;
    static float cumStep;
    static boolean keepSpacePressed;
    static float i;
    
    @SubscribeEvent
    public void renderUI(final RenderGameOverlayEvent event) {
        if (SexUI.shouldBeRendered && event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            final Minecraft minecraft = Minecraft.getMinecraft();
            if (SexUI.transitionStep < 1.0f) {
                SexUI.transitionStep += minecraft.getTickLength() / 25.0f;
            }
            else {
                SexUI.transitionStep = 1.0f;
            }
            GL11.glPushMatrix();
            minecraft.renderEngine.bindTexture(SexUI.buttons);
            GL11.glScalef(0.35f, 0.35f, 0.35f);
            if (SexUI.cumPercentage >= 1.0) {
                if (HandlePlayerMovement.isCumming) {
                    SexUI.keepSpacePressed = true;
                }
                final int yOffsetSpaceButton = SexUI.keepSpacePressed ? 54 : 0;
                this.drawTexturedModalRect(240, 160, 0, 108 + yOffsetSpaceButton, 256, 52);
            }
            if (!SexUI.keepSpacePressed) {
                final int yOffsetShiftButton = HandlePlayerMovement.isThrusting ? 54 : 0;
                this.drawTexturedModalRect((int)Reference.Lerp(-200.0, 98.0, SexUI.transitionStep), 405, 0, yOffsetShiftButton, 158, 54);
            }
            GL11.glScalef(2.857143f, 2.857143f, 2.857143f);
            minecraft.renderEngine.bindTexture(SexUI.hornyMeter);
            GL11.glScalef(0.75f, 0.75f, 0.75f);
            this.drawTexturedModalRect(10, (int)Reference.Lerp(-200.0, 10.0, SexUI.transitionStep), 0, 0, 146, 175);
            SexUI.drawnCumPercentage = Reference.Lerp(SexUI.drawnCumPercentage, SexUI.cumPercentage, minecraft.getTickLength());
            final int height = (int)Reference.Lerp(0.0, 160.0, SexUI.drawnCumPercentage);
            final int textureY = (int)Reference.Lerp(167.0, 8.0, SexUI.drawnCumPercentage);
            final double y = Reference.Lerp(178.0, 18.0, SexUI.drawnCumPercentage);
            if (!SexUI.keepSpacePressed) {
                this.drawTexturedModalRect(67, (int)Reference.Lerp(-45.0, y, SexUI.transitionStep), 159, textureY, 32, height);
                this.drawTexturedModalRect(120, (int)Reference.Lerp(-58.0, Reference.Lerp(178.0, 149.0, 1.0 - SexUI.drawnCumPercentage), SexUI.transitionStep), 212, (int)Reference.Lerp(169.0, 141.0, 1.0 - SexUI.drawnCumPercentage), 28, (int)Reference.Lerp(1.0, 29.0, 1.0 - SexUI.drawnCumPercentage));
                this.drawTexturedModalRect(18, (int)Reference.Lerp(-58.0, Reference.Lerp(178.0, 149.0, 1.0 - SexUI.drawnCumPercentage), SexUI.transitionStep), 212, (int)Reference.Lerp(169.0, 141.0, 1.0 - SexUI.drawnCumPercentage), 28, (int)Reference.Lerp(1.0, 29.0, 1.0 - SexUI.drawnCumPercentage));
            }
            else {
                SexUI.cumStep += minecraft.getTickLength() / 15.0f;
                this.drawTexturedModalRect(67, (int)Reference.Lerp(18.0, -300.0, SexUI.cumStep), 159, 8, 32, 160);
            }
            GL11.glPopMatrix();
        }
    }
    
    public static void addCumPercentage(final double amount) {
        SexUI.cumPercentage += amount;
        SexUI.cumPercentage = ((SexUI.cumPercentage > 1.0) ? 1.0 : SexUI.cumPercentage);
    }
    
    public static void resetCumPercentage() {
        SexUI.cumPercentage = 0.0;
        SexUI.keepSpacePressed = false;
    }
    
    static {
        SexUI.buttons = new ResourceLocation("sexmod", "textures/gui/buttons.png");
        SexUI.hornyMeter = new ResourceLocation("sexmod", "textures/gui/hornymeter.png");
        SexUI.shouldBeRendered = false;
        SexUI.cumPercentage = 0.0;
        SexUI.drawnCumPercentage = SexUI.cumPercentage;
        SexUI.transitionStep = 0.0f;
        SexUI.cumStep = 0.0f;
        SexUI.keepSpacePressed = false;
        SexUI.i = 0.0f;
    }
}
