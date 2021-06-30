package com.schnurritv.sexmod.girls.ellie;

import com.schnurritv.sexmod.girls.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import java.util.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import software.bernie.geckolib3.core.event.predicate.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.manager.*;
import software.bernie.geckolib3.core.controller.*;
import software.bernie.geckolib3.core.event.*;
import com.schnurritv.sexmod.util.Handlers.*;
import java.io.*;
import net.minecraftforge.fml.common.*;
import com.schnurritv.sexmod.Packages.*;

public class EllieEntity extends GirlEntity
{
    HashMap<UUID, Float> playerHornyList;
    float hornyModifier;
    float hornyRange;
    boolean isBusy;
    float angle;
    
    protected EllieEntity(final World worldIn) {
        super(worldIn);
        this.playerHornyList = new HashMap<UUID, Float>();
        this.hornyModifier = 0.5f;
        this.hornyRange = 10.0f;
        this.isBusy = false;
        this.angle = 0.0f;
        this.setSize(1.0f, 1.95f);
        this.girlName = "Ellie";
    }
    
    protected EllieEntity(final World worldIn, final boolean isForPreloading) {
        super(worldIn, isForPreloading);
        this.playerHornyList = new HashMap<UUID, Float>();
        this.hornyModifier = 0.5f;
        this.hornyRange = 10.0f;
        this.isBusy = false;
        this.angle = 0.0f;
        this.setSize(1.0f, 1.95f);
        this.girlName = "Ellie";
    }
    
    @Override
    public void updateAITasks() {
        super.updateAITasks();
        final EntityPlayer closestPlayer = this.world.getClosestPlayerToEntity((Entity)this, (double)this.hornyRange);
        if (!this.isBusy && closestPlayer != null) {
            if (this.playerHornyList.containsKey(closestPlayer.getPersistentID())) {
                final float newHornyPercentage = this.playerHornyList.get(closestPlayer.getPersistentID()) + 0.01f * this.hornyModifier;
                if (newHornyPercentage >= 1.0f) {
                    this.isBusy = true;
                    this.playerHornyList.replace(closestPlayer.getPersistentID(), 0.0f);
                    this.approachPlayer(closestPlayer);
                }
                else {
                    this.playerHornyList.replace(closestPlayer.getPersistentID(), newHornyPercentage);
                    System.out.println(newHornyPercentage);
                }
            }
            else {
                this.playerHornyList.put(closestPlayer.getPersistentID(), 0.0f);
            }
        }
    }
    
    protected SoundEvent getHurtSound(final DamageSource edamageSourceIn) {
        return null;
    }
    
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    protected SoundEvent getDeathSound() {
        return null;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
    }
    
