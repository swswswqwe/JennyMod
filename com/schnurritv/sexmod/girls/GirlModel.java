package com.schnurritv.sexmod.girls;

import software.bernie.geckolib3.model.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import javax.imageio.*;
import java.io.*;
import java.net.*;
import net.minecraft.util.text.*;
import net.minecraft.client.renderer.texture.*;
import java.awt.image.*;
import net.minecraft.client.entity.*;
import java.awt.*;
import software.bernie.geckolib3.core.event.predicate.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.model.provider.data.*;
import software.bernie.geckolib3.core.processor.*;

public abstract class GirlModel<T extends GirlEntity> extends AnimatedGeoModel<T>
{
    boolean madeSkin;
    public ResourceLocation skin;
    static final int XOFFSET = 192;
    
    public GirlModel() {
        this.madeSkin = false;
    }
    
    public abstract ResourceLocation getModel(final int p0);
    
    public abstract ResourceLocation getSkin();
    
    public abstract ResourceLocation getAnimationFile();
    
    public abstract String getSkinName();
    
    public ResourceLocation getAnimationFileLocation(final GirlEntity animatable) {
        return this.getAnimationFile();
    }
    
    public ResourceLocation getModelLocation(final GirlEntity girl) {
        return this.getModel(girl.currentModel);
    }
    
    public ResourceLocation getTextureLocation(final GirlEntity girl) {
        if (!this.madeSkin) {
            BufferedImage playerSkin = null;
            BufferedImage defaultSkin = null;
            final EntityPlayerSP player = Minecraft.getMinecraft().player;
            boolean foundPlayerSkin = false;
            try {
                defaultSkin = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(this.getSkin()).getInputStream());
            }
            catch (IOException e) {
                System.out.println("Couldn't load the default skin... which is kinda odd... Did you change Stuff inside the .jar file?");
                e.printStackTrace();
            }
            final File manualPlayerSkin = new File("sexmod/skin.png");
            try {
                playerSkin = ImageIO.read(manualPlayerSkin);
                foundPlayerSkin = true;
            }
            catch (IOException e4) {
                System.out.println("couldn't read a skin in sexmod/skin.png, which is why i am constructing a new one");
            }
            if (!foundPlayerSkin) {
                try {
                    final URL skinUrl = new URL("https://crafatar.com/skins/" + player.getPersistentID());
                    playerSkin = ImageIO.read(skinUrl);
                    foundPlayerSkin = true;
                }
                catch (IOException e4) {
                    player.sendMessage((ITextComponent)new TextComponentString("§dI'm s-sorry but... crafatar.com cannot grab your Skin §5UwU §d.. This could either be because your playing on a pirated version of Minecraft or crafatar.com is currently on stupido mode. If you want to fix that yourself: put your Minecraft Skin into %appdata%/.minecraft/sexmod as skin.png and restart Minecraft"));
                }
            }
            if (!foundPlayerSkin) {
                final String skinType = player.getSkinType().equals("slim") ? "alex" : "steve";
                try {
                    playerSkin = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("minecraft", "textures/entity/" + skinType + ".png")).getInputStream());
                }
                catch (IOException e2) {
                    System.out.println("couldn't load default Steve/alex... tbh have no fucking clue how this is even possible... maybe your mc version is fucked up?");
                    e2.printStackTrace();
                }
            }
            final Rectangle skinTexture = playerSkin.getData().getBounds();
            for (int x = 0; x < skinTexture.width; ++x) {
                for (int y = 0; y < skinTexture.height; ++y) {
                    for (int extraX = 0; extraX < 3; ++extraX) {
                        for (int extraY = 0; extraY < 3; ++extraY) {
                            defaultSkin.setRGB(192 + x * 3 + extraX, y * 3 + extraY, playerSkin.getRGB(x, y));
                        }
                    }
                }
            }
            final File dir = new File("sexmod");
            dir.mkdir();
            final File skin = new File("sexmod/" + this.getSkinName() + ".png");
            try {
                ImageIO.write(defaultSkin, "png", skin);
            }
            catch (IOException e3) {
                System.out.println("couldn't save file");
                e3.printStackTrace();
            }
            try {
                this.skin = Minecraft.getMinecraft().getRenderManager().renderEngine.getDynamicTextureLocation(skin.getName(), new DynamicTexture(ImageIO.read(skin)));
            }
            catch (IOException e3) {
                System.out.println("couldn't load skin");
                e3.printStackTrace();
            }
            this.madeSkin = true;
        }
        return this.skin;
    }
    
    public void setLivingAnimations(final T girl, final Integer uniqueID, final AnimationEvent customPredicate) {
        super.setLivingAnimations((IAnimatable)girl, uniqueID, customPredicate);
        if (girl.shouldBeAtTarget) {
            girl.setPositionAndRotationDirect(girl.targetPos.x, girl.targetPos.y, girl.targetPos.z, girl.targetYaw, 0.0f, 1, true);
        }
        final AnimationProcessor processor = this.getAnimationProcessor();
        try {
            processor.getBone("steve").setHidden(!girl.animationParameters.get(GirlEntity.AnimationParameters.PLAYERSHOULDBERENDERED));
        }
        catch (NullPointerException ex) {}
        if (girl.animationParameters.get(GirlEntity.AnimationParameters.AUTOHEAD)) {
            final EntityModelData extraData = customPredicate.getExtraDataOfType((Class)EntityModelData.class).get(0);
            final IBone neck = processor.getBone("neck");
            neck.setRotationY(extraData.netHeadYaw * 0.5f * 0.017453292f);
            final IBone head = processor.getBone("head");
            head.setRotationY(extraData.netHeadYaw * 0.017453292f);
            head.setRotationX(extraData.headPitch * 0.017453292f);
            final IBone body = processor.getBone("body");
            body.setRotationY(0.0f);
        }
        try {
            processor.getBone("items").getName();
        }
        catch (NullPointerException e) {
            return;
        }
        if (girl.paymentItemsAmount == 0) {
            for (final GirlEntity.PaymentItems item : GirlEntity.PaymentItems.values()) {
                for (int i = 1; i <= 3; ++i) {
                    processor.getBone(item.name().toLowerCase() + i).setHidden(true);
                }
            }
        }
        else {
            for (int j = 1; j <= girl.paymentItemsAmount; ++j) {
                processor.getBone(girl.paymentItem.name().toLowerCase() + j).setHidden(false);
            }
        }
    }
}
