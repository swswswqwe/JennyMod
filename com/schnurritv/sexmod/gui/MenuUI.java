package com.schnurritv.sexmod.gui;

import net.minecraft.client.gui.inventory.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import com.schnurritv.sexmod.girls.*;
import net.minecraft.inventory.*;
import com.schnurritv.sexmod.util.*;
import net.minecraft.client.gui.*;
import com.schnurritv.sexmod.events.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.item.*;

public class MenuUI extends GuiContainer
{
    private static final ResourceLocation SOPHI_HEAD;
    private final InventoryPlayer player;
    public static GirlEntity entityGirl;
    private GirlEntity girl;
    static float buttonTransitionStep;
    static float priceTransitionStep;
    
    public MenuUI(final InventoryPlayer player, final int x, final int y, final int z) {
        super((Container)new ContainerUI());
        this.girl = MenuUI.entityGirl;
        this.player = player;
    }
    
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.buttonList.clear();
        final ScaledResolution resolution = new ScaledResolution(this.mc);
        final int screenWidth = resolution.getScaledWidth();
        final int screenHeight = resolution.getScaledHeight();
        if (MenuUI.buttonTransitionStep < 1.0f) {
            MenuUI.buttonTransitionStep += this.mc.getTickLength() / 5.0f;
        }
        else {
            MenuUI.buttonTransitionStep = 1.0f;
        }
        final int x = (int)Reference.Lerp(-30.0, 120.0, MenuUI.buttonTransitionStep);
        this.buttonList.add(new GuiButton(1, screenWidth - x, screenHeight - 150, 100, 20, "Blowjob"));
        this.buttonList.add(new GuiButton(2, screenWidth - x, screenHeight - 120, 100, 20, "Sex"));
        this.buttonList.add(new GuiButton(3, screenWidth - x, screenHeight - 90, 100, 20, "Strip"));
    }
    
    public void onGuiClosed() {
        MenuUI.buttonTransitionStep = 0.0f;
        MenuUI.priceTransitionStep = 0.0f;
        super.onGuiClosed();
    }
    
    protected void actionPerformed(final GuiButton button) {
        final ArrayList<GirlEntity> girlList = GirlEntity.getGirlsByPos(this.girl.getPosition());
        for (final GirlEntity girl : girlList) {
            girl.playerSheHasSexWith = this.player.player;
        }
        this.player.player.closeScreen();
        switch (button.id) {
            case 1: {
                HandlePlayerMovement.active = false;
                this.girl.startAnimation("blowjob");
                break;
            }
            case 2: {
                HandlePlayerMovement.active = false;
                this.girl.startAnimation("doggy");
                break;
            }
            case 3: {
                HandlePlayerMovement.active = false;
                this.girl.startAnimation("strip");
                break;
            }
        }
    }
    
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        final ScaledResolution resolution = new ScaledResolution(this.mc);
        final int screenWidth = resolution.getScaledWidth();
        final int screenHeight = resolution.getScaledHeight();
        if (MenuUI.priceTransitionStep < 1.0f) {
            MenuUI.priceTransitionStep += this.mc.getTickLength() / 5.0f;
            return;
        }
        if (MenuUI.priceTransitionStep < 2.0f) {
            MenuUI.priceTransitionStep += this.mc.getTickLength() / 5.0f;
        }
        else {
            MenuUI.priceTransitionStep = 2.0f;
        }
        final int xText = (int)Reference.Lerp(120.0, 161.0, MenuUI.priceTransitionStep - 1.0f);
        final int xItem = (int)Reference.Lerp(96.0, 137.0, MenuUI.priceTransitionStep - 1.0f);
        this.drawHoveringText("3x    ", screenWidth - xText, screenHeight - 132);
        this.itemRender.renderItemIntoGUI(new ItemStack(Items.EMERALD, 1), screenWidth - xItem, screenHeight - 148);
        this.drawHoveringText("2x    ", screenWidth - xText, screenHeight - 102);
        this.itemRender.renderItemIntoGUI(new ItemStack(Items.DIAMOND, 1), screenWidth - xItem, screenHeight - 118);
        this.drawHoveringText("1x    ", screenWidth - xText, screenHeight - 72);
        this.itemRender.renderItemIntoGUI(new ItemStack(Items.GOLD_INGOT, 1), screenWidth - xItem, screenHeight - 88);
    }
    
    static {
        SOPHI_HEAD = new ResourceLocation("sexmod:textures/gui/sophihead.png");
        MenuUI.buttonTransitionStep = 0.0f;
        MenuUI.priceTransitionStep = 0.0f;
    }
}