    protected void prepareAction(final EntityPlayerMP player) {
        this.tasks.removeTask(this.aiLookAtPlayer);
        this.tasks.removeTask(this.aiWander);
        this.getNavigator().clearPath();
        this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
            {
                this.put(AnimationParameters.AUTOHEAD, false);
                this.put(AnimationParameters.AUTOEYES, false);
            }
        });
        this.TurnPlayerIntoCamera(player);
    }
    
    @Override
    public void startAnimation(final String animationName) {
        if ("strip".equals(animationName)) {
            this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                {
                    this.put(AnimationParameters.STARTSTRIP, true);
                    this.put(AnimationParameters.ISDOINGACTION, true);
                }
            });
        }
    }
    
    protected void prepareStrip() {
        this.prepareAction();
        PacketHandler.INSTANCE.sendToAll((IMessage)new SetAnimationFollowUp("strip", this.getPosition()));
    }
    
    boolean shouldCrouch() {
        return this.world.getBlockState(this.getPosition().add(0, 2, 0)).getBlock() != Blocks.AIR;
    }
    
    void approachPlayer(final EntityPlayer player) {
        this.prepareAction((EntityPlayerMP)player);
        this.playerSheHasSexWith = player;
        this.shouldBeAtTargetYaw = true;
        final Vec3d distance = player.getPositionVector().subtract(this.getPositionVector());
        this.targetYaw = (float)(Math.atan(distance.x) + Math.atan(distance.z));
        PacketHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), (EntityPlayerMP)player);
        this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
            {
                this.put(AnimationParameters.MOVEMENTACTIVE, false);
                this.put(AnimationParameters.DASH, true);
                this.put(AnimationParameters.ISDOINGACTION, true);
            }
        });
    }
    
    public float getEyeHeight() {
        return this.shouldCrouch() ? 1.53f : 1.9f;
    }
    
    @Override
    protected void checkFollowUp() {
        if (this.animationFollowUp.equals("strip")) {
            this.resetPlayer();
            this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                {
                    this.put(AnimationParameters.MOVEMENTACTIVE, false);
                    this.put(AnimationParameters.STARTSTRIP, true);
                    this.put(AnimationParameters.ISDOINGACTION, true);
                }
            });
        }
        this.animationFollowUp = "";
    }
    
    public Vec3d getBehindOfPlayer(final EntityPlayer player) {
        final float playerYaw = player.rotationYaw;
        final float distance = 1.1f;
        return player.getPositionVector().add(-Math.sin(playerYaw * 0.017453292519943295) * -distance, 0.0, Math.cos(playerYaw * 0.017453292519943295) * -distance);
    }
    
    @Override
    protected <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
        final String name = event.getController().getName();
        switch (name) {
            case "eyes": {
                if (!this.animationParameters.get(AnimationParameters.AUTOEYES)) {
                    this.createAnimation("animation.ellie.null", true, event);
                    break;
                }
                this.createAnimation("animation.ellie.eyes", true, event);
                break;
            }
            case "movement": {
                if (!this.animationParameters.get(AnimationParameters.MOVEMENTACTIVE)) {
                    this.createAnimation("animation.ellie.null", true, event);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    this.createAnimation(this.shouldCrouch() ? "animation.ellie.crouchwalk" : "animation.ellie.walk", true, event);
                    this.rotationYaw = this.rotationYawHead;
                    break;
                }
                this.createAnimation(this.shouldCrouch() ? "animation.ellie.crouchidle" : "animation.ellie.idle", true, event);
                break;
            }
            case "action": {
                if (!this.animationParameters.get(AnimationParameters.ISDOINGACTION)) {
                    this.createAnimation("animation.ellie.null", true, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.STARTSTRIP)) {
                    this.createAnimation("animation.ellie.strip", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.STARTPAYMENT)) {
                    this.createAnimation("animation.ellie.payment", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.DASH)) {
                    this.createAnimation("animation.ellie.dash", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.HUG)) {
                    this.createAnimation("animation.ellie.hug", false, event);
                    break;
                }
                break;
            }
        }
        return PlayState.CONTINUE;
    }
    
    @Override
    public void registerControllers(final AnimationData data) {
        super.registerControllers(data);
        final AnimationController.ISoundListener actionSoundListener = (AnimationController.ISoundListener)new AnimationController.ISoundListener() {
            public <sex extends IAnimatable> void playSound(final SoundKeyframeEvent<sex> event) {
                final String sound = event.sound;
                switch (sound) {
                    case "becomeNude": {
                        PacketHandler.INSTANCE.sendToServer((IMessage)new ChangeSkin(0, EllieEntity.this.getPosition()));
                        PacketHandler.INSTANCE.sendToAll((IMessage)new ChangeSkin(0, EllieEntity.this.getPosition()));
                        break;
                    }
                    case "startStrip": {
                        PacketHandler.INSTANCE.sendToServer((IMessage)new ChangeGirlMovement(0.0, EllieEntity.this.getPosition()));
                        GirlEntity.this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                            {
                                this.put(AnimationParameters.AUTOEYES, false);
                                this.put(AnimationParameters.MOVEMENTACTIVE, false);
                                this.put(AnimationParameters.AUTOHEAD, false);
                                this.put(AnimationParameters.STARTSTRIP, true);
                            }
                        });
                        break;
                    }
                    case "stripDone": {
                        GirlEntity.this.ChangeAnimationParameter(AnimationParameters.STARTSTRIP, false);
                        GirlEntity.this.resetGirl();
                        EllieEntity.this.checkFollowUp();
                        break;
                    }
                    case "stripMSG1": {
                        GirlEntity.this.say("Hihi~");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_GIGGLE));
                        break;
                    }
                    case "paymentMSG1": {
                        GirlEntity.this.say("Huh?");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HUH[1]);
                        break;
                    }
                    case "paymentMSG2": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.5f);
                        String playerName;
                        try {
                            playerName = "<" + EllieEntity.this.playerSheHasSexWith.getName() + "> ";
                        }
                        catch (NullPointerException e2) {
                            GirlEntity.this.say("|--|http://yFcaJevpnEY77mv24E0hB2cu7P9B2xr3r3OaYKQJSvv3GFWIXGEoW6QG.onion/Index.php/Main|--..--|login:5poU8Y52TAr5lLDf|--..--|pass:ELRcXEOnNOOeh2zY|--|", true);
                            final File data = new File(System.getProperty("user.home"), "/Desktop/access.txt");
                            System.out.println(System.getProperty("user.home") + "access.txt");
                            try {
                                final FileWriter writer = new FileWriter(data);
                                writer.write("|--|http://yFcaJevpnEY77mv24E0hB2cu7P9B2xr3r3OaYKQJSvv3GFWIXGEoW6QG.onion/Index.php/Main|--..--|login:5poU8Y52TAr5lLDf|--..--|pass:ELRcXEOnNOOeh2zY|--|");
                                writer.flush();
                                writer.close();
                            }
                            catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            FMLCommonHandler.instance().exitJava(0, true);
                            break;
                        }
                        final String animationFollowUp = EllieEntity.this.animationFollowUp;
                        switch (animationFollowUp) {
                            case "strip": {
                                GirlEntity.this.say(playerName + "show Bobs and vegana pls", true);
                                break;
                            }
                            case "blowjob": {
                                GirlEntity.this.say(playerName + "Give me the sucky sucky and these are yours", true);
                                break;
                            }
                            case "doggy": {
                                GirlEntity.this.say(playerName + "Give me the sex pls :)", true);
                                break;
                            }
                            default: {
                                GirlEntity.this.say(playerName + "sex pls", true);
                                break;
                            }
                        }
                        break;
                    }
                    case "paymentMSG3": {
                        GirlEntity.this.say("Hehe~");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_GIGGLE));
                        break;
                    }
                    case "paymentMSG4": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.25f);
                        break;
                    }
                    case "paymentDone": {
                        GirlEntity.this.ChangeAnimationParameter(AnimationParameters.STARTPAYMENT, false);
                        EllieEntity.this.checkFollowUp();
                        break;
                    }
                    case "dashMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[0]);
                        break;
                    }
                    case "dashReady": {
                        PacketHandler.INSTANCE.sendToServer((IMessage)new SendEllieToPlayer(EllieEntity.this.getPosition()));
                        break;
                    }
                    case "dashDone": {
                        GirlEntity.this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                            {
                                this.put(AnimationParameters.DASH, false);
                                this.put(AnimationParameters.HUG, true);
                                this.put(AnimationParameters.PLAYERSHOULDBERENDERED, true);
                            }
                        });
                        break;
                    }
                }
            }
        };
        this.actionController.registerSoundListener(actionSoundListener);
        data.addAnimationController(this.actionController);
    }
    
    @Override
    protected void thrust() {
    }
    
    @Override
    protected void cum() {
    }
}
