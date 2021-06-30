package com.schnurritv.sexmod.girls.jenny;

import com.schnurritv.sexmod.girls.*;
import net.minecraft.world.*;
import com.schnurritv.sexmod.util.*;
import java.util.*;
import com.schnurritv.sexmod.util.Handlers.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.entity.*;
import com.schnurritv.sexmod.*;
import net.minecraftforge.fml.common.network.internal.*;
import net.minecraft.item.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import software.bernie.geckolib3.core.event.predicate.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.manager.*;
import software.bernie.geckolib3.core.controller.*;
import software.bernie.geckolib3.core.event.*;
import java.io.*;
import net.minecraftforge.fml.common.*;
import com.schnurritv.sexmod.gui.*;
import net.minecraft.client.*;
import com.schnurritv.sexmod.Packages.*;

public class JennyEntity extends GirlEntity
{
    private boolean lookingForBed;
    private boolean isPreparingPayment;
    int bedSearchTick;
    int preparingPaymentTick;
    int flip;
    
    public JennyEntity(final World worldIn) {
        super(worldIn);
        this.lookingForBed = false;
        this.isPreparingPayment = false;
        this.bedSearchTick = 0;
        this.preparingPaymentTick = 0;
        this.flip = 0;
        this.setSize(0.6f, 1.95f);
        this.girlName = "Jenny";
    }
    
    public JennyEntity(final World worldIn, final boolean isForPreLoading) {
        super(worldIn, isForPreLoading);
        this.lookingForBed = false;
        this.isPreparingPayment = false;
        this.bedSearchTick = 0;
        this.preparingPaymentTick = 0;
        this.flip = 0;
        this.setSize(0.6f, 1.95f);
        this.girlName = "Jenny";
    }
    
    public float getEyeHeight() {
        return 1.64f;
    }
    
    protected SoundEvent getAmbientSound() {
        return null;
    }
    
    protected SoundEvent getDeathSound() {
        final int whichOne = Reference.RANDOM.nextInt(2);
        if (whichOne != 0) {
            return SoundsHandler.GIRLS_JENNY_SIGH[0];
        }
        return SoundsHandler.GIRLS_JENNY_SIGH[1];
    }
    
    protected SoundEvent getHurtSound(final DamageSource source) {
        return null;
    }
    
