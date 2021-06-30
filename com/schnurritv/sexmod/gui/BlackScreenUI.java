package com.schnurritv.sexmod.gui;

import net.minecraft.client.gui.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.*;
import com.schnurritv.sexmod.util.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class BlackScreenUI extends Gui
{
    private static boolean shouldBeRendered;
    private static double step;
    static ResourceLocation transitionScreen;
    static ResourceLocation mirroredTransitionScreen;
    static ResourceLocation blackScreen;
    
    public static void activate() {
        BlackScreenUI.shouldBeRendered = true;
    }
    
    @SubscribeEvent
    public void renderUI(final RenderGameOverlayEvent event) {
        if (BlackScreenUI.shouldBeRendered && event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            final Minecraft mc = Minecraft.getMinecraft();
            BlackScreenUI.step += mc.getTickLength() * 0.75f;
            final float xOffset = (float)Reference.Lerp(-900.0, 450.0, 0.5 * Math.cos(BlackScreenUI.step / 25.0) + 0.5);
            mc.renderEngine.bindTexture(BlackScreenUI.transitionScreen);
            this.drawTexturedModalRect(xOffset, 0.0f, 0, (int)(BlackScreenUI.step * 1.5), 256, 256);
            mc.renderEngine.bindTexture(BlackScreenUI.mirroredTransitionScreen);
            this.drawTexturedModalRect(xOffset + 600.0f, 0.0f, 0, (int)(BlackScreenUI.step * 1.5), 256, 256);
            mc.renderEngine.bindTexture(BlackScreenUI.blackScreen);
            this.drawTexturedModalRect(xOffset + 200.0f, 0.0f, 0, 0, 400, 256);
            if (BlackScreenUI.step > 30.0) {
                SexUI.shouldBeRendered = false;
            }
            if (BlackScreenUI.step > 69.0) {
                BlackScreenUI.step = 0.0;
                BlackScreenUI.shouldBeRendered = false;
            }
        }
    }
    
    static {
        BlackScreenUI.shouldBeRendered = false;
        BlackScreenUI.step = 0.0;
        BlackScreenUI.transitionScreen = new ResourceLocation("sexmod", "textures/gui/transitionscreen.png");
        BlackScreenUI.mirroredTransitionScreen = new ResourceLocation("sexmod", "textures/gui/mirroredtransitionscreen.png");
        BlackScreenUI.blackScreen = new ResourceLocation("sexmod", "textures/gui/blackscreen.png");
    }
}