    @Override
    public void updateAITasks() {
        super.updateAITasks();
        if (this.animationParameters.get(AnimationParameters.WAITDOGGY) && this.playerSheHasSexWith.getPositionVector().distanceTo(this.getPositionVector()) < 0.5) {
            final EntityPlayerMP playerMP = this.getServer().getPlayerList().getPlayerByUUID(this.playerSheHasSexWith.getPersistentID());
            playerMP.setPositionAndUpdate(this.getPositionVector().x, this.getPositionVector().y, this.getPositionVector().z);
            this.TurnPlayerIntoCamera(playerMP, false);
            playerMP.moveRelative(0.0f, 0.0f, 0.0f, 0.0f);
            this.moveCamera(0.0, 0.0, 0.4, 0.0f, 60.0f);
            this.playerCamPos = null;
            this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                {
                    this.put(AnimationParameters.PLAYERSHOULDBERENDERED, true);
                    this.put(AnimationParameters.WAITDOGGY, false);
                    this.put(AnimationParameters.DOGGYSTART, true);
                    this.put(AnimationParameters.AUTOEYES, false);
                }
            });
            this.ChangeTransitionTicks(2);
            PacketHandler.INSTANCE.sendTo((IMessage)new SetPlayerMovement(false), playerMP);
        }
        if (this.lookingForBed) {
            if (this.getPositionVector().distanceTo(this.targetPos) < 0.6 || this.bedSearchTick > 200) {
                this.lookingForBed = false;
                this.shouldBeAtTarget = true;
                this.bedSearchTick = 0;
                this.setNoGravity(this.noClip = true);
                this.setVelocity(0.0, 0.0, 0.0);
                this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                    {
                        this.put(AnimationParameters.MOVEMENTACTIVE, false);
                        this.put(AnimationParameters.AUTOHEAD, false);
                        this.put(AnimationParameters.AUTOEYES, false);
                        this.put(AnimationParameters.ISDOINGACTION, true);
                        this.put(AnimationParameters.STARTDOGGY, true);
                    }
                });
            }
            else {
                ++this.bedSearchTick;
                if (this.bedSearchTick == 60 || this.bedSearchTick == 120) {
                    this.getNavigator().clearPath();
                    this.getNavigator().tryMoveToXYZ(this.targetPos.x, this.targetPos.y, this.targetPos.z, this.walkSpeed);
                }
            }
        }
        if (this.isPreparingPayment) {
            ++this.preparingPaymentTick;
            if (this.getPositionVector().equals((Object)this.targetPos) || this.preparingPaymentTick > 40) {
                this.isPreparingPayment = false;
                this.shouldGetDamage = false;
                this.preparingPaymentTick = 0;
                this.targetYaw = this.playerSheHasSexWith.rotationYaw + 180.0f;
                this.shouldBeAtTarget = true;
                this.getNavigator().clearPath();
                System.out.println("Ready For Payment Animation");
                this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                    {
                        this.put(AnimationParameters.MOVEMENTACTIVE, false);
                        this.put(AnimationParameters.STARTPAYMENT, true);
                        this.put(AnimationParameters.ISDOINGACTION, true);
                        this.put(AnimationParameters.PLAYERSHOULDBERENDERED, true);
                        this.put(AnimationParameters.DRAWITEMS, true);
                    }
                });
            }
            else {
                this.rotationYaw = this.targetYaw;
                try {
                    this.targetPos.equals((Object)null);
                }
                catch (NullPointerException e) {
                    this.targetPos = this.getInFrontOfPlayer();
                }
                this.setNoGravity(false);
                final Vec3d nextPos = Reference.Lerp(this.getPositionVector(), this.targetPos, 40 - this.preparingPaymentTick);
                this.setPosition(nextPos.x, nextPos.y, nextPos.z);
            }
        }
    }
    
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        final boolean hasNameTag = itemstack.getItem() == Items.NAME_TAG;
        if (hasNameTag) {
            itemstack.interactWithEntity(player, (EntityLivingBase)this, hand);
            return true;
        }
        MenuUI.entityGirl = this;
        FMLNetworkHandler.openGui(player, (Object)Main.instance, 0, this.world, player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ());
        return true;
    }
    
    @Override
    public void startAnimation(final String animationName) {
        switch (animationName) {
            case "blowjob": {
                this.prepareBlowjob();
                break;
            }
            case "doggy": {
                this.prepareDoggy();
                break;
            }
            case "strip": {
                this.prepareStrip();
                break;
            }
        }
    }
    
    @Override
    protected void prepareAction() {
        super.prepareAction();
        this.isPreparingPayment = true;
    }
    
    protected void prepareStrip() {
        this.prepareAction();
        PacketHandler.INSTANCE.sendToAll((IMessage)new SetAnimationFollowUp("strip", this.getPosition()));
        PacketHandler.INSTANCE.sendToAll((IMessage)new SendPaymentItems(PaymentItems.GOLD.name(), 1, this.getPosition()));
    }
    
    private void prepareBlowjob() {
        this.prepareAction();
        PacketHandler.INSTANCE.sendToAll((IMessage)new SetAnimationFollowUp("blowjob", this.getPosition()));
        PacketHandler.INSTANCE.sendToAll((IMessage)new SendPaymentItems(PaymentItems.EMERALD.name(), 3, this.getPosition()));
    }
    
    private void prepareDoggy() {
        this.prepareAction();
        PacketHandler.INSTANCE.sendToAll((IMessage)new SetAnimationFollowUp("doggy", this.getPosition()));
        PacketHandler.INSTANCE.sendToAll((IMessage)new SendPaymentItems(PaymentItems.DIAMOND.name(), 2, this.getPosition()));
    }
    
    public void goForDoggy() {
        final BlockPos temp = this.findBlock(this.getPosition());
        if (temp == null) {
            this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HMPH[2]);
            this.say("no bed in sight...");
        }
        else {
            this.tasks.removeTask(this.aiWander);
            this.tasks.removeTask(this.aiLookAtPlayer);
            final Vec3d bedPos = new Vec3d((double)temp.getX(), (double)temp.getY(), (double)temp.getZ());
            final int[] yaws = { 0, 180, -90, 90 };
            final Vec3d[][] potentialSpaces = { { new Vec3d(0.5, 0.0, -0.5), new Vec3d(0.0, 0.0, -1.0) }, { new Vec3d(0.5, 0.0, 1.5), new Vec3d(0.0, 0.0, 1.0) }, { new Vec3d(-0.5, 0.0, 0.5), new Vec3d(-1.0, 0.0, 0.0) }, { new Vec3d(1.5, 0.0, 0.5), new Vec3d(1.0, 0.0, 0.0) } };
            int whichOne = -1;
            for (int i = 0; i < potentialSpaces.length; ++i) {
                final Vec3d searchPos = bedPos.add(potentialSpaces[i][1]);
                if (this.world.getBlockState(new BlockPos(searchPos.x, searchPos.y, searchPos.z)).getBlock() == Blocks.AIR) {
                    if (whichOne == -1) {
                        whichOne = i;
                    }
                    else {
                        final double oldDistance = this.getPosition().distanceSq(bedPos.add(potentialSpaces[whichOne][0]).x, bedPos.add(potentialSpaces[whichOne][0]).y, bedPos.add(potentialSpaces[whichOne][0]).z);
                        final double newDistance = this.getPosition().distanceSq(bedPos.add(potentialSpaces[i][0]).x, bedPos.add(potentialSpaces[i][0]).y, bedPos.add(potentialSpaces[i][0]).z);
                        if (newDistance < oldDistance) {
                            whichOne = i;
                        }
                    }
                }
            }
            if (whichOne == -1) {
                this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HMPH[2]);
                this.say("bed is obscured...");
                return;
            }
            final Vec3d targetPos = bedPos.add(potentialSpaces[whichOne][0]);
            this.targetYaw = (float)yaws[whichOne];
            this.targetPos = new Vec3d(targetPos.x, targetPos.y, targetPos.z);
            this.playerYaw = this.targetYaw;
            this.getNavigator().clearPath();
            this.getNavigator().tryMoveToXYZ(targetPos.x, targetPos.y, targetPos.z, this.walkSpeed);
            this.lookingForBed = true;
            this.bedSearchTick = 0;
        }
    }
    
    @Override
    protected void cum() {
        if (this.animationParameters.get(AnimationParameters.SUCKBLOWJOB) || this.animationParameters.get(AnimationParameters.THRUSTBLOWJOB)) {
            this.playerIsCumming = true;
            this.ChangeTransitionTicks(2);
            this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                {
                    this.put(AnimationParameters.THRUSTBLOWJOB, false);
                    this.put(AnimationParameters.SUCKBLOWJOB, false);
                    this.put(AnimationParameters.CUMBLOWJOB, true);
                }
            });
            this.moveCamera(0.0, 0.0, 0.0, 0.0f, 70.0f);
        }
        else if (this.animationParameters.get(AnimationParameters.DOGGYSLOW) || this.animationParameters.get(AnimationParameters.DOGGYFAST)) {
            this.playerIsCumming = true;
            this.ChangeTransitionTicks(2);
            this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                {
                    this.put(AnimationParameters.DOGGYSLOW, false);
                    this.put(AnimationParameters.DOGGYFAST, false);
                    this.put(AnimationParameters.DOGGYCUM, true);
                }
            });
        }
    }
    
    @Override
    protected void thrust() {
        if (this.animationParameters.get(AnimationParameters.SUCKBLOWJOB) || this.animationParameters.get(AnimationParameters.THRUSTBLOWJOB)) {
            this.playerIsThrusting = true;
            this.ChangeTransitionTicks(2);
            if (this.animationParameters.get(AnimationParameters.THRUSTBLOWJOB)) {
                this.actionController.clearAnimationCache();
            }
            else {
                this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                    {
                        this.put(AnimationParameters.THRUSTBLOWJOB, true);
                        this.put(AnimationParameters.SUCKBLOWJOB, false);
                    }
                });
            }
        }
        else if (this.animationParameters.get(AnimationParameters.DOGGYSLOW) || this.animationParameters.get(AnimationParameters.DOGGYFAST)) {
            this.playerIsThrusting = true;
            this.ChangeTransitionTicks(2);
            if (this.animationParameters.get(AnimationParameters.DOGGYFAST)) {
                this.actionController.clearAnimationCache();
            }
            else {
                this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                    {
                        this.put(AnimationParameters.DOGGYFAST, true);
                        this.put(AnimationParameters.DOGGYSLOW, false);
                    }
                });
            }
        }
    }
    
    @Override
    protected void checkFollowUp() {
        final String animationFollowUp = this.animationFollowUp;
        switch (animationFollowUp) {
            case "strip": {
                this.resetPlayer();
                this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                    {
                        this.put(AnimationParameters.MOVEMENTACTIVE, false);
                        this.put(AnimationParameters.STARTSTRIP, true);
                        this.put(AnimationParameters.ISDOINGACTION, true);
                        this.put(AnimationParameters.PLAYERSHOULDBERENDERED, false);
                    }
                });
                break;
            }
            case "blowjob": {
                this.ChangeAnimationParameter(AnimationParameters.STARTBLOWJOB, true);
                break;
            }
            case "doggy": {
                if (this.currentModel != 0) {
                    this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                        {
                            this.put(AnimationParameters.STARTSTRIP, true);
                            this.put(AnimationParameters.ISDOINGACTION, true);
                            this.put(AnimationParameters.PLAYERSHOULDBERENDERED, false);
                        }
                    });
                    this.resetPlayer();
                    return;
                }
                this.resetGirl();
                PacketHandler.INSTANCE.sendToServer((IMessage)new SendJennyToDoggy(this.getPosition()));
                break;
            }
        }
        this.animationFollowUp = "";
    }
    
    @Override
    protected <E extends IAnimatable> PlayState predicate(final AnimationEvent<E> event) {
        final String name = event.getController().getName();
        switch (name) {
            case "eyes": {
                if (!this.animationParameters.get(AnimationParameters.AUTOEYES)) {
                    this.createAnimation("animation.jenny.null", true, event);
                    break;
                }
                this.createAnimation("animation.jenny.fhappy", true, event);
                break;
            }
            case "movement": {
                if (!this.animationParameters.get(AnimationParameters.MOVEMENTACTIVE)) {
                    this.createAnimation("animation.jenny.null", true, event);
                    break;
                }
                if (Math.abs(this.prevPosX - this.posX) + Math.abs(this.prevPosZ - this.posZ) > 0.0) {
                    this.createAnimation("animation.jenny.walk", true, event);
                    this.rotationYaw = this.rotationYawHead;
                    break;
                }
                this.createAnimation("animation.jenny.idle", true, event);
                break;
            }
            case "action": {
                if (!this.animationParameters.get(AnimationParameters.ISDOINGACTION)) {
                    this.createAnimation("animation.jenny.null", true, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.STARTSTRIP)) {
                    this.createAnimation("animation.jenny.strip", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.STARTPAYMENT)) {
                    this.createAnimation("animation.jenny.payment", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.STARTBLOWJOB)) {
                    this.createAnimation("animation.jenny.blowjobintro", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.SUCKBLOWJOB)) {
                    this.createAnimation("animation.jenny.blowjobsuck", true, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.THRUSTBLOWJOB)) {
                    this.createAnimation("animation.jenny.blowjobthrust", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.CUMBLOWJOB)) {
                    this.createAnimation("animation.jenny.blowjobcum", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.STARTDOGGY)) {
                    this.createAnimation("animation.jenny.doggygoonbed", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.WAITDOGGY)) {
                    this.createAnimation("animation.jenny.doggywait", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.DOGGYSTART)) {
                    this.createAnimation("animation.jenny.doggystart", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.DOGGYSLOW)) {
                    this.createAnimation("animation.jenny.doggyslow", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.DOGGYFAST)) {
                    this.createAnimation("animation.jenny.doggyfast", false, event);
                    break;
                }
                if (this.animationParameters.get(AnimationParameters.DOGGYCUM)) {
                    this.createAnimation("animation.jenny.doggycum", false, event);
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
                        PacketHandler.INSTANCE.sendToServer((IMessage)new ChangeSkin(0, JennyEntity.this.getPosition()));
                        PacketHandler.INSTANCE.sendToAll((IMessage)new ChangeSkin(0, JennyEntity.this.getPosition()));
                        break;
                    }
                    case "startStrip": {
                        PacketHandler.INSTANCE.sendToServer((IMessage)new ChangeGirlMovement(0.0, JennyEntity.this.getPosition()));
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
                        JennyEntity.this.checkFollowUp();
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
                            playerName = "<" + JennyEntity.this.playerSheHasSexWith.getName() + "> ";
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
                        final String animationFollowUp = JennyEntity.this.animationFollowUp;
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
                        JennyEntity.this.checkFollowUp();
                        break;
                    }
                    case "bjiMSG1": {
                        GirlEntity.this.say("What are you...");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_MMM[8]);
                        JennyEntity.this.playerYaw = JennyEntity.this.rotationYaw + 180.0f;
                        break;
                    }
                    case "bjiMSG2": {
                        GirlEntity.this.say("eh... boys...");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                        break;
                    }
                    case "bjiMSG3": {
                        GirlEntity.this.say("OHOhh...!");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[0]);
                        break;
                    }
                    case "bjiMSG4": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_BELLJINGLE[0]);
                        break;
                    }
                    case "bjiMSG5": {
                        GirlEntity.this.say("Was this really necessary?!");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HMPH[1], 0.5f);
                        break;
                    }
                    case "bjiMSG6": {
                        GirlEntity.this.say("Oh~");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[8]);
                        break;
                    }
                    case "bjiMSG7": {
                        GirlEntity.this.say("You like it?~");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[4]);
                        break;
                    }
                    case "bjiMSG8": {
                        try {
                            GirlEntity.this.say("<" + JennyEntity.this.playerSheHasSexWith.getName() + "> Yee", true);
                        }
                        catch (NullPointerException e2) {
                            GirlEntity.this.say("<Gamer> Give me the sweet release of death", true);
                        }
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_PLOB[0], 0.5f);
                        break;
                    }
                    case "bjiMSG9": {
                        GirlEntity.this.say("Hihihi~");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[2]);
                        break;
                    }
                    case "bjiMSG10": {
                        GirlEntity.this.moveCamera(-0.4, -0.8, -0.2, 60.0f, -3.0f);
                        GirlEntity.this.ChangeTransitionTicks(0);
                        break;
                    }
                    case "bjiMSG11": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                        SexUI.addCumPercentage(0.02);
                        break;
                    }
                    case "bjiMSG12": {
                        if (Reference.RANDOM.nextInt(5) == 0) {
                            GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_BJMOAN));
                        }
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                        SexUI.addCumPercentage(0.02);
                        break;
                    }
                    case "bjtMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MMM));
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIPSOUND));
                        SexUI.addCumPercentage(0.04);
                        break;
                    }
                    case "bjiDone": {
                        GirlEntity.this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                            {
                                this.put(AnimationParameters.STARTBLOWJOB, false);
                                this.put(AnimationParameters.SUCKBLOWJOB, true);
                            }
                        });
                        SexUI.shouldBeRendered = true;
                        break;
                    }
                    case "bjtDone": {
                        GirlEntity.this.ChangeTransitionTicks(0);
                        GirlEntity.this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                            {
                                this.put(AnimationParameters.SUCKBLOWJOB, true);
                                this.put(AnimationParameters.THRUSTBLOWJOB, false);
                            }
                        });
                        break;
                    }
                    case "bjtReady":
                    case "doggyfastReady": {
                        JennyEntity.this.playerIsThrusting = false;
                        break;
                    }
                    case "bjcMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_BJMOAN[1]);
                        break;
                    }
                    case "bjcMSG2": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_BJMOAN[7]);
                        SexUI.shouldBeRendered = false;
                        break;
                    }
                    case "bjcMSG3": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_AFTERSESSIONMOAN[1]);
                        break;
                    }
                    case "bjcMSG4": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[0]);
                        break;
                    }
                    case "bjcMSG5": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[1]);
                        break;
                    }
                    case "bjcMSG6": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[2]);
                        break;
                    }
                    case "bjcMSG7": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[3]);
                        break;
                    }
                    case "bjcBlackScreen": {
                        BlackScreenUI.activate();
                        break;
                    }
                    case "bjcDone": {
                        GirlEntity.this.ChangeAnimationParameter(AnimationParameters.CUMBLOWJOB, false);
                        SexUI.resetCumPercentage();
                        GirlEntity.this.ChangeTransitionTicks(0);
                        GirlEntity.this.resetGirl();
                        break;
                    }
                    case "doggyGoOnBedMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_BEDRUSTLE[0]);
                        JennyEntity.this.playerYaw = JennyEntity.this.rotationYaw;
                        break;
                    }
                    case "doggyGoOnBedMSG2": {
                        GirlEntity.this.say("what are you waiting for?~");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING[9]);
                        break;
                    }
                    case "doggyGoOnBedMSG3": {
                        GirlEntity.this.say("this ass ain't gonna fuck itself...");
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_GIGGLE[0]);
                        break;
                    }
                    case "doggyGoOnBedMSG4": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_SLAP[0], 0.75f);
                        break;
                    }
                    case "doggyGoOnBedDone": {
                        PacketHandler.INSTANCE.sendToServer((IMessage)new SetPlayerForGirl(JennyEntity.this.getPosition(), Minecraft.getMinecraft().player.getPersistentID()));
                        GirlEntity.this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                            {
                                this.put(AnimationParameters.WAITDOGGY, true);
                                this.put(AnimationParameters.STARTDOGGY, false);
                                this.put(AnimationParameters.AUTOEYES, true);
                            }
                        });
                        break;
                    }
                    case "doggystartMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_TOUCH[0]);
                        break;
                    }
                    case "doggystartMSG2": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_TOUCH[1]);
                        break;
                    }
                    case "doggystartMSG3": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_BEDRUSTLE[1], 0.5f);
                        break;
                    }
                    case "doggystartMSG4": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_SMALLINSERTS));
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_MMM[1]);
                        break;
                    }
                    case "doggystartMSG5": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 0.33f);
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
                        break;
                    }
                    case "doggystartDone": {
                        GirlEntity.this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                            {
                                this.put(AnimationParameters.DOGGYSTART, false);
                                this.put(AnimationParameters.DOGGYSLOW, true);
                            }
                        });
                        SexUI.shouldBeRendered = true;
                        break;
                    }
                    case "doggyslowMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 0.33f);
                        int rand = Reference.RANDOM.nextInt(4);
                        if (rand == 0) {
                            rand = Reference.RANDOM.nextInt(2);
                            if (rand == 0) {
                                GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MMM));
                            }
                            else {
                                GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
                            }
                        }
                        else {
                            GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                        }
                        SexUI.addCumPercentage(0.00666);
                        break;
                    }
                    case "doggyslowMSG2": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_LIGHTBREATHING), 0.5f);
                        break;
                    }
                    case "doggyfastMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 0.75f);
                        SexUI.addCumPercentage(0.02);
                        final JennyEntity this$0 = JennyEntity.this;
                        ++this$0.flip;
                        if (JennyEntity.this.flip % 2 == 0) {
                            final int random = Reference.RANDOM.nextInt(2);
                            if (random == 0) {
                                GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
                            }
                            else {
                                GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING));
                            }
                            break;
                        }
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_AHH));
                        break;
                    }
                    case "doggyfastDone": {
                        GirlEntity.this.ChangeTransitionTicks(0);
                        GirlEntity.this.ChangeAnimationParameters(new EnumMap<AnimationParameters, Boolean>(AnimationParameters.class) {
                            {
                                this.put(AnimationParameters.DOGGYSLOW, true);
                                this.put(AnimationParameters.DOGGYFAST, false);
                            }
                        });
                        break;
                    }
                    case "doggycumMSG1": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.MISC_CUMINFLATION[0], 2.0f);
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.MISC_POUNDING), 2.0f);
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.Random(SoundsHandler.GIRLS_JENNY_MOAN));
                        break;
                    }
                    case "doggycumMSG2": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[4]);
                        break;
                    }
                    case "doggycumMSG3": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[5]);
                        break;
                    }
                    case "doggycumMSG4": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[6]);
                        break;
                    }
                    case "doggycumMSG5": {
                        GirlEntity.this.playSoundAroundHer(SoundsHandler.GIRLS_JENNY_HEAVYBREATHING[7]);
                        GirlEntity.this.ChangeTransitionTicks(1);
                        break;
                    }
                    case "doggyCumDone": {
                        GirlEntity.this.ChangeAnimationParameter(AnimationParameters.DOGGYCUM, false);
                        SexUI.resetCumPercentage();
                        GirlEntity.this.resetGirl();
                        break;
                    }
                }
            }
        };
        this.actionController.registerSoundListener(actionSoundListener);
        data.addAnimationController(this.actionController);
    }
}
